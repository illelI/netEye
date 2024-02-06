package com.neteye.persistence.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class DeviceDto {
    private String ip;
    private String hostname;
    private String location;
    private String system;
    private String typeOfDevice;
    private List<PortInfoDto> portInfo;
    public DeviceDto() {
        portInfo = new ArrayList<>();
    }
}
