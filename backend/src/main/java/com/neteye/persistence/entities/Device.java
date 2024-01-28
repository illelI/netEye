package com.neteye.persistence.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.cassandra.core.mapping.Indexed;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import java.util.List;

@Data
@AllArgsConstructor
@Table
public class Device {
    @PrimaryKey
    private String ip;
    @Indexed
    private List<Integer> openedPorts;
    @Indexed
    private String hostname;
    @Indexed
    private String location;
    @Indexed
    private String system;
    @Indexed
    private String typeOfDevice;
}
