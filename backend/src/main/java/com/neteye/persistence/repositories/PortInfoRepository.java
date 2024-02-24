package com.neteye.persistence.repositories;

import com.neteye.persistence.entities.PortInfo.PortInfo;
import com.neteye.persistence.entities.PortInfo.PortInfoPrimaryKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PortInfoRepository extends JpaRepository<PortInfo, PortInfoPrimaryKey> {
    List<PortInfo> findAllByPrimaryKeyIp(String ip);
}
