package com.neteye.persistence.repositories;

import com.neteye.persistence.entities.User;
import org.springframework.data.cassandra.repository.CassandraRepository;

public interface UserRepository extends CassandraRepository<User, Long> {
    User findByEmail(String email);
}
