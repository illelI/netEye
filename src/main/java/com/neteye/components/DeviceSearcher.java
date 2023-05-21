package com.neteye.components;

import com.neteye.persistence.repositories.DeviceRepository;
import com.neteye.utils.Identify;
import com.neteye.utils.IpAddress;
import com.neteye.utils.enums.PortNumbersEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicIntegerArray;

import static com.neteye.NetEyeApplication.logger;

@Component
public class DeviceSearcher {

    private final DeviceRepository deviceRepository;
    private AtomicIntegerArray ip = new AtomicIntegerArray(4);
    private IpAddress lastAddress = new IpAddress("224.0.0.0");
    @Autowired
    public DeviceSearcher(DeviceRepository deviceRepository) {
        this.deviceRepository = deviceRepository;
    }

    public void search(String... range) {
        ip.set(0,1);
        ip.set(1,0);
        ip.set(2,0);
        ip.set(3,0);
        if(range.length > 0) {
            ip = new IpAddress(range[0]).getAtomicIntegerArrayIP();
            lastAddress = new IpAddress(range[1]);
        }
        logger.info("Stated scanning with ip " + ip.get(0) + "." + ip.get(1) + "." + ip.get(2) + "." + ip.get(3));
        scan();
        logger.info("Ended scanning");
    }
    public void scan() {
        IpAddress currentIp = new IpAddress();
        InetAddress inetAddress;
        Socket socket;
        while (new IpAddress(ip).isLesser(lastAddress)) {
            synchronized (this) {
                ip = new IpAddress(ip).increment().getAtomicIntegerArrayIP();
                currentIp.setAddress(ip);
            }
            try {
                inetAddress = currentIp.getIP();
                logger.info(inetAddress.getHostAddress());
                if(inetAddress.isReachable(500)) {
                    for (PortNumbersEnum portNumber : PortNumbersEnum.values()) {
                        try {
                            socket = new Socket();
                            socket.connect(new InetSocketAddress(inetAddress, portNumber.getValue()), 500);
                            if(socket.isConnected()) {
                                Identify.fetchInfo(portNumber, currentIp);
                            }
                        } catch (Exception e) {
                        }
                    }
                }
            }
            catch (Exception e) {
                logger.info(e.getMessage());
            }
        }
    }
}
