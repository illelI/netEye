package com.neteye.persistence.repositories;

import com.neteye.persistence.entities.Device;
import org.springframework.data.cassandra.repository.CassandraRepository;

import java.util.Optional;

public interface DeviceRepository extends CassandraRepository<Device, String> {
    Optional<Device> findByIp(String ip);
}
