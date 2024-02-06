package ticket.store.backend.mapper.impl;

import ticket.store.backend.dto.ticket.TicketCreationDto;
import ticket.store.backend.dto.ticket.TicketDto;
import ticket.store.backend.entity.event.Event;
import ticket.store.backend.entity.Ticket;
import ticket.store.backend.mapper.TicketMapper;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.time.LocalDateTime;
import java.util.UUID;

@Component
public class TicketMapperImpl implements TicketMapper {

    @Override
    public Ticket mapTicketCreationDTOToTicket(TicketCreationDto creationDTO, UUID ownerUuid) {
        Ticket ticket = new Ticket(UUID.randomUUID());
        ticket.setUserFirstName(creationDTO.getUserFirstName());
        ticket.setUserLastName(creationDTO.getUserLastName());
        ticket.setUserUuid(ownerUuid);
        ticket.setUserEmail(creationDTO.getUserEmail());
        ticket.setEventUuid(UUID.fromString(creationDTO.getEventUuid()));
        ticket.setCreatedAt(LocalDateTime.now().withNano(0));

        return ticket;
    }

    @Override
    public TicketDto mapTicketAndEventToTicketDTO(Ticket ticket, Event event) {
        return new TicketDto(
                ticket.getUuid(),
                ticket.getUserUuid(),
                ticket.getUserFirstName(),
                ticket.getUserLastName(),
                ticket.getUserEmail(),
                ticket.getEventUuid(),
                event.getDate(),
                event.getCity(),
                event.getCityAddress(),
                ServletUriComponentsBuilder.fromCurrentContextPath()
                        .path("/api/event/image/{imageFilename}")
                        .build(event.getImageFilename())
                        .getRawPath(),
                ticket.getCreatedAt()
        );
    }
}
