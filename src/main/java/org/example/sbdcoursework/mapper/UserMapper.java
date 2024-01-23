package org.example.sbdcoursework.mapper;

import org.example.sbdcoursework.dto.user.UserCreationDto;
import org.example.sbdcoursework.dto.user.UserDto;
import org.example.sbdcoursework.entity.user.User;

public interface UserMapper {

    UserDto mapUserToUserDTO(User user);

    User mapUserCreationDtoToUser(UserCreationDto dto);
}
