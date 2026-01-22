package com.microservices.user.repository;

import com.microservices.user.model.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserProfile, Long> {
    Optional<UserProfile> findByUsername(String username);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
}
