package ticket.store.backend.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import ticket.store.backend.dto.auth.CredentialsDto;
import ticket.store.backend.dto.auth.LoginResponseDto;
import ticket.store.backend.service.AuthService;
import org.hibernate.validator.constraints.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static ticket.store.backend.validation.ValidationErrorMessages.INVALID_UUID_MESSAGE;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Validated
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody @Valid CredentialsDto credentials) {
        LoginResponseDto loginResponseDto = authService.login(credentials);

        return ResponseEntity.ok(loginResponseDto);
    }

    @PostMapping("/refresh")
    public ResponseEntity<String> refresh(
            @RequestBody @Pattern(regexp = "^([a-zA-Z0-9_=]+)\\.([a-zA-Z0-9_=]+)\\.([a-zA-Z0-9_\\-\\+\\/=]*)", message = "String must be valid refresh token")
            String refreshToken
    ) {
        String accessToken = authService.refresh(refreshToken);

        return ResponseEntity.ok(accessToken);
    }

    @PostMapping("/logout/{userId}")
    public ResponseEntity<Void> logout(@PathVariable @UUID(message = INVALID_UUID_MESSAGE) String userId) {
        authService.logout(java.util.UUID.fromString(userId));

        return ResponseEntity.noContent().build();
    }
}
