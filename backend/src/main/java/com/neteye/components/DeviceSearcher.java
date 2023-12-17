package com.neteye.components;

import com.neteye.persistence.entities.Device;
import com.neteye.persistence.repositories.DeviceRepository;
import com.neteye.utils.Identify;
import com.neteye.utils.IpAddress;
import com.neteye.utils.enums.PortNumbersEnum;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
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
    private IpAddress lastAddress = new IpAddress("224.0.0.0");

    @Setter
    private int numberOfThreads = 10000;
    private AtomicInteger howManyThreadsLeft;

    public DeviceSearcher(DeviceRepository deviceRepository) {
        this.deviceRepository = deviceRepository;
    }

    public void search(Map<String, String> searchProperties) throws InterruptedException {
        long numbersOfIpsPerThread;
        ExecutorService executorService;

        AtomicIntegerArray firstAddress = new IpAddress(searchProperties.get("startingIP")).getAtomicIntegerArrayIP();
        lastAddress = new IpAddress(searchProperties.get("endingIP"));
        log.info("Stated scanning with ip {}.{}.{}.{}", firstAddress.get(0), firstAddress.get(1), firstAddress.get(2), firstAddress.get(3));

        if (searchProperties.containsKey("numberOfThreads")) {
            log.info("{}", searchProperties.get("numberOfThreads"));
            this.setNumberOfThreads(Integer.parseInt(searchProperties.get("numberOfThreads")));
        }
        howManyThreadsLeft = new AtomicInteger(numberOfThreads);
        executorService = Executors.newFixedThreadPool(numberOfThreads);

        numbersOfIpsPerThread = IpAddress.calculateHowManyIpsAreInRange(new IpAddress(firstAddress), lastAddress) / numberOfThreads;

        if (numbersOfIpsPerThread == 0) {
            numberOfThreads = (int) IpAddress.calculateHowManyIpsAreInRange(new IpAddress(firstAddress), lastAddress);
            numbersOfIpsPerThread = 1;
        }

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

    private void scan(IpAddress firstAddress, IpAddress localLastAddress){
        InetAddress inetAddress;
        while (firstAddress.isLesser(lastAddress) && firstAddress.isLesser(localLastAddress)) {
            try {
                inetAddress = firstAddress.getIP();
                if(inetAddress.isReachable(700)) {
                    lookForOpenPorts(inetAddress, firstAddress);
                }
            }
            catch (Exception e) {
                //there will be a lot of insignificant exceptions
            } finally {
                firstAddress.increment();
            }
        }
        howManyThreadsLeft.decrementAndGet();
    }

    private void lookForOpenPorts(InetAddress address, IpAddress currentIp) {
        for (PortNumbersEnum portNumber : PortNumbersEnum.values()) {
            try(Socket socket = new Socket()) {
                socket.connect(new InetSocketAddress(address, portNumber.getValue()), 700);
                if(socket.isConnected()) {
                    Device device = new Device(currentIp.toString(), portNumber.getValue(), Identify.fetchInfo(portNumber, currentIp));
                    deviceRepository.save(device);
                }
            } catch (Exception e) {
                //there will be a lot of insignificant exceptions
            }
        }
    }
}
