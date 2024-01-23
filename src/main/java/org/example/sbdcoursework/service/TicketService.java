package org.example.sbdcoursework.service;

import org.example.sbdcoursework.dto.ticket.TicketCreationDto;
import org.example.sbdcoursework.dto.ticket.TicketDto;

import java.util.UUID;

public interface TicketService {

    UUID create(TicketCreationDto creationDTO);

    TicketDto getById(UUID ticketUuid);
}
