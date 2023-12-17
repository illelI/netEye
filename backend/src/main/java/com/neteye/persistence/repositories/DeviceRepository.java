package com.neteye.persistence.repositories;

import com.neteye.persistence.entities.Device;
import org.springframework.data.cassandra.repository.CassandraRepository;

import java.util.List;

public interface DeviceRepository extends CassandraRepository<Device, String> {
    List<Device> findByIp(String ip);
}
