package ticket.store.backend.controller.advice;

import ticket.store.backend.dto.ApiErrorDto;
import ticket.store.backend.exception.external.InvalidTokenException;
import ticket.store.backend.exception.external.InvalidUserCredentialsException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class SecurityControllerAdvice {

    @ExceptionHandler(InvalidTokenException.class)
    public ResponseEntity<ApiErrorDto> handleInvalidToken(InvalidTokenException exception) {
        ApiErrorDto apiErrorDto = ApiErrorDto.builder()
                .code(InvalidTokenException.errorCode())
                .description(exception.getMessage())
                .build();

        return new ResponseEntity<>(apiErrorDto, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(InvalidUserCredentialsException.class)
    public ResponseEntity<ApiErrorDto> handleInvalidUserCredentials(InvalidUserCredentialsException exception) {
        ApiErrorDto apiErrorDto = ApiErrorDto.builder()
                .code(InvalidUserCredentialsException.errorCode())
                .description(exception.getMessage())
                .build();

        return new ResponseEntity<>(apiErrorDto, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiErrorDto> handleAccessDenied(AccessDeniedException exception) {
        ApiErrorDto apiErrorDto = ApiErrorDto.builder()
                .code("access.denied")
                .description(exception.getMessage())
                .build();

        return new ResponseEntity<>(apiErrorDto, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ApiErrorDto> handleAuthException(AuthenticationException exception) {
        ApiErrorDto apiErrorDto = ApiErrorDto.builder()
                .code("authentication.failed")
                .description("Valid token required")
                .build();

        return new ResponseEntity<>(apiErrorDto, HttpStatus.UNAUTHORIZED);
    }
}
