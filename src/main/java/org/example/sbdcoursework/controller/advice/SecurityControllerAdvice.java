package org.example.sbdcoursework.controller.advice;

import org.example.sbdcoursework.dto.ApiErrorDTO;
import org.example.sbdcoursework.exception.InvalidTokenException;
import org.example.sbdcoursework.exception.InvalidUserCredentialsException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class SecurityControllerAdvice {

    @ExceptionHandler({InvalidTokenException.class})
    public ResponseEntity<ApiErrorDTO> handleInvalidToken(
            InvalidTokenException exception
    ) {
        ApiErrorDTO errorInfo = ApiErrorDTO.builder()
                .code(InvalidTokenException.errorCode())
                .description(exception.getMessage())
                .build();

        return new ResponseEntity<>(errorInfo, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler({InvalidUserCredentialsException.class})
    public ResponseEntity<ApiErrorDTO> handleInvalidUserCredentials(
            InvalidUserCredentialsException exception
    ) {
        ApiErrorDTO errorInfo = ApiErrorDTO.builder()
                .code(InvalidUserCredentialsException.errorCode())
                .description(exception.getMessage())
                .build();

        return new ResponseEntity<>(errorInfo, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler({AccessDeniedException.class})
    public ResponseEntity<ApiErrorDTO> handleAccessDeniedException(
            AccessDeniedException exception
    ) {
        ApiErrorDTO errorInfo = ApiErrorDTO.builder()
                .code("access.denied")
                .description(exception.getMessage())
                .build();

        return new ResponseEntity<>(errorInfo, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler({AuthenticationException.class})
    public ResponseEntity<ApiErrorDTO> handleAccessDeniedException(
            AuthenticationException exception
    ) {
        ApiErrorDTO errorInfo = ApiErrorDTO.builder()
                .code("authentication.failed")
                .description(exception.getMessage())
                .build();

        return new ResponseEntity<>(errorInfo, HttpStatus.UNAUTHORIZED);
    }
}
