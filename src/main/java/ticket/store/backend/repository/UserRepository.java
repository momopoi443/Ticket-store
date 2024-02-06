package ticket.store.backend.repository;

import ticket.store.backend.entity.user.User;
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
