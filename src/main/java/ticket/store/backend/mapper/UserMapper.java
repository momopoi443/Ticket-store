package ticket.store.backend.mapper;

import ticket.store.backend.dto.user.UserCreationDto;
import ticket.store.backend.dto.user.UserDto;
import ticket.store.backend.entity.user.User;

public interface UserMapper {

    UserDto mapUserToUserDTO(User user);

    User mapUserCreationDtoToUser(UserCreationDto dto);
}
