package org.example.sbdcoursework.mapper;

import org.example.sbdcoursework.dto.user.UserCreationDTO;
import org.example.sbdcoursework.dto.user.UserDTO;
import org.example.sbdcoursework.entity.user.User;

public interface UserMapper {

    UserDTO mapUserToUserDTO(User user);

    void mapUserCreationDTOToUser(
            UserCreationDTO dto,
            User user
    );
}
