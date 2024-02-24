package com.neteye.unitTests;

import com.neteye.TestContainers;
import com.neteye.components.DeviceSearcher;
import com.neteye.persistence.entities.Device;
import com.neteye.persistence.entities.PortInfo.PortInfo;
import com.neteye.persistence.repositories.DeviceRepository;
import com.neteye.persistence.repositories.PortInfoRepository;
import com.neteye.utils.enums.DefaultServerPortNumbers;
import com.neteye.utils.enums.commonServers.WwwServers;
import com.neteye.utils.misc.IpAddress;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

public class ScannerTest extends TestContainers {

    @Autowired
    DeviceSearcher deviceSearcher;
    @Autowired
    DeviceRepository deviceRepository;
    @Autowired
    PortInfoRepository portInfoRepository;

    @AfterEach
    void clearDb() {
        deviceRepository.deleteAll();
        portInfoRepository.deleteAll();
    }

    //we want to get info from localhost. we have to use reflections, because private ips are in restricted addresses
    //that are skipped during scan, so we are invoking checking method directly
    @Test
    void shouldGetInfo() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, UnknownHostException {
        Class<?> scannerClass = DeviceSearcher.class;
        Method checkIp = scannerClass.getDeclaredMethod("checkIp", IpAddress.class);
        Method setup = scannerClass.getDeclaredMethod("setUp", Map.class);
        Method howManyLeft = scannerClass.getDeclaredMethod("setHowManyIpsLeft", AtomicLong.class);

        Map<String, String> params = new HashMap<>();
        params.put("scannedPorts", "HTTP8080 ");
        params.put("deviceResponseTimeout", "1000");
        params.put("serviceResponseTimeout", "10000");

        IpAddress localhost = new IpAddress(InetAddress.getLocalHost().getHostAddress());


        checkIp.setAccessible(true);
        setup.setAccessible(true);
        howManyLeft.setAccessible(true);

        howManyLeft.invoke(deviceSearcher, new AtomicLong(1L));
        setup.invoke(deviceSearcher, params);
        checkIp.invoke(deviceSearcher, localhost);

        Optional<Device> device = deviceRepository.findByIp(localhost.toString());
        List<PortInfo> ports = portInfoRepository.findAllByPrimaryKeyIp(localhost.toString());
        Assertions.assertAll(
                () -> Assertions.assertTrue(device.isPresent()),
                () -> Assertions.assertFalse(ports.isEmpty()),
                () -> {
                    List<Integer> portNumbers = ports.stream()
                            .map(x -> x.getPrimaryKey().getPort())
                            .toList();
                    Assertions.assertTrue(portNumbers.contains(8080));
                }
        );
    }

    @Test
    void shouldNotStartScanningWhenProvidedAddressesFromRestrictedPool() throws InterruptedException {
        Map<String, String> params = new HashMap<>();
        params.put("scannedPorts", "HTTP HTTP8080 HTTPS Telnet SMTP FTP IMAP POP3 RTSP");
        params.put("deviceResponseTimeout", "700");
        params.put("serviceResponseTimeout", "10000");
        params.put("startingIP", "192.168.0.0");
        params.put("endingIP", "192.168.255.255");
        deviceSearcher.prepareToScan(params);

        List<Device> allDevices = deviceRepository.findAll();
        Assertions.assertTrue(allDevices.isEmpty());
    }

}
