package com.neteye.utils;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.atomic.AtomicIntegerArray;

@Data
@NoArgsConstructor
public class IpAddress {
    private int[] address = new int[4];

    public IpAddress(int first, int second, int third, int fourth) {
        this.address[0] = first;
        this.address[1] = second;
        this.address[2] = third;
        this.address[3] = fourth;
    }

    public IpAddress(int[] address) {
        this.address = address;
    }

    public IpAddress(String ip) {
        String[] ipSplit = ip.split("\\.");
        this.address[0] = Integer.parseInt(ipSplit[0]);
        this.address[1] = Integer.parseInt(ipSplit[1]);
        this.address[2] = Integer.parseInt(ipSplit[2]);
        this.address[3] = Integer.parseInt(ipSplit[3]);
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

    public byte[] getByteAddress() {
        byte[] address = new byte[4];
        address[0] = (byte) this.address[0];
        address[1] = (byte) this.address[1];
        address[2] = (byte) this.address[2];
        address[3] = (byte) this.address[3];
        return address;
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

    public InetAddress getIP() throws UnknownHostException {
        try {
            return InetAddress.getByAddress(this.getByteAddress());
        } catch (UnknownHostException e) {
            throw new UnknownHostException();
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

    public static long calculateHowManyIpsAreInRange(IpAddress first, IpAddress last) {
        //counting how many ip addresses are in range form 0.0.0.0 to ip, then first - last
        long howManyInFirst = (long) ( Math.pow(255, 3) * first.getAddress()[0] +
                Math.pow(255, 2) * first.getAddress()[1] +
                255L * first.getAddress()[2] + first.getAddress()[3]);

        long howManyInLast = (long) ( Math.pow(255, 3) * last.getAddress()[0] +
                Math.pow(255, 2) * last.getAddress()[1] +
                255L * last.getAddress()[2] + last.getAddress()[3]);

        return howManyInLast - howManyInFirst;
    }

    public static IpAddress addToIp(IpAddress address, long number) {
        int[] ipArray = new int[4];

        for (int i = 3; i >= 0; i--) {
            ipArray[i] = (int) (number & 0xFF);
            number >>= 8;
        }

        return addTwoAddresses(address, new IpAddress(new AtomicIntegerArray(ipArray)));
    }

    private static IpAddress addTwoAddresses(IpAddress first, IpAddress second) {
        int[] firstOctets = first.getAddress();
        int[] secondOctets = second.getAddress();
        int[] resultAddress = new int[4];

        resultAddress[3] = firstOctets[3] + secondOctets[3];
        resultAddress[2] = firstOctets[2] + secondOctets[2];
        resultAddress[1] = firstOctets[1] + secondOctets[1];
        resultAddress[0] = firstOctets[0] + secondOctets[0];

        int tmp = resultAddress[3] / 256;
        resultAddress[3] %= 256;
        resultAddress[2] += tmp;
        tmp = resultAddress[2] / 256;
        resultAddress[2] %= 256;
        resultAddress[1] += tmp;
        tmp = resultAddress[1] / 256;
        resultAddress[1] %= 256;
        resultAddress[0] += tmp;

        if (resultAddress[0] > 255) {
            throw new IllegalArgumentException();
        }

        return new IpAddress(resultAddress);
    }

    @Override
    public String toString() {
        return address[0] +
                "." + address[1] + "." + address[2] +
                "." + address[3];
    }
}
