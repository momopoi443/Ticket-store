package ticket.store.backend.mapper;

import ticket.store.backend.dto.ticket.TicketCreationDto;
import ticket.store.backend.dto.ticket.TicketDto;
import ticket.store.backend.entity.event.Event;
import ticket.store.backend.entity.Ticket;

import java.util.UUID;

public interface TicketMapper {

    Ticket mapTicketCreationDTOToTicket(TicketCreationDto creationDTO, UUID ownerUuid);

    TicketDto mapTicketAndEventToTicketDTO(Ticket ticket, Event event);
}
