package org.example.sbdcoursework.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.sbdcoursework.dto.event.EventCreationDTO;
import org.example.sbdcoursework.dto.event.EventDTO;
import org.example.sbdcoursework.entity.event.Event;
import org.example.sbdcoursework.entity.event.EventType;
import org.example.sbdcoursework.exception.InvalidArgumentException;
import org.example.sbdcoursework.exception.NotFoundException;
import org.example.sbdcoursework.mapper.EventMapper;
import org.example.sbdcoursework.repository.*;
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
    private final TicketRepository ticketRepository;
    private final UserRepository userRepository;

    @Override
    public UUID create(EventCreationDTO creationDTO) {
        if (eventRepository.existsByName(creationDTO.getName())) {
            logAndThrowException(
                    new InvalidArgumentException(
                            "Invalid name: given name is already taken"
                    )
            );
        }
        if (!userRepository.existsByUuid(
                UUID.fromString(creationDTO.getOrganizerId())
        )) {
            logAndThrowException(
                    new InvalidArgumentException(
                            "Invalid organizer id: no such organizers"
                    )
            );
        }

        Event eventToSave = new Event(UUID.randomUUID());
        eventMapper.mapEventCreationDTOToEvent(
                creationDTO,
                eventToSave
        );

        Event savedEvent = eventRepository.save(eventToSave);
        log.info("Event: " + savedEvent.getUuid() + " created");

        return savedEvent.getUuid();
    }

    @Override
    public EventDTO getById(UUID eventUuid) {
        AtomicReference<EventDTO> eventDTO = new AtomicReference<>(new EventDTO());

        eventRepository.findByUuid(eventUuid).ifPresentOrElse(
                foundEvent -> eventDTO.set(
                        eventMapper.mapEventToEventDTO(
                                foundEvent,
                                ticketRepository.countSoldTicketsByEventUuid(eventUuid)
                        )
                ),
                () -> logAndThrowException(
                        new NotFoundException("No such events found")
                )
        );

        return eventDTO.get();
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

    private void logAndThrowException(RuntimeException exception) {
        log.error(exception.getMessage(), exception);
        throw exception;
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
