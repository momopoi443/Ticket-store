package ticket.store.backend.mapper.impl;

import lombok.RequiredArgsConstructor;
import ticket.store.backend.dto.user.UserCreationDto;
import ticket.store.backend.dto.user.UserDto;
import ticket.store.backend.entity.Ticket;
import ticket.store.backend.entity.event.Event;
import ticket.store.backend.entity.user.User;
import ticket.store.backend.mapper.TicketMapper;
import ticket.store.backend.mapper.UserMapper;
import ticket.store.backend.repository.EventRepository;
import ticket.store.backend.repository.TicketRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class UserMapperImpl implements UserMapper {

    private final PasswordEncoder passwordEncoder;
    private final TicketMapper ticketMapper;
    private final TicketRepository ticketRepository;
    private final EventRepository eventRepository;

    @Override
    public User mapUserCreationDtoToUser(UserCreationDto dto) {
        User user = new User(UUID.randomUUID());
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setEmail(dto.getEmail());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setRole(dto.getRole());

        return user;
    }

    @Override
    public UserDto mapUserToUserDTO(User user) {
        List<Ticket> userTickets = ticketRepository.findAllByUserUuid(user.getUuid());
        Map<UUID, Event> userTicketsEventsMap = eventRepository.findAllByUuidIn(userTickets.stream()
                .map(Ticket::getEventUuid).distinct().toList()
        ).stream().collect(Collectors.toMap(Event::getUuid, event -> event));

        return new UserDto(
                user.getUuid(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getRole(),
                userTickets.stream()
                        .map(ticket -> ticketMapper.mapTicketAndEventToTicketDTO(
                                ticket, userTicketsEventsMap.get(ticket.getEventUuid())
                        ))
                        .toList()
        );
    }
}
