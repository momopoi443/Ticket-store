package org.example.sbdcoursework.service;

import org.example.sbdcoursework.dto.event.EventCreationDTO;
import org.example.sbdcoursework.dto.event.EventDTO;
import org.example.sbdcoursework.entity.event.EventType;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface EventService {

    UUID create(EventCreationDTO creationDTO);

    EventDTO getById(UUID eventUuid);

    List<String> getAllCities();

    List<EventDTO> listAllBy(
            Optional<String> city,
            Optional<LocalDate> date,
            List<EventType> types,
            Long pageNumber,
            Optional<UUID> organizerId
    );
}
