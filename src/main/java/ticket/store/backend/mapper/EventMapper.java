package ticket.store.backend.mapper;

import ticket.store.backend.dto.event.EventCreationDto;
import ticket.store.backend.dto.event.EventDto;
import ticket.store.backend.entity.event.Event;

import java.util.List;

public interface EventMapper {

    void mapEventCreationDtoToEvent(EventCreationDto dto, String imageFilename, Event event);

    EventDto mapEventToEventDto(Event event);

    List<EventDto> mapEventsToEventDtos(List<Event> events);
}
