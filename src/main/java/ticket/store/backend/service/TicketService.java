package ticket.store.backend.service;

import ticket.store.backend.dto.ticket.TicketCreationDto;
import ticket.store.backend.dto.ticket.TicketDto;

import java.util.UUID;

public interface TicketService {

    UUID create(TicketCreationDto creationDTO);

    TicketDto getById(UUID ticketUuid);
}
