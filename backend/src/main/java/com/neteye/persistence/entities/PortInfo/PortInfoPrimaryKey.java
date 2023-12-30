package com.neteye.persistence.entities.PortInfo;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyClass;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;

@Data
@AllArgsConstructor
@PrimaryKeyClass
public class PortInfoPrimaryKey {
    @PrimaryKeyColumn(name = "ip", type = PrimaryKeyType.PARTITIONED)
    private String ip;
    @PrimaryKeyColumn(name = "port", type = PrimaryKeyType.CLUSTERED)
    private int port;
}
