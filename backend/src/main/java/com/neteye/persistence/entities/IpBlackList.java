package com.neteye.persistence.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Entity
public class IpBlackList {
    @Id
    @Size(max = 15)
    @Getter
    String ip;

    public IpBlackList(String ip) {
        this.ip = ip;
    }

    public IpBlackList() {

    }
}
