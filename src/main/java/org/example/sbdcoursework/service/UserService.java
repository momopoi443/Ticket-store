package org.example.sbdcoursework.service;

import org.example.sbdcoursework.dto.user.UserCreationDto;
import org.example.sbdcoursework.dto.user.UserDto;

import java.util.UUID;

public interface UserService {

    UUID register(UserCreationDto creationDto);

    UserDto getById(UUID userUuid);
}
