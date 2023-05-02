package com.neteye.persistence.repositories;

import com.neteye.persistence.entities.Device;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeviceRepository extends JpaRepository<Device, String> {
    Device findByIp(String ip);
}
