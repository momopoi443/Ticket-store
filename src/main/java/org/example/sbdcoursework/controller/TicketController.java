package org.example.sbdcoursework.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.sbdcoursework.dto.ticket.TicketCreationDto;
import org.example.sbdcoursework.dto.ticket.TicketDto;
import org.example.sbdcoursework.service.TicketService;
import org.hibernate.validator.constraints.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.example.sbdcoursework.validation.ValidationErrorMessages.INVALID_UUID_MESSAGE;

@RestController
@RequestMapping("/api/ticket")
@RequiredArgsConstructor
@Validated
public class TicketController {

    public final TicketService ticketService;

    @PostMapping
    public ResponseEntity<java.util.UUID> createTicket(@RequestBody @Valid TicketCreationDto creationDto) {
        java.util.UUID createdTicketUuid = ticketService.create(creationDto);

        return new ResponseEntity<>(createdTicketUuid, HttpStatus.CREATED);
    }

    @GetMapping("/{ticketId}")
    public ResponseEntity<TicketDto> getTicketById(@PathVariable @UUID(message = INVALID_UUID_MESSAGE) String ticketId) {
        TicketDto fetchedTicketDto = ticketService.getById(java.util.UUID.fromString(ticketId));

        return ResponseEntity.ok(fetchedTicketDto);
    }
}
