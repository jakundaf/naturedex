package com.naturedex.auth_service.repository;

import com.naturedex.auth_service.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface AuthRepository extends JpaRepository <User, UUID> {

    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);

}
