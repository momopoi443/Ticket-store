package org.example.sbdcoursework.mapper;

import org.example.sbdcoursework.dto.ticket.TicketCreationDTO;
import org.example.sbdcoursework.dto.ticket.TicketDTO;
import org.example.sbdcoursework.entity.event.Event;
import org.example.sbdcoursework.entity.Ticket;

public interface TicketMapper {

    void mapTicketCreationDTOToTicket(
            TicketCreationDTO creationDTO,
            Ticket ticket
    );

    TicketDTO mapTicketAndEventToTicketDTO(
            Ticket ticket,
            Event event
    );
}
