package org.example.sbdcoursework.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.sbdcoursework.dto.event.EventCreationDto;
import org.example.sbdcoursework.dto.event.EventDto;
import org.example.sbdcoursework.entity.event.Event;
import org.example.sbdcoursework.entity.event.EventType;
import org.example.sbdcoursework.exception.external.InvalidArgumentException;
import org.example.sbdcoursework.exception.external.NotFoundException;
import org.example.sbdcoursework.exception.internal.InternalEventStorageException;
import org.example.sbdcoursework.mapper.EventMapper;
import org.example.sbdcoursework.repository.CitiesView;
import org.example.sbdcoursework.repository.EventRepository;
import org.example.sbdcoursework.repository.EventSpecifications;
import org.example.sbdcoursework.repository.UserRepository;
import org.example.sbdcoursework.service.EventImageService;
import org.example.sbdcoursework.service.EventService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class EventServiceImpl implements EventService {

    private final Integer EVENT_PAGE_SIZE = 20;

    private final EventRepository eventRepository;
    private final EventMapper eventMapper;
    private final EventImageService eventImageService;
    private final UserRepository userRepository;

    @Override
    public UUID create(EventCreationDto creationDTO) {
        if (eventRepository.existsByName(creationDTO.getName())) {
            throw new InvalidArgumentException("Invalid name: given name is already taken");
        }
        if (!userRepository.existsByUuid(UUID.fromString(creationDTO.getOrganizerId()))) {
            throw new InvalidArgumentException("Invalid organizer id: no such organizers");
        }

        Event eventToSave = new Event(UUID.randomUUID());
        String savedImageFilename = eventImageService.store(eventToSave.getUuid(), creationDTO.getImage());
        eventMapper.mapEventCreationDtoToEvent(creationDTO, savedImageFilename, eventToSave);

        Event savedEvent;
        try {
            savedEvent = eventRepository.save(eventToSave);
        } catch (Exception e) {
            eventImageService.delete(savedImageFilename);
            throw new InternalEventStorageException(e);
        }
        log.info("Event: " + savedEvent.getUuid() + " created");
        return savedEvent.getUuid();
    }

    @Override
    public EventDto getById(UUID eventUuid) {
        Event foundEvent = eventRepository.findByUuid(eventUuid)
                .orElseThrow(() -> new NotFoundException("No such events found"));

        return eventMapper.mapEventToEventDto(foundEvent);
    }

    @Override
    public List<String> getAllCities() {
        return eventRepository.findDistinctByOrderByCity().stream()
                .map(CitiesView::getCity)
                .toList();
    }

    @Override
    public List<EventDto> listAllBy(
            Optional<String> city,
            Optional<LocalDate> date,
            List<EventType> types,
            Long pageNumber,
            Optional<UUID> organizerId,
            Boolean isConfirmed
    ) {
        Pageable pageable = PageRequest.of(pageNumber.intValue(), EVENT_PAGE_SIZE);
        AtomicReference<Specification<Event>> specification = new AtomicReference<>(Specification.where(null));

        city.ifPresent(c -> specification.set(specification.get().and(EventSpecifications.hasCity(c))));
        date.ifPresent(d -> specification.set(specification.get().and(EventSpecifications.inDate(d))));
        if (!types.isEmpty()) specification.set(specification.get().and(EventSpecifications.hasTypes(types)));
        organizerId.ifPresent(o -> specification.set(specification.get().and(EventSpecifications.hasOrganizerId(o))));
        specification.set(specification.get().and(EventSpecifications.isConfirmed(isConfirmed)));

        return eventMapper.mapEventsToEventDtos(eventRepository.findAll(specification.get(), pageable).toList());
    }

    @Override
    public void confirm(UUID eventUuid) {
        Event eventToConfirm = eventRepository.findByUuid(eventUuid)
                .orElseThrow(() -> new NotFoundException("No such events found"));
        eventToConfirm.setIsConfirmed(true);

        eventRepository.save(eventToConfirm);
    }
}
