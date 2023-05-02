package com.neteye.persistence.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Port")
public class Port {
    @Id
    @GeneratedValue
    private Long id;

    @Getter
    @Setter
    private int portNumber;

    @Getter
    @Setter
    private String info;

}
