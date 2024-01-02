package org.example.sbdcoursework.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.sbdcoursework.dto.event.EventCreationDTO;
import org.example.sbdcoursework.dto.event.EventDTO;
import org.example.sbdcoursework.entity.event.Event;
import org.example.sbdcoursework.entity.event.EventType;
import org.example.sbdcoursework.exception.InternalEventStorageException;
import org.example.sbdcoursework.exception.InvalidArgumentException;
import org.example.sbdcoursework.exception.NotFoundException;
import org.example.sbdcoursework.mapper.EventMapper;
import org.example.sbdcoursework.repository.*;
import org.example.sbdcoursework.service.EventImageService;
import org.example.sbdcoursework.service.EventService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class EventServiceImpl implements EventService {

    private final Integer EVENT_PAGE_SIZE = 20;

    private final EventRepository eventRepository;
    private final EventMapper eventMapper;
    private final EventImageService eventImageService;
    private final TicketRepository ticketRepository;
    private final UserRepository userRepository;

    @Override
    public UUID create(EventCreationDTO creationDTO) {
        if (eventRepository.existsByName(creationDTO.getName())) {
            throw new InvalidArgumentException("Invalid name: given name is already taken");
        }
        if (!userRepository.existsByUuid(UUID.fromString(creationDTO.getOrganizerId()))) {
            throw new InvalidArgumentException("Invalid organizer id: no such organizers");
        }
        if (creationDTO.getImage().isEmpty()) {
            throw new InvalidArgumentException("Can't store empty file");
        }

        Event eventToSave = new Event(UUID.randomUUID());
        String savedImageFilename = eventImageService.store(
                eventToSave.getUuid(),
                creationDTO.getImage()
        );
        eventMapper.mapEventCreationDTOToEvent(
                creationDTO,
                savedImageFilename,
                eventToSave
        );

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
    public EventDTO getById(UUID eventUuid) {
        Event foundEvent = eventRepository
                .findByUuid(eventUuid)
                .orElseThrow(() -> new NotFoundException("No such events found"));

        return eventMapper.mapEventToEventDTO(
                foundEvent,
                ticketRepository.countSoldTicketsByEventUuid(eventUuid)
        );
    }

    @Override
    public List<String> getAllCities() {
        return eventRepository.findDistinctByOrderByCity()
                .stream()
                .map(CitiesView::getCity)
                .toList();
    }

    @Override
    public List<EventDTO> listAllBy(
            Optional<String> city,
            Optional<LocalDate> date,
            List<EventType> types,
            Long pageNumber,
            Optional<UUID> organizerId
    ) {
        Pageable pageable = PageRequest.of(pageNumber.intValue(), EVENT_PAGE_SIZE);
        Specification<Event> specification = createEventSpecification(
                city, date, types, organizerId
        );

        Page<Event> eventsPage = eventRepository.findAll(specification, pageable);

        Map<UUID, Long> soldTicketsPerEvent = mapSoldTicketsPerEventViewToMap(
                ticketRepository.countSoldTicketsGroupedByEventUuid()
        );

        return eventsPage.stream()
                .map(event -> eventMapper.mapEventToEventDTO(
                        event,
                        soldTicketsPerEvent.get(event.getUuid())
                ))
                .toList();
    }

    private Map<UUID, Long> mapSoldTicketsPerEventViewToMap(List<SoldTicketsPerEventView> view) {
        return view.stream()
                .collect(Collectors.toMap(
                        SoldTicketsPerEventView::getEventUuid,
                        SoldTicketsPerEventView::getSoldTickets
                ));
    }

    private Specification<Event> createEventSpecification(
            Optional<String> city,
            Optional<LocalDate> date,
            List<EventType> types,
            Optional<UUID> organizerId
    ) {
        AtomicReference<Specification<Event>> specification =
                new AtomicReference<>(Specification.where(null));

        city.ifPresent(c -> specification.set(
                specification.get().and(EventSpecifications.hasCity(c))
        ));
        date.ifPresent(d -> specification.set(
                specification.get().and(EventSpecifications.inDate(d))
        ));
        if (!types.isEmpty()) {
            specification.set(
                    specification.get().and(EventSpecifications.hasTypes(types))
            );
        }
        organizerId.ifPresent(o -> specification.set(
                specification.get().and(EventSpecifications.hasOrganizerId(o))
        ));

        return specification.get();
    }
}
