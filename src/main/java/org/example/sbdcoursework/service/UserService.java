package org.example.sbdcoursework.service;

import org.example.sbdcoursework.dto.user.UserCreationDTO;
import org.example.sbdcoursework.dto.user.UserDTO;

import java.util.UUID;

public interface UserService {

    UUID register(UserCreationDTO creationData);

    UserDTO getById(UUID userUuid);

    void delete(UUID userUuid);
}
