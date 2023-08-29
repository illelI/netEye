package com.neteye.persistence.repositories;

import com.neteye.persistence.entities.User;
import org.springframework.data.cassandra.repository.CassandraRepository;

import java.util.Optional;

public interface UserRepository extends CassandraRepository<User, Long> {
    Optional<User> findByEmail(String email);

}
