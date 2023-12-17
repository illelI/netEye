package com.neteye.services;

import com.neteye.persistence.dto.DeviceDto;
import com.neteye.persistence.entities.Device;
import com.neteye.persistence.repositories.DeviceRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DeviceService {
    private final DeviceRepository deviceRepository;

    public DeviceService(DeviceRepository deviceRepository) {
        this.deviceRepository = deviceRepository;
    }

    public DeviceDto findDeviceByIp(String ip) {
        List<Device> deviceInfo = deviceRepository.findByIp(ip);
        DeviceDto deviceDto = new DeviceDto();
        deviceDto.setIp(ip);
        for (Device d : deviceInfo) {
            deviceDto.getInfo().put(d.getPortNumber(), d.getInfo());
        }
        return deviceDto;
    }
}
