package org.example.sbdcoursework.service;

import org.example.sbdcoursework.dto.ticket.TicketCreationDTO;
import org.example.sbdcoursework.dto.ticket.TicketDTO;

import java.util.List;
import java.util.UUID;

public interface TicketService {

    UUID create(TicketCreationDTO creationDTO);

    TicketDTO getById(UUID ticketUuid);

    List<TicketDTO> listByOwner(UUID ownerUuid);
}
