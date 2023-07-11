package com.neteye.persistence.repositories;

import com.neteye.persistence.entities.Device;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeviceRepository extends CassandraRepository<Device, String> {
    Device findByIp(String ip);
}
