package org.example.sbdcoursework.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.sbdcoursework.dto.user.UserCreationDto;
import org.example.sbdcoursework.dto.user.UserDto;
import org.example.sbdcoursework.service.UserService;
import org.hibernate.validator.constraints.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.example.sbdcoursework.validation.ValidationErrorMessages.INVALID_UUID_MESSAGE;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
@Validated
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<java.util.UUID> registerUser(@RequestBody @Valid UserCreationDto creationDto) {
        java.util.UUID registeredUserUuid = userService.register(creationDto);

        return new ResponseEntity<>(registeredUserUuid, HttpStatus.CREATED);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserDto> getUserById(@PathVariable @UUID(message = INVALID_UUID_MESSAGE) String userId) {
        UserDto fetchedUserDto = userService.getById(java.util.UUID.fromString(userId));

        return ResponseEntity.ok(fetchedUserDto);
    }
}
