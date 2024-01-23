package org.example.sbdcoursework.service;

import org.example.sbdcoursework.dto.event.EventCreationDto;
import org.example.sbdcoursework.dto.event.EventDto;
import org.example.sbdcoursework.entity.event.EventType;

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
