package com.smartclassroom.backend.repository;

import com.smartclassroom.backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    java.util.Optional<User> findByUsername(String username);
}
