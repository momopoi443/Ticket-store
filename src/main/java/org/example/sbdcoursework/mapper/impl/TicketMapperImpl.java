package org.example.sbdcoursework.mapper.impl;

import org.example.sbdcoursework.dto.ticket.TicketCreationDTO;
import org.example.sbdcoursework.dto.ticket.TicketDTO;
import org.example.sbdcoursework.entity.event.Event;
import org.example.sbdcoursework.entity.Ticket;
import org.example.sbdcoursework.mapper.TicketMapper;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.UUID;

@Component
public class TicketMapperImpl implements TicketMapper {

    @Override
    public void mapTicketCreationDTOToTicket(TicketCreationDTO creationDTO, Ticket ticket) {
        ticket.setUserFirstName(creationDTO.getUserFirstName());
        ticket.setUserLastName(creationDTO.getUserLastName());
        ticket.setUserEmail(creationDTO.getUserEmail());
        ticket.setEventUuid(
                UUID.fromString(creationDTO.getEventUuid())
        );
        ticket.setCreatedAt(LocalDateTime.now().withSecond(0).withNano(0));
    }

    @Override
    public TicketDTO mapTicketAndEventToTicketDTO(Ticket ticket, Event event) {
        return new TicketDTO(
                ticket.getUuid(),
                ticket.getUserUuid(),
                ticket.getUserFirstName(),
                ticket.getUserLastName(),
                ticket.getUserEmail(),
                ticket.getEventUuid(),
                event.getDate(),
                event.getCity(),
                event.getCityAddress(),
                event.getImageName(),
                ticket.getCreatedAt()
        );
    }
}
