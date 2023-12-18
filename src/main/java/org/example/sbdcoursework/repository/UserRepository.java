package org.example.sbdcoursework.repository;

import org.example.sbdcoursework.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByUuid(UUID uuid);

    boolean existsByEmail(String email);

    Optional<User> findByUuid(UUID uuid);

    Optional<User> findByEmail(String email);

    void deleteByUuid(UUID uuid);
}
