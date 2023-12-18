package org.example.sbdcoursework.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.sbdcoursework.dto.ticket.TicketCreationDTO;
import org.example.sbdcoursework.dto.ticket.TicketDTO;
import org.example.sbdcoursework.entity.event.Event;
import org.example.sbdcoursework.entity.Ticket;
import org.example.sbdcoursework.exception.InvalidArgumentException;
import org.example.sbdcoursework.exception.NotFoundException;
import org.example.sbdcoursework.mapper.TicketMapper;
import org.example.sbdcoursework.repository.EventRepository;
import org.example.sbdcoursework.repository.TicketRepository;
import org.example.sbdcoursework.repository.UserRepository;
import org.example.sbdcoursework.service.TicketService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class TicketServiceImpl implements TicketService {

    private final TicketRepository ticketRepository;
    private final TicketMapper ticketMapper;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;

    @Override
    public UUID create(TicketCreationDTO creationDTO) {
        if (!eventRepository.existsByUuid(
                UUID.fromString(creationDTO.getEventUuid())
        )) {
            logAndThrowException(
                    new InvalidArgumentException("Invalid event uuid: no events with such uuid")
            );
        }

        Ticket ticketToSave = new Ticket(UUID.randomUUID());

        userRepository.findByEmail(creationDTO.getUserEmail()).ifPresent(
                //TODO check that supplied first and last name equals found
                user -> ticketToSave.setUserUuid(user.getUuid())
        );
        ticketMapper.mapTicketCreationDTOToTicket(
                creationDTO, ticketToSave
        );

        Ticket savedTicket = ticketRepository.save(ticketToSave);
        log.info("Ticket: " + savedTicket.getUuid() + " created");

        return savedTicket.getUuid();
    }

    @Override
    public TicketDTO getById(UUID ticketUuid) {
        AtomicReference<TicketDTO> ticketToRetrieve = new AtomicReference<>();

        ticketRepository.findByUuid(ticketUuid).ifPresentOrElse(
                ticket -> {
                    //TODO check for existence of event
                    Event event = eventRepository.findByUuid(ticket.getEventUuid()).get();
                    ticketToRetrieve.set(
                            ticketMapper.mapTicketAndEventToTicketDTO(ticket, event)
                    );
                },
                () -> logAndThrowException(new NotFoundException("No such tickets found"))
        );

        return ticketToRetrieve.get();
    }

    @Override
    public List<TicketDTO> listByOwner(UUID ownerUuid) {
        List<Ticket> ownerTickets = ticketRepository.findAllByUserUuid(ownerUuid);
        Map<UUID, Event> ownerTicketsEvents = eventRepository.findAllByUuidIn(
                ownerTickets.stream()
                        .map(Ticket::getEventUuid)
                        .distinct()
                        .toList()
        ).stream().collect(
                Collectors.toMap(
                        Event::getUuid,
                        event -> event
                )
        );

        return ownerTickets.stream()
                .map(ticket -> {
                    Event event = ownerTicketsEvents.get(ticket.getEventUuid());
                    return ticketMapper.mapTicketAndEventToTicketDTO(ticket, event);
                })
                .toList();
    }

    private void logAndThrowException(RuntimeException exception) {
        log.error(exception.getMessage(), exception);
        throw exception;
    }
}
