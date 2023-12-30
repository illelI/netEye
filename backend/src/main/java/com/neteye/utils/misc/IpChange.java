package com.neteye.utils.misc;

import com.neteye.utils.enums.RestrictedAddressesEnum;

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
