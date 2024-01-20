package com.neteye.persistence.repositories;

import com.neteye.persistence.entities.Device;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DeviceRepository extends CassandraRepository<Device, String> {
    Optional<Device> findByIp(String ip);

}
