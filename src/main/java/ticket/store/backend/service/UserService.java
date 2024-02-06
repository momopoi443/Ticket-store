package ticket.store.backend.service;

import ticket.store.backend.dto.user.UserCreationDto;
import ticket.store.backend.dto.user.UserDto;

import java.util.UUID;

public interface UserService {

    UUID register(UserCreationDto creationDto);

    UserDto getById(UUID userUuid);
}
