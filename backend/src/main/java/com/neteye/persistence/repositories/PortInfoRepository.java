package com.neteye.persistence.repositories;

import com.neteye.persistence.entities.PortInfo.PortInfo;
import com.neteye.persistence.entities.PortInfo.PortInfoPrimaryKey;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PortInfoRepository extends CassandraRepository<PortInfo, PortInfoPrimaryKey> {
    @Query("""
            SELECT * FROM portinfo WHERE ip = ?0
            """)
    List<PortInfo> findPortInfoById(String ip);
}
