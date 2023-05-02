package com.neteye.persistence.entities;

import jakarta.persistence.*;
import lombok.*;

import javax.validation.constraints.Pattern;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Device")
public class Device {
    @Id
    @Pattern(regexp = "^((25[0-5]|(2[0-4]|1\\d|[1-9]|)\\d)\\.?\\b){4}$")
    @Getter
    @Setter
    private String ip;

    @OneToMany
    private List<Port> openPorts = new ArrayList<>();

    public void addOpenPort(Port port) {
        openPorts.add(port);
    }
}
