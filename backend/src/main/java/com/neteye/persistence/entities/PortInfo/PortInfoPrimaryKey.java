package com.neteye.persistence.entities.PortInfo;

import jakarta.persistence.Embeddable;
import jakarta.persistence.ForeignKey;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class PortInfoPrimaryKey implements Serializable {
    @Size(max = 15)
    private String ip;
    private int port;
}
