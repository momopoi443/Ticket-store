package ticket.store.backend.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ticket.store.backend.dto.user.UserCreationDto;
import ticket.store.backend.dto.user.UserDto;
import ticket.store.backend.entity.user.User;
import ticket.store.backend.entity.user.UserRole;
import ticket.store.backend.exception.external.InvalidArgumentException;
import ticket.store.backend.exception.external.NotFoundException;
import ticket.store.backend.mapper.UserMapper;
import ticket.store.backend.repository.UserRepository;
import ticket.store.backend.service.UserService;
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
