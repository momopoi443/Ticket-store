package org.example.sbdcoursework.controller.advice;

import org.example.sbdcoursework.dto.ApiErrorDTO;
import org.example.sbdcoursework.exception.InvalidTokenException;
import org.example.sbdcoursework.exception.InvalidUserCredentialsException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class AuthControllerAdvice {

    @ExceptionHandler(value = {
            InvalidTokenException.class
    })
    public ResponseEntity<ApiErrorDTO> handleInvalidToken(
            InvalidTokenException exception
    ) {
        ApiErrorDTO errorInfo = ApiErrorDTO.builder()
                .code(InvalidTokenException.errorCode())
                .description(exception.getMessage())
                .build();

        return new ResponseEntity<>(errorInfo, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(value = {
            InvalidUserCredentialsException.class
    })
    public ResponseEntity<ApiErrorDTO> handleInvalidUserCredentials(
            InvalidUserCredentialsException exception
    ) {
        ApiErrorDTO errorInfo = ApiErrorDTO.builder()
                .code(InvalidUserCredentialsException.errorCode())
                .description(exception.getMessage())
                .build();

        return new ResponseEntity<>(errorInfo, HttpStatus.UNAUTHORIZED);
    }
}
