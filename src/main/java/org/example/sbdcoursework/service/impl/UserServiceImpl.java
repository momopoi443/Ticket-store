package org.example.sbdcoursework.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.sbdcoursework.entity.user.User;
import org.example.sbdcoursework.exception.InvalidArgumentException;
import org.example.sbdcoursework.exception.NotFoundException;
import org.example.sbdcoursework.mapper.UserMapper;
import org.example.sbdcoursework.repository.UserRepository;
import org.example.sbdcoursework.dto.user.UserCreationDTO;
import org.example.sbdcoursework.dto.user.UserDTO;
import org.example.sbdcoursework.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public UUID register(UserCreationDTO creationData) {
        if (userRepository.existsByEmail(creationData.getEmail())) {
            logAndThrowException(
                    new InvalidArgumentException(
                            "Invalid email: given email is already taken"
                    )
            );
        }

        User userToSave = new User(UUID.randomUUID());
        userMapper.mapUserCreationDTOToUser(
                creationData,
                userToSave
        );

        User savedUser = userRepository.save(userToSave);
        log.info("User: " + savedUser.getEmail() + " registered");

        return savedUser.getUuid();
    }

    @Override
    @Transactional(readOnly = true)
    public UserDTO getById(UUID userUuid) {
        AtomicReference<UserDTO> userDTO = new AtomicReference<>();

        userRepository.findByUuid(userUuid).ifPresentOrElse(
                foundUser -> userDTO.set(
                        userMapper.mapUserToUserDTO(foundUser)
                ),
                () -> logAndThrowException(
                    new NotFoundException("No such users found")
                )
        );

        return userDTO.get();
    }

    @Override
    public void delete(UUID userUuid) {
        if (!userRepository.existsByUuid(userUuid)) {
            logAndThrowException(
                    new NotFoundException("No such users found")
            );
        }

        userRepository.deleteByUuid(userUuid);
        log.info("Deleted user: " + userUuid);
    }

    private void logAndThrowException(RuntimeException exception) {
        log.error(exception.getMessage(), exception);
        throw exception;
    }
}
