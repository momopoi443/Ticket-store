package org.example.sbdcoursework.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.sbdcoursework.dto.user.UserCreationDTO;
import org.example.sbdcoursework.dto.user.UserDTO;
import org.example.sbdcoursework.service.UserService;
import org.hibernate.validator.constraints.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
@Validated
public class UserController {

    public static final String USER_PATH = "/api/user";
    public static final String USER_ID_PATH_VAR = "/{userId}";

    private final UserService userService;

    @PostMapping
    public ResponseEntity<java.util.UUID> registerUser(
            @RequestBody
            @Valid
            UserCreationDTO creationData
    ) {
        java.util.UUID registeredUserUuid = userService.register(
                creationData
        );

        return new ResponseEntity<>(registeredUserUuid, HttpStatus.CREATED);
    }

    @GetMapping(USER_ID_PATH_VAR)
    @PreAuthorize(
            "#userId == authentication.principal.toString()"
    )
    public ResponseEntity<UserDTO> getUserById(
            @PathVariable
            @UUID
            String userId
    ) {
        UserDTO userDTO = userService.getById(
                java.util.UUID.fromString(userId)
        );

        return ResponseEntity.ok(userDTO);
    }

    @DeleteMapping(USER_ID_PATH_VAR)
    @PreAuthorize(
            "#userId == authentication.principal.toString()"
    )
    public ResponseEntity<Void> deleteUser(
            @PathVariable
            @UUID
            String userId
    ) {
        userService.delete(
                java.util.UUID.fromString(userId)
        );

        return ResponseEntity.noContent().build();
    }
}
