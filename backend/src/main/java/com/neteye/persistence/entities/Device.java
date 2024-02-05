package com.neteye.persistence.entities;

import com.neteye.persistence.entities.PortInfo.PortInfo;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
@Entity
@Table(name = "device")
public class Device {
    @Id
    private String ip;
    @OneToMany(cascade = CascadeType.ALL)
    private List<PortInfo> openedPorts;
    private String hostname;
    private String location;
    private String system;
    private String typeOfDevice;

    public Device() {

    }
}
