package com.neteye.persistence.entities;

import com.datastax.oss.driver.api.core.uuid.Uuids;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.cassandra.core.mapping.Indexed;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import java.util.UUID;

@Data
@Table
@Getter
@Setter
public class Device {
    @PrimaryKey
    private UUID id;

    @Indexed
    private String ip;

    private int portNumber;

    private String info;

    public Device(String ip, int portNumber, String info) {
        this.id = Uuids.timeBased();
        this.ip = ip;
        this.portNumber = portNumber;
        this.info = info;
    }
}
