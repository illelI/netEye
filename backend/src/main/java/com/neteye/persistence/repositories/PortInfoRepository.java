package com.neteye.persistence.repositories;

import com.neteye.persistence.entities.PortInfo.PortInfo;
import com.neteye.persistence.entities.PortInfo.PortInfoPrimaryKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PortInfoRepository extends JpaRepository<PortInfo, PortInfoPrimaryKey> {

}
