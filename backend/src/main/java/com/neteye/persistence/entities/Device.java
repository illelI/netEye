package com.neteye.persistence.entities;

import com.neteye.persistence.entities.PortInfo.PortInfo;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "device")
public class Device {
    @Id
    @Size(max = 15)
    private String ip;
    @OneToMany(mappedBy = "primaryKey.ip", cascade = CascadeType.ALL)
    private List<PortInfo> openedPorts;
    private String hostname;
    private String location;
    private String system;
    private String typeOfDevice;
}
