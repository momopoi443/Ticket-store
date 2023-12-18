package org.example.sbdcoursework.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.sbdcoursework.dto.ticket.TicketCreationDTO;
import org.example.sbdcoursework.dto.ticket.TicketDTO;
import org.example.sbdcoursework.service.TicketService;
import org.hibernate.validator.constraints.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ticket")
@RequiredArgsConstructor
@Validated
public class TicketController {

    public static final String TICKET_PATH = "/api/ticket";
    public static final String TICKET_ID_PATH_VAR = "/{ticketId}";

    public final TicketService ticketService;

    @PostMapping
    public ResponseEntity<java.util.UUID> createTicket(
            @RequestBody
            @Valid
            TicketCreationDTO creationDTO
    ) {
        java.util.UUID createdTicketUuid = ticketService.create(
                creationDTO
        );

        return new ResponseEntity<>(createdTicketUuid, HttpStatus.CREATED);
    }

    @GetMapping(TICKET_ID_PATH_VAR)
    public ResponseEntity<TicketDTO> getTicketById(
            @PathVariable
            @UUID
            String ticketId
    ) {
        TicketDTO fetchedTicket = ticketService.getById(
                java.util.UUID.fromString(ticketId)
        );

        return ResponseEntity.ok(fetchedTicket);
    }

    @GetMapping
    @PreAuthorize(
            "#ownerId == authentication.principal.toString()"
    )
    public ResponseEntity<List<TicketDTO>> listTicketByOwnerId(
            @RequestParam
            @UUID
            String ownerId
    ) {
        List<TicketDTO> ownerTickets = ticketService.listByOwner(
                java.util.UUID.fromString(ownerId)
        );

        return ResponseEntity.ok(ownerTickets);
    }
}
