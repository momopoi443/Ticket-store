package org.example.sbdcoursework.mapper;

import org.example.sbdcoursework.dto.event.EventCreationDto;
import org.example.sbdcoursework.dto.event.EventDto;
import org.example.sbdcoursework.entity.event.Event;

import java.util.List;

public interface EventMapper {

    void mapEventCreationDtoToEvent(EventCreationDto dto, String imageFilename, Event event);

    EventDto mapEventToEventDto(Event event);

    List<EventDto> mapEventsToEventDtos(List<Event> events);
}
