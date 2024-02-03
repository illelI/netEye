package com.neteye.persistence.entities.PortInfo;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.cassandra.core.mapping.Indexed;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

@Data
@AllArgsConstructor
@Table
public class PortInfo {
    @PrimaryKey
    private PortInfoPrimaryKey primaryKey;
    @Indexed
    private String info;
    @Indexed
    private String appName;
    @Indexed
    private String appVersion;
}
