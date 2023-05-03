package com.neteye.utils.enums;

import com.neteye.utils.support.IpAddress;

public enum RESTRICTEDADDRESSES {
    POOL1(new IpAddress(10,0,0,0), new IpAddress(10,255,255,255));

    private IpAddress startingAddress, endingAddress;
    RESTRICTEDADDRESSES(IpAddress startingAddress, IpAddress endingAddress) {
        this.startingAddress = startingAddress;
        this.endingAddress = endingAddress;
    }

    public IpAddress getStartingAddress() {
        return startingAddress;
    }

    public IpAddress getEndingAddress() {
        return endingAddress;
    }

}
