package com.neteye.services;

import com.neteye.persistence.dto.DeviceDto;
import com.neteye.persistence.entities.Device;
import com.neteye.persistence.entities.PortInfo.PortInfo;
import com.neteye.persistence.repositories.DeviceRepository;
import com.neteye.persistence.repositories.DeviceSearchRepository;
import com.neteye.persistence.repositories.PortInfoRepository;
import com.neteye.utils.exceptions.NotFoundException;
import com.neteye.utils.mappers.PortInfoMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class DeviceService {
    private final DeviceRepository deviceRepository;
    private final PortInfoRepository portInfoRepository;
    private final DeviceSearchRepository deviceSearchRepository;

    public DeviceService(DeviceRepository deviceRepository, PortInfoRepository portInfoRepository, DeviceSearchRepository deviceSearchRepository) {
        this.deviceRepository = deviceRepository;
        this.portInfoRepository = portInfoRepository;
        this.deviceSearchRepository = deviceSearchRepository;
    }

    public DeviceDto findDeviceByIp(String ip) {
        Optional<Device> deviceInfo = deviceRepository.findByIp(ip);

        if (deviceInfo.isEmpty()) {
            throw new NotFoundException("Device with this ip not found");
        }
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

    public Page<Device> searchDevices(String criteria, Pageable pageable) {
        Map<String, String> criteriaMap = new HashMap<>();
        String[] criteriaSplit = criteria.split(" ");
        for (String s : criteriaSplit) {
            String[] keyValue = s.split(":");
            if (keyValue.length == 2) {
                criteriaMap.put(keyValue[0], keyValue[1]);
            }
        }
        return deviceSearchRepository.findDevicesByRequestedCriteria(criteriaMap, pageable);
    }
}
