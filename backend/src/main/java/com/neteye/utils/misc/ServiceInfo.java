package com.neteye.utils.misc;

import com.neteye.utils.enums.PortNumbersEnum;
import lombok.Data;

import java.net.InetAddress;

@Data
public class ServiceInfo {
    PortNumbersEnum port;
    InetAddress ip;
    String info;
    String appName;
    String version;

    public ServiceInfo(InetAddress ip, PortNumbersEnum port) {
        this.ip = ip;
        this.port = port;
    }
}
