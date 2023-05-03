package com.neteye.utils.support;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class IpAddress {
    private final int firstOctet;
    private final int secondOctet;
    private final int thirdOctet;
    private final int fourthOctet;

    public IpAddress(int first, int second, int third, int fourth) {
        this.firstOctet = first;
        this.secondOctet = second;
        this.thirdOctet = third;
        this.fourthOctet = fourth;
    }

    @Override
    public String toString() {
        return new StringBuilder().append(firstOctet)
                .append(".").append(secondOctet).append(".").append(thirdOctet)
                .append(".").append(fourthOctet).toString();
    }

    @Override
    public boolean equals(Object obj) {
        try {
            IpAddress tmp = (IpAddress) obj;
            if(this.firstOctet != tmp.firstOctet) return false;
            if(this.secondOctet != tmp.secondOctet) return false;
            if(this.thirdOctet != tmp.thirdOctet) return false;
            return this.fourthOctet == tmp.fourthOctet;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean isGreater(IpAddress ip) {
        if(this.firstOctet > ip.firstOctet) {
            return true;
        }
        else if (this.firstOctet == ip.firstOctet && this.secondOctet > ip.secondOctet) {
            return true;
        }
        else if (this.firstOctet == ip.firstOctet && this.secondOctet == ip.secondOctet && this.thirdOctet > ip.thirdOctet) {
            return true;
        } else return this.firstOctet == ip.firstOctet && this.secondOctet == ip.secondOctet && this.thirdOctet == ip.thirdOctet && this.fourthOctet > ip.fourthOctet;
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

}
