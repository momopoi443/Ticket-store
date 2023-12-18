package org.example.sbdcoursework.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import org.example.sbdcoursework.dto.auth.CredentialsDTO;
import org.example.sbdcoursework.dto.auth.LoginResponsePayload;
import org.example.sbdcoursework.service.AuthService;
import org.hibernate.validator.constraints.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Validated
public class AuthController {

    public static final String AUTH_PATH = "/api/auth";

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponsePayload> login(
            @RequestBody
            @Valid
            CredentialsDTO credentials
    ) {
        LoginResponsePayload loginResponsePayload = authService.login(credentials);

        return ResponseEntity.ok(loginResponsePayload);
    }

    @PostMapping("/refresh")
    public ResponseEntity<String> refresh(
            @RequestParam(name = "refreshToken")
            @Pattern(regexp = "^([a-zA-Z0-9_=]+)\\.([a-zA-Z0-9_=]+)\\.([a-zA-Z0-9_\\-\\+\\/=]*)")
            String refreshToken
    ) {
        String accessToken = authService.refresh(refreshToken);

        return ResponseEntity.ok(accessToken);
    }

    @PostMapping("/logout/{userUuid}")
    @PreAuthorize(
            "#userUuid == authentication.principal.toString()"
    )
    public ResponseEntity<Void> logout(
            @PathVariable
            @UUID
            String userUuid
    ) {
        authService.logout(
                java.util.UUID.fromString(userUuid)
        );

        return ResponseEntity.ok().build();
    }
}
