package com.neteye.utils.mappers;

import com.neteye.persistence.dto.DeviceDto;
import com.neteye.persistence.entities.Device;

public class DeviceMapper {
    public static DeviceDto toDto(Device device) {
        DeviceDto dto = new DeviceDto();
        dto.setIp(device.getIp());
        dto.setPortInfo(PortInfoMapper.toDto(device.getOpenedPorts()));
        dto.setHostname(device.getHostname());
        dto.setLocation(device.getLocation());
        dto.setOperatingSystem(device.getSystem());
        dto.setTypeOfDevice(device.getTypeOfDevice());
        return dto;
    }
}
