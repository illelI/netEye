package com.neteye.persistence.entities.PortInfo;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
@Embeddable
public class PortInfoPrimaryKey implements Serializable {
    private String ip;
    private int port;

    public PortInfoPrimaryKey() {

    }
}
