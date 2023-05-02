package com.neteye.services;

import com.neteye.persistence.entities.Device;
import com.neteye.persistence.repositories.DeviceRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class DeviceService {
    private final DeviceRepository deviceRepository;

    public DeviceService(DeviceRepository deviceRepository) {
        this.deviceRepository = deviceRepository;
    }

    public Page<Device> getPageOfData(Pageable pageable) {
        return deviceRepository.findAll(pageable);
    }
}
