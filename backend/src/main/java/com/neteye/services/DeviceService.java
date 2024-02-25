package com.neteye.services;

import com.neteye.persistence.dto.DeviceDto;
import com.neteye.persistence.entities.Device;
import com.neteye.persistence.entities.IpBlackList;
import com.neteye.persistence.repositories.DeviceRepository;
import com.neteye.persistence.repositories.DeviceSearchRepository;
import com.neteye.persistence.repositories.IpBlackListRepository;
import com.neteye.persistence.repositories.PortInfoRepository;
import com.neteye.utils.exceptions.NotFoundException;
import com.neteye.utils.mappers.DeviceMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class DeviceService {
    private final DeviceRepository deviceRepository;
    private final PortInfoRepository portInfoRepository;
    private final IpBlackListRepository ipBlackListRepository;
    private final DeviceSearchRepository deviceSearchRepository;

    public DeviceService(DeviceRepository deviceRepository, PortInfoRepository portInfoRepository, IpBlackListRepository ipBlackListRepository, DeviceSearchRepository deviceSearchRepository) {
        this.deviceRepository = deviceRepository;
        this.portInfoRepository = portInfoRepository;
        this.ipBlackListRepository = ipBlackListRepository;
        this.deviceSearchRepository = deviceSearchRepository;
    }

    public DeviceDto findDeviceByIp(String ip) {

        Optional<Device> deviceInfo = deviceRepository.findByIp(ip);

        if (deviceInfo.isEmpty()) {
            throw new NotFoundException("Device with this ip not found");
        }

        return DeviceMapper.toDto(deviceInfo.get());
    }

    public Page<Device> searchDevices(String criteria, Pageable pageable) {
        Map<String, String> criteriaMap = new HashMap<>();
        String[] criteriaSplit = criteria.split(" ");
        for (String s : criteriaSplit) {
            String[] keyValue = s.split(":");
            if (keyValue.length == 2) {
                criteriaMap.put(keyValue[0], keyValue[1]);
            } else {
                if (criteriaMap.containsKey("info")) {
                    criteriaMap.put("info", criteriaMap.get("info") + " " + keyValue[0]);
                } else {
                    criteriaMap.put("info", keyValue[0]);
                }
            }
        }
        return deviceSearchRepository.findDevicesByRequestedCriteria(criteriaMap, pageable);
    }

    public void delete(String ip) {
        portInfoRepository.deleteAllByPrimaryKeyIp(ip);
        deviceRepository.deleteById(ip);
    }

    public void addToBlacklist(String ip) {
        IpBlackList ipBlackList = new IpBlackList(ip);
        ipBlackListRepository.save(ipBlackList);
    }

    public void deleteFromBlacklist(String ip) {
        ipBlackListRepository.deleteById(ip);
    }
}
