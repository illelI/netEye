package com.neteye.components;

import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicInteger;

@Component
public class DeviceSearcher {

    public void search(String... range) {
        AtomicInteger[] ip = new AtomicInteger[4];
        ip[0].set(1);
        ip[1].set(0);
        ip[2].set(0);
        ip[3].set(0);
        String lastAddress = "224.0.0.0";
        if(range.length > 0) {
            String[] startingIp = range[0].split("\\.");
            lastAddress = range[1];
            int tmp = 0;
            try {
                for(String octet : startingIp) {
                    ip[tmp++].set(Integer.parseInt(octet));
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
