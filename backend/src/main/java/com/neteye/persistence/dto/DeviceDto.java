package com.neteye.persistence.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;

@Getter
@Setter
public class DeviceDto {
    private String ip;
    private HashMap<Integer, String> info;
    public DeviceDto() {
        info = new HashMap<>();
    }
}
