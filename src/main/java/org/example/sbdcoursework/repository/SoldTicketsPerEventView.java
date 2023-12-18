package org.example.sbdcoursework.repository;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class SoldTicketsPerEventView {

    private UUID eventUuid;

    private Long soldTickets;
}

