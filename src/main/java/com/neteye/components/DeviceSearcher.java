package com.neteye.components;

import com.neteye.persistence.repositories.DeviceRepository;
import com.neteye.utils.Identify;
import com.neteye.utils.IpAddress;
import com.neteye.utils.enums.PortNumbersEnum;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicIntegerArray;

@Component
@Log4j2
public class DeviceSearcher {
    private final DeviceRepository deviceRepository;
    private AtomicIntegerArray ip = new AtomicIntegerArray(4);
    private IpAddress lastAddress = new IpAddress("224.0.0.0");
    @Autowired
    public DeviceSearcher(DeviceRepository deviceRepository) {
        this.deviceRepository = deviceRepository;
    }

    public void search(String... range) {
        ip = new IpAddress(range[0]).getAtomicIntegerArrayIP();
        lastAddress = new IpAddress(range[1]);
        log.info("Stated scanning with ip {}.{}.{}.{}.", ip.get(0), ip.get(1),ip.get(2), ip.get(3));
        scan();
        log.info("Scanning ended");
    }
    private void scan() {
        IpAddress currentIp = new IpAddress();
        InetAddress inetAddress;
        while (new IpAddress(ip).isLesser(lastAddress)) {
            synchronized (this) {
                ip = new IpAddress(ip).increment().getAtomicIntegerArrayIP();
                currentIp.setAddress(ip);
            }
            try {
                inetAddress = currentIp.getIP();
                if(inetAddress.isReachable(500)) {
                    lookForOpenPorts(inetAddress, currentIp);
                }
            }
            catch (Exception e) {
                //there will be a lot of insignificant exceptions due to isReachable method implementation
            }
        }
    }

    private void lookForOpenPorts(InetAddress address, IpAddress currentIp) {
        for (PortNumbersEnum portNumber : PortNumbersEnum.values()) {
            try(Socket socket = new Socket()) {
                socket.connect(new InetSocketAddress(address, portNumber.getValue()), 500);
                log.info("Current ip - {}. Current port - {}", currentIp, portNumber.getValue());
                if(socket.isConnected()) {
                    Identify.fetchInfo(portNumber, currentIp);
                }
            } catch (Exception e) {
                //there will be a lot of insignificant exceptions due to connect method implementation
            }
        }
    }

}
