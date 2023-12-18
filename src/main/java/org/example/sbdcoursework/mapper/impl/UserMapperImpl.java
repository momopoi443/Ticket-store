package org.example.sbdcoursework.mapper.impl;

import lombok.RequiredArgsConstructor;
import org.example.sbdcoursework.dto.user.UserCreationDTO;
import org.example.sbdcoursework.dto.user.UserDTO;
import org.example.sbdcoursework.entity.user.User;
import org.example.sbdcoursework.mapper.UserMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserMapperImpl implements UserMapper {

    private final PasswordEncoder passwordEncoder;

    @Override
    public void mapUserCreationDTOToUser(UserCreationDTO dto, User user) {
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setEmail(dto.getEmail());
        user.setPassword(
                passwordEncoder.encode(dto.getPassword())
        );
        user.setRole(dto.getRole());
    }

    @Override
    public UserDTO mapUserToUserDTO(User user) {
        return new UserDTO(
                user.getUuid(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getRole()
        );
    }
}
