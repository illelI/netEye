package com.neteye.components;

import com.neteye.persistence.entities.Device;
import com.neteye.persistence.entities.IpBlackList;
import com.neteye.persistence.entities.PortInfo.PortInfo;
import com.neteye.persistence.entities.PortInfo.PortInfoPrimaryKey;
import com.neteye.persistence.repositories.DeviceRepository;
import com.neteye.persistence.repositories.IpBlackListRepository;
import com.neteye.persistence.repositories.PortInfoRepository;
import com.neteye.utils.enums.DefaultServerPortNumbers;
import com.neteye.utils.enums.RestrictedAddresses;
import com.neteye.utils.misc.Identify;
import com.neteye.utils.misc.IpAddress;
import com.neteye.utils.misc.ServiceInfo;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.stereotype.Component;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicIntegerArray;
import java.util.concurrent.atomic.AtomicLong;

@Component
@Log4j2
public class DeviceSearcher {
    private final DeviceRepository deviceRepository;
    private final PortInfoRepository portInfoRepository;
    private final IpBlackListRepository blackListRepository;
    @Setter
    private int deviceResponseTimeout = 700;
    @Setter
    private int numberOfThreads = 10000;
    private List<DefaultServerPortNumbers> portsToCheck = new ArrayList<>();
    @Setter
    private AtomicLong howManyIpsLeft;

    @Autowired
    public DeviceSearcher(DeviceRepository deviceRepository, PortInfoRepository portInfoRepository, IpBlackListRepository blackListRepository) {
        this.deviceRepository = deviceRepository;
        this.portInfoRepository = portInfoRepository;
        this.blackListRepository = blackListRepository;
    }

    public void prepareToScan(Map<String, String> searchProperties) throws InterruptedException {
        IpAddress lastAddress;

        List<IpAddress> ipAddresses = new ArrayList<>();
        List<String> blacklist = blackListRepository.findAll().stream()
                .map(IpBlackList::getIp)
                .toList();

        AtomicIntegerArray firstAddress = new IpAddress(searchProperties.get("startingIP")).getAtomicIntegerArrayIP();
        IpAddress firstAddressIp = new IpAddress(firstAddress);
        lastAddress = new IpAddress(searchProperties.get("endingIP"));
        log.info("Stated scanning with ip {}.{}.{}.{}", firstAddress.get(0), firstAddress.get(1), firstAddress.get(2), firstAddress.get(3));

        setUp(searchProperties);

        ipAddresses.add(firstAddressIp);

        long loopCondition = IpAddress.calculateHowManyIpsAreInRange(new IpAddress(firstAddress), lastAddress);

        for (long i = 1; i < loopCondition; i++) {
            IpAddress currentIpAddress = new IpAddress(firstAddressIp.increment().getAddress());
            for (RestrictedAddresses restrictedAddress : RestrictedAddresses.values()) {
                if (currentIpAddress.ipInRange(restrictedAddress.getFirstAddress(), restrictedAddress.getLastAddress())) {
                    i += IpAddress.calculateHowManyIpsAreInRange(restrictedAddress.getFirstAddress(), restrictedAddress.getLastAddress());
                    currentIpAddress = new IpAddress(restrictedAddress.getLastAddress().toString()).increment();
                }
            }
            if (!blacklist.contains(currentIpAddress.toString())) {
                ipAddresses.add(new IpAddress(currentIpAddress.toString()));
            }
        }

        this.howManyIpsLeft = new AtomicLong(ipAddresses.size());

        ForkJoinPool forkJoinPool = new ForkJoinPool(numberOfThreads);
        forkJoinPool.submit(() -> ipAddresses.stream().parallel().forEach(this::checkIp));

        while (howManyIpsLeft.get() > 0) {
            TimeUnit.SECONDS.sleep(10);
            log.info(howManyIpsLeft);
        }
        forkJoinPool.close();

        log.info("Scanning ended");

    }

    private void setUp(Map<String, String> searchProperties) {
        portsToCheck.clear();
        if (searchProperties.containsKey("numberOfThreads")) {
            this.setNumberOfThreads(Integer.parseInt(searchProperties.get("numberOfThreads")));
        }

        if (searchProperties.containsKey("deviceResponseTimeout")) {
            this.setDeviceResponseTimeout(Integer.parseInt(searchProperties.get("deviceResponseTimeout")));
        }

        if (searchProperties.containsKey("serviceResponseTimeout")) {
            Identify.setConnectionTimeout(Integer.parseInt(searchProperties.get("serviceResponseTimeout")));
        }

        if (searchProperties.containsKey("scannedPorts")) {
            String[] split = searchProperties.get("scannedPorts").trim().split(" ");
            for (String s : split) {
                addPortsToList(s);
            }
        }
    }

    private void addPortsToList(String string) {
        DefaultServerPortNumbers port = switch (string) {
            case "FTP" -> DefaultServerPortNumbers.FTP;
            case "Telnet" -> DefaultServerPortNumbers.TELNET;
            case "SMTP" -> DefaultServerPortNumbers.SMTP;
            case "HTTP" -> DefaultServerPortNumbers.HTTP;
            case "HTTP8080" -> DefaultServerPortNumbers.HTTP8080;
            case "HTTPS" -> DefaultServerPortNumbers.HTTPS;
            case "POP3" -> DefaultServerPortNumbers.POP3;
            case "IMAP" -> DefaultServerPortNumbers.IMAP;
            case "RTSP" -> DefaultServerPortNumbers.RTSP;
            default -> null;
        };

        if (port != null) {
            portsToCheck.add(port);
        }
    }

    private void checkIp(IpAddress currentAddress){
        try {
            InetAddress inetAddress = currentAddress.getIP();
            if(inetAddress.isReachable(deviceResponseTimeout)) {
                List<PortInfo> foundPorts = portsToCheck.stream().parallel()
                        .map(portNumber -> checkPort(currentAddress, portNumber))
                        .filter(Objects::nonNull)
                        .toList();
                if (!foundPorts.isEmpty()) {
                    String hostname = inetAddress.getHostName();

                    String system = Identify.getOperatingSystem(foundPorts);
                    String location = Identify.getLocation(currentAddress);

                    String typeOfDevice = "server";

                    for (PortInfo info : foundPorts) {
                        if (info.getPrimaryKey().getPort() == DefaultServerPortNumbers.RTSP.getPortNumber()) {
                            typeOfDevice = "camera";
                            break;
                        }
                    }

                    Device device = new Device(
                            currentAddress.toString(),
                            foundPorts,
                            hostname,
                            location,
                            system,
                            typeOfDevice
                    );

                    saveToDb(device, foundPorts);
                }
            }
        }
        catch (Exception e) {
            //insignificant exceptions
        }
        howManyIpsLeft.decrementAndGet();
    }

    private PortInfo checkPort(IpAddress ip, DefaultServerPortNumbers port) {
        try (Socket socket = new Socket()) {
            socket.connect(new InetSocketAddress(ip.getIP(), port.getPortNumber()), deviceResponseTimeout);
            if (socket.isConnected()) {
                ServiceInfo serviceInfo = new ServiceInfo(ip.getIP(), port);
                serviceInfo = Identify.fetchPortInfo(serviceInfo);
                return new PortInfo(
                        new PortInfoPrimaryKey(ip.toString(), port.getPortNumber()),
                        serviceInfo.getInfo(),
                        serviceInfo.getAppName(),
                        serviceInfo.getVersion()
                );
            }
        } catch (Exception e) {
            //there will be a lot of insignificant exceptions
        }
        return null;
    }

    private void saveToDb(Device device, List<PortInfo> portInfos) {
        try {
            deviceRepository.save(device);
            portInfoRepository.saveAll(portInfos);
        } catch (InvalidDataAccessApiUsageException ex) {
        }
    }

}
