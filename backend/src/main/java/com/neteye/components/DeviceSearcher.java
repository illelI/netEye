package com.neteye.components;

import com.neteye.persistence.entities.Device;
import com.neteye.persistence.entities.PortInfo.PortInfo;
import com.neteye.persistence.entities.PortInfo.PortInfoPrimaryKey;
import com.neteye.persistence.repositories.DeviceRepository;
import com.neteye.persistence.repositories.PortInfoRepository;
import com.neteye.utils.Identify;
import com.neteye.utils.misc.IpAddress;
import com.neteye.utils.enums.PortNumbersEnum;
import com.neteye.utils.misc.ServiceInfo;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicIntegerArray;

@Component
@Log4j2
public class DeviceSearcher {
    private final DeviceRepository deviceRepository;
    private final PortInfoRepository portInfoRepository;
    private IpAddress lastAddress = new IpAddress("224.0.0.0");

    @Setter
    private int numberOfThreads = 10000;
    private AtomicInteger howManyThreadsLeft;

    public DeviceSearcher(DeviceRepository deviceRepository, PortInfoRepository portInfoRepository) {
        this.deviceRepository = deviceRepository;
        this.portInfoRepository = portInfoRepository;
    }

    public void search(Map<String, String> searchProperties) throws InterruptedException {
        long numbersOfIpsPerThread;
        ExecutorService executorService;

        AtomicIntegerArray firstAddress = new IpAddress(searchProperties.get("startingIP")).getAtomicIntegerArrayIP();
        lastAddress = new IpAddress(searchProperties.get("endingIP"));
        log.info("Stated scanning with ip {}.{}.{}.{}", firstAddress.get(0), firstAddress.get(1), firstAddress.get(2), firstAddress.get(3));

        if (searchProperties.containsKey("numberOfThreads")) {
            this.setNumberOfThreads(Integer.parseInt(searchProperties.get("numberOfThreads")));
        }

        numbersOfIpsPerThread = IpAddress.calculateHowManyIpsAreInRange(new IpAddress(firstAddress), lastAddress) / numberOfThreads;

        if (numbersOfIpsPerThread == 0) {
            numberOfThreads = (int) IpAddress.calculateHowManyIpsAreInRange(new IpAddress(firstAddress), lastAddress);
            numbersOfIpsPerThread = 1;
        }

        howManyThreadsLeft = new AtomicInteger(numberOfThreads);
        executorService = Executors.newFixedThreadPool(numberOfThreads);

        long finalNumbersOfIpsPerThread = numbersOfIpsPerThread;

        IpAddress tmpAddress;

        for (int i = 0; i < numberOfThreads; i++) {
            tmpAddress = IpAddress.addToIp(new IpAddress(firstAddress), numbersOfIpsPerThread * i);
            IpAddress finalTmpAddress = tmpAddress;
            executorService.submit(() -> scan(finalTmpAddress, IpAddress.addToIp(finalTmpAddress, finalNumbersOfIpsPerThread)));
        }

        executorService.shutdown();
        while (!executorService.isTerminated()) {
            TimeUnit.SECONDS.sleep(10);
            log.info(howManyThreadsLeft);
        }
        executorService.close();
        log.info("Scanning ended");

    }

    private void scan(IpAddress currentAddress, IpAddress localLastAddress){
        InetAddress inetAddress;
        while (currentAddress.isLesser(lastAddress) && currentAddress.isLesser(localLastAddress)) {
            try {
                inetAddress = currentAddress.getIP();
                if(inetAddress.isReachable(700)) {
                    List<Integer> foundPorts = lookForOpenPorts(inetAddress, currentAddress);
                    if (!foundPorts.isEmpty()) {
                        deviceRepository.save(new Device(
                                currentAddress.toString(),
                                foundPorts,
                                inetAddress.getHostName(),
                                "", "", ""
                        ));
                    }
                }
            }
            catch (Exception e) {
                //there will be a lot of insignificant exceptions
            } finally {
                currentAddress.increment();
            }
        }
        howManyThreadsLeft.decrementAndGet();
    }

    private List<Integer> lookForOpenPorts(InetAddress address, IpAddress currentIp) {
        List<Integer> foundPorts = new ArrayList<>();
        for (PortNumbersEnum portNumber : PortNumbersEnum.values()) {
            if (portNumber != PortNumbersEnum.FTP) continue;
            try(Socket socket = new Socket()) {
                socket.connect(new InetSocketAddress(address, portNumber.getValue()), 700);
                if(socket.isConnected()) {
                    foundPorts.add(portNumber.getValue());
                    ServiceInfo serviceInfo = new ServiceInfo(address, portNumber);
                    serviceInfo = Identify.fetchPortInfo(serviceInfo);
                    portInfoRepository.save(new PortInfo(
                            new PortInfoPrimaryKey(currentIp.toString(), portNumber.getValue()),
                            serviceInfo.getInfo(),
                            serviceInfo.getAppName(),
                            serviceInfo.getVersion()
                    ));
                }
            } catch (Exception e) {
                //there will be a lot of insignificant exceptions
            }
        }
        return foundPorts;
    }
}
