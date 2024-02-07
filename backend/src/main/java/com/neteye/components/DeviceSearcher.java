package com.neteye.components;

import com.neteye.persistence.entities.Device;
import com.neteye.persistence.entities.PortInfo.PortInfo;
import com.neteye.persistence.entities.PortInfo.PortInfoPrimaryKey;
import com.neteye.persistence.repositories.DeviceRepository;
import com.neteye.persistence.repositories.PortInfoRepository;
import com.neteye.utils.Identify;
import com.neteye.utils.enums.DefaultServerPortNumbers;
import com.neteye.utils.misc.IpAddress;
import com.neteye.utils.misc.ServiceInfo;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
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

    @Setter
    private int numberOfThreads = 10000;
    private AtomicLong howManyIpsLeft;

    @Autowired
    public DeviceSearcher(DeviceRepository deviceRepository, PortInfoRepository portInfoRepository) {
        this.deviceRepository = deviceRepository;
        this.portInfoRepository = portInfoRepository;
    }

    public void search(Map<String, String> searchProperties) throws InterruptedException {
        IpAddress lastAddress;

        List<IpAddress> ipAddresses = new ArrayList<>();

        AtomicIntegerArray firstAddress = new IpAddress(searchProperties.get("startingIP")).getAtomicIntegerArrayIP();
        IpAddress firstAddressIp = new IpAddress(firstAddress);
        lastAddress = new IpAddress(searchProperties.get("endingIP"));
        log.info("Stated scanning with ip {}.{}.{}.{}", firstAddress.get(0), firstAddress.get(1), firstAddress.get(2), firstAddress.get(3));

        if (searchProperties.containsKey("numberOfThreads")) {
            this.setNumberOfThreads(Integer.parseInt(searchProperties.get("numberOfThreads")));
        }
        howManyIpsLeft = new AtomicLong(IpAddress.calculateHowManyIpsAreInRange(new IpAddress(firstAddress), lastAddress));
        ipAddresses.add(firstAddressIp);
        for (long i = 0; i < IpAddress.calculateHowManyIpsAreInRange(new IpAddress(firstAddress), lastAddress); i++) {
            IpAddress currentIpAddress = new IpAddress(firstAddressIp.increment().getAddress());
            ipAddresses.add(new IpAddress(currentIpAddress.toString()));
        }

        ForkJoinPool forkJoinPool = new ForkJoinPool(numberOfThreads);
        forkJoinPool.submit(() ->
                ipAddresses.parallelStream().forEach(this::checkIp)
        );

        while (howManyIpsLeft.get() > 0) {
            TimeUnit.SECONDS.sleep(10);
            log.info(howManyIpsLeft);
        }
        forkJoinPool.close();

        log.info("Scanning ended");

    }

    private void checkIp(IpAddress currentAddress){
            try {
                InetAddress inetAddress = currentAddress.getIP();
                if(inetAddress.isReachable(700)) {
                    List<PortInfo> foundPorts = Arrays.stream(DefaultServerPortNumbers.values()).parallel()
                            .map(portNumber -> scanPort(currentAddress, portNumber))
                            .filter(Objects::nonNull)
                            .toList();
                    if (!foundPorts.isEmpty()) {
                        saveToDb(currentAddress, foundPorts);
                    }
                }
            }
            catch (Exception e) {
                log.error(e);
            }
        howManyIpsLeft.decrementAndGet();
    }

    private PortInfo scanPort(IpAddress ip, DefaultServerPortNumbers port) {
        try (Socket socket = new Socket()) {
            socket.connect(new InetSocketAddress(ip.getIP(), port.getPortNumber()), 700);
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

    private void saveToDb(IpAddress ipAddress, List<PortInfo> portInfos) {
        String hostname;
        try {
            hostname = ipAddress.getIP().getHostName();
        } catch (UnknownHostException e) {
            hostname = "";
        }
        portInfoRepository.saveAll(portInfos);

        String system = Identify.getOperatingSystem(portInfos);
        String location = Identify.getLocation(ipAddress);
        String typeOfDevice = Identify.checkIfDeviceIsCamera(ipAddress.toString()) ? "Camera" : "Server";

        deviceRepository.save(new Device(
                ipAddress.toString(),
                portInfos,
                hostname,
                location,
                system,
                typeOfDevice
        ));
    }
}
