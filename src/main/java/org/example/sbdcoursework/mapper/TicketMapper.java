package org.example.sbdcoursework.mapper;

import org.example.sbdcoursework.dto.ticket.TicketCreationDto;
import org.example.sbdcoursework.dto.ticket.TicketDto;
import org.example.sbdcoursework.entity.event.Event;
import org.example.sbdcoursework.entity.Ticket;

import java.util.UUID;

public interface TicketMapper {

    Ticket mapTicketCreationDTOToTicket(TicketCreationDto creationDTO, UUID ownerUuid);

    TicketDto mapTicketAndEventToTicketDTO(Ticket ticket, Event event);
}
