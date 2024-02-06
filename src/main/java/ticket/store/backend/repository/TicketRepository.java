package ticket.store.backend.repository;

import ticket.store.backend.entity.Ticket;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TicketRepository extends MongoRepository<Ticket, String> {

    Optional<Ticket> findByUuid(UUID uuid);

    List<Ticket> findAllByUserUuid(UUID userUuid);

    long countSoldTicketsByEventUuid(UUID eventUuid);

    @Aggregation(pipeline = {
            "{$group: { _id: '$eventUuid', soldTickets: { $sum: 1 } }}",
            "{$project: { _id: 0, eventUuid: '$_id', soldTickets: 1 }}"
    })
    List<SoldTicketsPerEventView> countSoldTicketsGroupedByEventUuid();
}
