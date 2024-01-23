package org.example.sbdcoursework.mapper.impl;

import lombok.RequiredArgsConstructor;
import org.example.sbdcoursework.dto.event.EventCreationDto;
import org.example.sbdcoursework.dto.event.EventDto;
import org.example.sbdcoursework.entity.event.Event;
import org.example.sbdcoursework.mapper.EventMapper;
import org.example.sbdcoursework.repository.SoldTicketsPerEventView;
import org.example.sbdcoursework.repository.TicketRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class EventMapperImpl implements EventMapper {

    private final TicketRepository ticketRepository;

    @Override
    public void mapEventCreationDtoToEvent(EventCreationDto dto, String imageFilename, Event event) {
        event.setName(dto.getName());
        event.setCity(dto.getCity());
        event.setType(dto.getType());
        event.setCityAddress(dto.getCityAddress());
        event.setDescription(dto.getDescription());
        event.setOrganizerId(UUID.fromString(dto.getOrganizerId()));
        event.setImageFilename(imageFilename);
        event.setDate(dto.getDate());
        event.setTicketPrice(dto.getTicketPrice());
        event.setMaxTicketAmount(dto.getMaxTicketAmount());
        event.setIsConfirmed(false);
    }

    @Override
    public EventDto mapEventToEventDto(Event event) {
        return mapEventDto(event, ticketRepository.countSoldTicketsByEventUuid(event.getUuid()));
    }

    @Override
    public List<EventDto> mapEventsToEventDtos(List<Event> events) {
        Map<UUID, Long> soldTicketsPerEvent = ticketRepository.countSoldTicketsGroupedByEventUuid().stream()
                .collect(Collectors.toMap(SoldTicketsPerEventView::getEventUuid, SoldTicketsPerEventView::getSoldTickets));

        return events.stream()
                .map(event -> mapEventDto(event, soldTicketsPerEvent.getOrDefault(event.getUuid(), 0L)))
                .toList();
    }

    private EventDto mapEventDto(Event event, long soldTickets) {
        return new EventDto(
                event.getUuid(),
                event.getName(),
                event.getType(),
                event.getCity(),
                event.getCityAddress(),
                event.getDescription(),
                ServletUriComponentsBuilder.fromCurrentContextPath()
                        .path("/api/event/image/{imageFilename}")
                        .build(event.getImageFilename())
                        .getRawPath(),
                event.getDate(),
                event.getTicketPrice(),
                event.getMaxTicketAmount() - soldTickets,
                event.getIsConfirmed()
        );
    }
}
