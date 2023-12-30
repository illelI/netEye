package com.neteye.persistence.entities.PortInfo;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

@Data
@AllArgsConstructor
@Table
public class PortInfo {
    @PrimaryKey
    private PortInfoPrimaryKey primaryKey;
    private String info;
    private String appName;
    private String appVersion;
}
