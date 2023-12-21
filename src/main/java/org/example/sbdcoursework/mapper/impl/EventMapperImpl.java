package org.example.sbdcoursework.mapper.impl;

import org.example.sbdcoursework.dto.event.EventCreationDTO;
import org.example.sbdcoursework.dto.event.EventDTO;
import org.example.sbdcoursework.entity.event.Event;
import org.example.sbdcoursework.mapper.EventMapper;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class EventMapperImpl implements EventMapper {

    @Override
    public void mapEventCreationDTOToEvent(EventCreationDTO dto, Event event) {
        event.setName(dto.getName());
        event.setCity(dto.getCity());
        event.setType(dto.getType());
        event.setCityAddress(dto.getCityAddress());
        event.setDescription(dto.getDescription());
        event.setOrganizerId(
                UUID.fromString(dto.getOrganizerId())
        );
        event.setImageName(dto.getImageName());
        event.setDate(dto.getDate());
        event.setTicketPrice(dto.getTicketPrice());
        event.setMaxTicketAmount(dto.getMaxTicketAmount());
    }

    @Override
    public EventDTO mapEventToEventDTO(Event event, Long soldTickets) {
        return new EventDTO(
                event.getUuid(),
                event.getName(),
                event.getType(),
                event.getCity(),
                event.getCityAddress(),
                event.getDescription(),
                event.getImageName(),
                event.getDate(),
                event.getTicketPrice(),
                event.getMaxTicketAmount() - (soldTickets == null ? 0: soldTickets)
        );
    }
}
