package ticket.store.backend.service;

import ticket.store.backend.dto.event.EventCreationDto;
import ticket.store.backend.dto.event.EventDto;
import ticket.store.backend.entity.event.EventType;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface EventService {

    UUID create(EventCreationDto creationDTO);

    EventDto getById(UUID eventUuid);

    List<String> getAllCities();

    List<EventDto> listAllBy(
            Optional<String> city,
            Optional<LocalDate> date,
            List<EventType> types,
            Long pageNumber,
            Optional<UUID> organizerId,
            Boolean isConfirmed
    );

    void confirm(UUID eventUuid);
}
