package org.example.sbdcoursework.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.sbdcoursework.dto.user.UserCreationDto;
import org.example.sbdcoursework.dto.user.UserDto;
import org.example.sbdcoursework.entity.user.User;
import org.example.sbdcoursework.entity.user.UserRole;
import org.example.sbdcoursework.exception.external.InvalidArgumentException;
import org.example.sbdcoursework.exception.external.NotFoundException;
import org.example.sbdcoursework.mapper.UserMapper;
import org.example.sbdcoursework.repository.UserRepository;
import org.example.sbdcoursework.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public UUID register(UserCreationDto creationDto) {
        if (creationDto.getRole().equals(UserRole.ADMIN)) {
            throw new InvalidArgumentException("Invalid role: can't register admin");
        }
        if (userRepository.existsByEmail(creationDto.getEmail())) {
            throw new InvalidArgumentException("Invalid email: given email is already taken");
        }

        User savedUser = userRepository.save(userMapper.mapUserCreationDtoToUser(creationDto));
        log.info("User with email: [" + savedUser.getEmail() + "] registered");
        return savedUser.getUuid();
    }

    @Override
    @Transactional(readOnly = true)
    public UserDto getById(UUID userUuid) {
        User fetchedUser = userRepository.findByUuid(userUuid)
                .orElseThrow(() -> new NotFoundException("No such users found"));

        return userMapper.mapUserToUserDTO(fetchedUser);
    }
}
