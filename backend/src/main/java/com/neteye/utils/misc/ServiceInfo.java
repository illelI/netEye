package com.neteye.utils.misc;

import com.neteye.utils.enums.DefaultServerPortNumbers;
import lombok.Data;

import java.net.InetAddress;

@Data
public class ServiceInfo {
    DefaultServerPortNumbers port;
    InetAddress ip;
    String info;
    String appName;
    String version;

    public ServiceInfo(InetAddress ip, DefaultServerPortNumbers port) {
        this.ip = ip;
        this.port = port;
    }
}
