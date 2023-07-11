package com.neteye.persistence.repositories;

import com.neteye.persistence.entities.User;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends CassandraRepository<User, Long> {
}
