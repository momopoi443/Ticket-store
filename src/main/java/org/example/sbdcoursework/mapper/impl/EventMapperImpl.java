package org.example.sbdcoursework.mapper.impl;

import org.example.sbdcoursework.dto.event.EventCreationDTO;
import org.example.sbdcoursework.dto.event.EventDTO;
import org.example.sbdcoursework.entity.event.Event;
import org.example.sbdcoursework.mapper.EventMapper;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.UUID;

import static org.example.sbdcoursework.controller.EventController.EVENT_IMAGE_PATH;
import static org.example.sbdcoursework.controller.EventController.EVENT_PATH;

@Component
public class EventMapperImpl implements EventMapper {

    @Override
    public void mapEventCreationDTOToEvent(
            EventCreationDTO dto,
            String imageFilename,
            Event event
    ) {
        event.setName(dto.getName());
        event.setCity(dto.getCity());
        event.setType(dto.getType());
        event.setCityAddress(dto.getCityAddress());
        event.setDescription(dto.getDescription());
        event.setOrganizerId(
                UUID.fromString(dto.getOrganizerId())
        );
        event.setImageFilename(imageFilename);
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
                ServletUriComponentsBuilder.fromCurrentContextPath().toUriString() + EVENT_PATH + EVENT_IMAGE_PATH + "/" + event.getImageFilename(),
                event.getDate(),
                event.getTicketPrice(),
                event.getMaxTicketAmount() - (soldTickets == null ? 0: soldTickets)
        );
    }
}
