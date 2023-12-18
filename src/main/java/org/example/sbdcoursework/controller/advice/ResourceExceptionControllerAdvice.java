package org.example.sbdcoursework.controller.advice;

import org.example.sbdcoursework.dto.ApiErrorDTO;
import org.example.sbdcoursework.exception.InvalidArgumentException;
import org.example.sbdcoursework.exception.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ResourceExceptionControllerAdvice {

    @ExceptionHandler(value = {InvalidArgumentException.class})
    public ResponseEntity<ApiErrorDTO> handleInvalidArguments(
            InvalidArgumentException exception
    ) {
        ApiErrorDTO errorInfo = ApiErrorDTO.builder()
                .code(InvalidArgumentException.errorCode())
                .description(exception.getMessage())
                .build();

        return new ResponseEntity<>(errorInfo, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {NotFoundException.class})
    public ResponseEntity<ApiErrorDTO> handleNotFound(
            NotFoundException exception
    ) {
        ApiErrorDTO errorInfo = ApiErrorDTO.builder()
                .code(NotFoundException.errorCode())
                .description(exception.getMessage())
                .build();

        return new ResponseEntity<>(errorInfo, HttpStatus.NOT_FOUND);
    }
}
