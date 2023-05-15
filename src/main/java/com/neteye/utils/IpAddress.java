package com.neteye.utils;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.atomic.AtomicIntegerArray;

public class IpAddress {
    private int[] address = new int[4];
    public IpAddress(int first, int second, int third, int fourth) {
        this.address[0] = first;
        this.address[1] = second;
        this.address[2] = third;
        this.address[3] = fourth;
    }
    public IpAddress() {
    }

    public IpAddress(String ip) {
        String[] ipSplitted = ip.split("\\.");
        this.address[0] = Integer.parseInt(ipSplitted[0]);
        this.address[1] = Integer.parseInt(ipSplitted[1]);
        this.address[2] = Integer.parseInt(ipSplitted[2]);
        this.address[3] = Integer.parseInt(ipSplitted[3]);
    }

    public IpAddress(AtomicIntegerArray ip) {
        this.address[0] = ip.get(0);
        this.address[1] = ip.get(1);
        this.address[2] = ip.get(2);
        this.address[3] = ip.get(3);
    }

    public void setAddress(AtomicIntegerArray ip) {
        this.address[0] = ip.get(0);
        this.address[1] = ip.get(1);
        this.address[2] = ip.get(2);
        this.address[3] = ip.get(3);
    }

    @Override
    public String toString() {
        return new StringBuilder().append(address[0])
                .append(".").append(address[1]).append(".").append(address[2])
                .append(".").append(address[3]).toString();
    }

    @Override
    public boolean equals(Object obj) {
        try {
            IpAddress tmp = (IpAddress) obj;
            if(this.address[0] != tmp.address[0]) return false;
            if(this.address[1] != tmp.address[1]) return false;
            if(this.address[2] != tmp.address[2]) return false;
            return this.address[4] == tmp.address[4];
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean isGreater(IpAddress ip) {
        if(this.address[0] > ip.address[0]) {
            return true;
        }
        else if (this.address[0] == ip.address[0] && this.address[1] > ip.address[1]) {
            return true;
        }
        else if (this.address[0] == ip.address[0] && this.address[1] == ip.address[1] && this.address[2] > ip.address[2]) {
            return true;
        } else return this.address[0] == ip.address[0] && this.address[1] == ip.address[1] && this.address[2] == ip.address[2] && this.address[3] > ip.address[3];
    }

    public boolean isLesser(IpAddress ip) {
        return !(equals(ip) || isGreater(ip));
    }

    public InetAddress getIP() {
        try {
            return InetAddress.getByName(this.toString());
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
    }

    public AtomicIntegerArray getAtomicIntegerArrayIP() {
        AtomicIntegerArray tmp = new AtomicIntegerArray(4);
        tmp.set(0, this.address[0]);
        tmp.set(1, this.address[1]);
        tmp.set(2, this.address[2]);
        tmp.set(3, this.address[3]);
        return tmp;
    }

    public boolean ipInRange(IpAddress start, IpAddress end) {
        return (this.isGreater(start) || this.equals(start)) && (this.isLesser(end) || this.equals(end));
    }

    public IpAddress increment() {
        if(++address[3] > 255) {
            address[3] = 0;
            if (++address[2] > 255) {
                address[2] = 0;
                if (++address[1] > 255) {
                    address[1] = 0;
                    ++address[0];
                }
            }
        }
        return this;
    }

}
