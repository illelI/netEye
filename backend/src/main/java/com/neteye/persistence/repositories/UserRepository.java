package com.neteye.persistence.repositories;

import com.neteye.persistence.entities.User;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends CassandraRepository<User, Long> {
    Optional<User> findByEmail(String email);

}
