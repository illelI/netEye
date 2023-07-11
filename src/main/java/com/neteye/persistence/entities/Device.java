package com.neteye.persistence.entities;

import com.datastax.oss.driver.api.core.uuid.Uuids;
import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import org.springframework.data.cassandra.core.mapping.Indexed;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@Table
public class Device {
    @PrimaryKey
    private UUID id;

    @Indexed
    @Getter
    @Setter
    private String ip;

    @Getter
    @Setter
    private int portNumber;

    @Getter
    @Setter
    private String info;

    public Device() {
        this.id = Uuids.timeBased();
    }


    public Device(String ip, int portNumber, String info) {
        this.id = Uuids.timeBased();
        this.ip = ip;
        this.portNumber = portNumber;
        this.info = info;
    }
}
