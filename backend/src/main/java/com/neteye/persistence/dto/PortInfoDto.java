package com.neteye.persistence.dto;

import lombok.Data;

@Data
public class PortInfoDto {
    private int port;
    private String info;
    private String appName;
    private String appVersion;
}
