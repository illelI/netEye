package com.neteye.persistence.entities.PortInfo;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.Length;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "portinfo")
public class PortInfo {
    @EmbeddedId
    private PortInfoPrimaryKey primaryKey;

    @Column(length = Length.LONG32, columnDefinition = "text")
    private String info;
    private String appName;
    private String appVersion;
}
