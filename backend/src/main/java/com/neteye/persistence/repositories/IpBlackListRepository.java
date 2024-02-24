package com.neteye.persistence.repositories;

import com.neteye.persistence.entities.IpBlackList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IpBlackListRepository extends JpaRepository<IpBlackList, String> {
    List<IpBlackList> findAll();
}
