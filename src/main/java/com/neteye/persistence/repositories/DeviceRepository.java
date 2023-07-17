package com.neteye.persistence.repositories;

import com.neteye.persistence.entities.Device;
import org.springframework.data.cassandra.repository.CassandraRepository;

public interface DeviceRepository extends CassandraRepository<Device, String> {
    Device findByIp(String ip);
}
