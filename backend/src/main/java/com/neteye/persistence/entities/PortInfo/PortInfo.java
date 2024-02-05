package com.neteye.persistence.entities.PortInfo;

import com.neteye.persistence.entities.Device;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.hibernate.Length;

@Data
@AllArgsConstructor
@Entity
@Table(name = "portinfo")
public class PortInfo {
    @EmbeddedId
    private PortInfoPrimaryKey primaryKey;
    @Column(length = Length.LONG32, columnDefinition = "text")
    private String info;
    private String appName;
    private String appVersion;
    public PortInfo() {

    }
}
