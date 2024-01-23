package org.example.sbdcoursework.controller.advice;

import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.example.sbdcoursework.dto.ApiErrorDto;
import org.example.sbdcoursework.exception.external.InvalidArgumentException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
@Slf4j
public class InputValidationControllerAdvice {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<List<ApiErrorDto>> handleMethodArgumentNotValid(MethodArgumentNotValidException exception) {
        List<ApiErrorDto> apiErrorDtos = exception.getFieldErrors().stream()
                .map(error -> new ApiErrorDto(
                        InvalidArgumentException.errorCode(),
                        error.getField() + ": " + error.getDefaultMessage()
                ))
                .toList();

        return new ResponseEntity<>(apiErrorDtos, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiErrorDto> handleConstraintViolation(ConstraintViolationException exception) {
        ApiErrorDto apiErrorDto = ApiErrorDto.builder()
                .code(InvalidArgumentException.errorCode())
                .description(exception.getConstraintViolations().iterator().next().getMessage())
                .build();

        return new ResponseEntity<>(apiErrorDto, HttpStatus.BAD_REQUEST);
    }
}
