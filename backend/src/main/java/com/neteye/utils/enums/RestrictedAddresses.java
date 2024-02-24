package com.neteye.utils.enums;

import com.neteye.utils.misc.IpAddress;

public enum RestrictedAddresses {
    POOL1(new IpAddress("0.0.0.0"), new IpAddress("0.255.255.255")),
    POOL2(new IpAddress("10.0.0.0"), new IpAddress("10.255.255.255")),
    POOL3(new IpAddress("100.64.0.0"), new IpAddress("100.127.255.255")),
    POOL4(new IpAddress("127.0.0.0"), new IpAddress("127.255.255.255")),
    POOL5(new IpAddress("169.254.0.0"), new IpAddress("169.254.255.255")),
    POOL6(new IpAddress("172.16.0.0"), new IpAddress("172.31.255.255")),
    POOL7(new IpAddress("192.0.0.0"), new IpAddress("192.0.0.255")),
    POOL8(new IpAddress("192.0.2.0"), new IpAddress("192.0.2.255")),
    POOL9(new IpAddress("192.88.99.0"), new IpAddress("192.88.99.255")),
    POOL10(new IpAddress("192.168.0.0"), new IpAddress("192.168.255.255")),
    POOL11(new IpAddress("198.18.0.0"), new IpAddress("198.19.255.255")),
    POOL12(new IpAddress("198.51.100.0"), new IpAddress("198.51.100.255")),
    POOL13(new IpAddress("224.0.0.0"), new IpAddress("255.255.255.255"));

    private IpAddress firstAddress;
    private IpAddress lastAddress;

    RestrictedAddresses(IpAddress firstAddress, IpAddress lastAddress) {
        this.firstAddress = firstAddress;
        this.lastAddress = lastAddress;
    }

    public IpAddress getFirstAddress() {
        return firstAddress;
    }

    public IpAddress getLastAddress() {
        return lastAddress;
    }
}
