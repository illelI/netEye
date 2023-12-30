package com.neteye.persistence.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import java.util.List;

@Data
@AllArgsConstructor
@Table
public class Device {
    @PrimaryKey
    private String ip;
    private List<Integer> openedPorts;
    private String hostname;
    private String location;
    private String system;
    private String typeOfDevice;
}
