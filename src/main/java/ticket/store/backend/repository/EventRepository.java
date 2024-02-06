package ticket.store.backend.repository;

import ticket.store.backend.entity.event.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface EventRepository extends JpaRepository<Event, Long>, JpaSpecificationExecutor<Event> {

    boolean existsByName(String name);

    boolean existsByUuid(UUID uuid);

    List<CitiesView> findDistinctByOrderByCity();

    Optional<Event> findByUuid(UUID uuid);

    List<Event> findAllByUuidIn(List<UUID> uuids);
}
