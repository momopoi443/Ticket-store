package org.example.sbdcoursework.mapper;

import org.example.sbdcoursework.dto.event.EventCreationDTO;
import org.example.sbdcoursework.dto.event.EventDTO;
import org.example.sbdcoursework.entity.event.Event;

public interface EventMapper {

    void mapEventCreationDTOToEvent(EventCreationDTO dto, String imageFilename, Event event);

    EventDTO mapEventToEventDTO(Event event, Long soldTickets);
}
