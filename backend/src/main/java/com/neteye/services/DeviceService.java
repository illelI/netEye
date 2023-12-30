package com.neteye.services;

import com.neteye.persistence.dto.DeviceDto;
import com.neteye.persistence.entities.Device;
import com.neteye.persistence.entities.PortInfo.PortInfo;
import com.neteye.persistence.repositories.DeviceRepository;
import com.neteye.persistence.repositories.PortInfoRepository;
import com.neteye.utils.exceptions.NotFoundException;
import com.neteye.utils.mappers.PortInfoMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DeviceService {
    private final DeviceRepository deviceRepository;
    private final PortInfoRepository portInfoRepository;

    public DeviceService(DeviceRepository deviceRepository, PortInfoRepository portInfoRepository) {
        this.deviceRepository = deviceRepository;
        this.portInfoRepository = portInfoRepository;
    }

    public DeviceDto findDeviceByIp(String ip) {
        Optional<Device> deviceInfo = deviceRepository.findByIp(ip);

        if (deviceInfo.isEmpty()) {
            throw new NotFoundException("Device with this ip not found");
        }
        System.out.println(ip);
        List<PortInfo> portInfos = portInfoRepository.findPortInfoById(ip);
        DeviceDto deviceDto = new DeviceDto();

        deviceDto.setIp(ip);
        deviceDto.setPortInfo(PortInfoMapper.toDto(portInfos));
        deviceDto.setTypeOfDevice(deviceInfo.get().getTypeOfDevice());
        deviceDto.setHostname(deviceInfo.get().getHostname());
        deviceDto.setLocation(deviceInfo.get().getLocation());
        deviceDto.setOperatingSystem(deviceInfo.get().getSystem());

        return deviceDto;
    }
}
