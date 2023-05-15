package com.neteye.utils;

import com.neteye.utils.IpAddress;
import com.neteye.utils.enums.RestrictedAddressesEnum;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicIntegerArray;

public class IpChange {
    public static void getOutOfRestrictedAddressesPool(AtomicIntegerArray ip) {
        IpAddress currentIp = new IpAddress(ip);
        for(RestrictedAddressesEnum address : RestrictedAddressesEnum.values()) {
            if(currentIp.ipInRange(address.getStartingAddress(),address.getEndingAddress())) {
                currentIp = address.getEndingAddress().increment();
                ip = currentIp.getAtomicIntegerArrayIP();
            }
        }
    }
}
