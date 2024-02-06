package ticket.store.backend.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ticket.store.backend.dto.ticket.TicketCreationDto;
import ticket.store.backend.dto.ticket.TicketDto;
import ticket.store.backend.entity.event.Event;
import ticket.store.backend.entity.Ticket;
import ticket.store.backend.exception.external.InvalidArgumentException;
import ticket.store.backend.exception.external.NotFoundException;
import ticket.store.backend.exception.internal.InternalTicketStorageException;
import ticket.store.backend.mapper.TicketMapper;
import ticket.store.backend.repository.EventRepository;
import ticket.store.backend.repository.TicketRepository;
import ticket.store.backend.repository.UserRepository;
import ticket.store.backend.service.TicketService;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

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
    public UUID create(TicketCreationDto creationDTO) {
        if (!eventRepository.existsByUuid(UUID.fromString(creationDTO.getEventUuid()))) {
            throw  new InvalidArgumentException("Invalid event uuid: no events with such uuid");
        }
        AtomicReference<UUID> ownerUuid = new AtomicReference<>(null);
        userRepository.findByEmail(creationDTO.getUserEmail()).ifPresent(user -> ownerUuid.set(user.getUuid()));

        Ticket savedTicket = ticketRepository.save(ticketMapper.mapTicketCreationDTOToTicket(creationDTO, ownerUuid.get()));
        log.info("Ticket with uuid: [" + savedTicket.getUuid() + "] created");
        return savedTicket.getUuid();
    }

    @Override
    public TicketDto getById(UUID ticketUuid) {
        Ticket fetchedTicket = ticketRepository.findByUuid(ticketUuid)
                .orElseThrow(() -> new NotFoundException("No such tickets found"));
        Event ticketEvent = eventRepository.findByUuid(fetchedTicket.getEventUuid())
                .orElseThrow(() -> new InternalTicketStorageException("ticket with uuid: [" + fetchedTicket.getUuid() + "] related to non existing event with uuid: [" + fetchedTicket.getEventUuid() + "]"));

        return ticketMapper.mapTicketAndEventToTicketDTO(fetchedTicket, ticketEvent);
    }
}
