package org.example.sbdcoursework.controller.advice;

import org.example.sbdcoursework.dto.ApiErrorDto;
import org.example.sbdcoursework.exception.internal.InternalEventStorageException;
import org.example.sbdcoursework.exception.internal.InternalImageStorageException;
import org.example.sbdcoursework.exception.external.InvalidArgumentException;
import org.example.sbdcoursework.exception.external.NotFoundException;
import org.example.sbdcoursework.exception.internal.InternalTicketStorageException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ResourceExceptionControllerAdvice {

    @ExceptionHandler(InvalidArgumentException.class)
    public ResponseEntity<ApiErrorDto> handleInvalidArguments(InvalidArgumentException exception) {
        ApiErrorDto apiErrorDto = ApiErrorDto.builder()
                .code(InvalidArgumentException.errorCode())
                .description(exception.getMessage())
                .build();

        return new ResponseEntity<>(apiErrorDto, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ApiErrorDto> handleNotFound(NotFoundException exception) {
        ApiErrorDto apiErrorDto = ApiErrorDto.builder()
                .code(NotFoundException.errorCode())
                .description(exception.getMessage())
                .build();

        return new ResponseEntity<>(apiErrorDto, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({
            InternalEventStorageException.class,
            InternalImageStorageException.class,
            InternalTicketStorageException.class
    })
    public ResponseEntity<ApiErrorDto> handleInternalExceptions() {
        ApiErrorDto apiErrorDto = ApiErrorDto.builder()
                .code("internal.server.error")
                .description("Error occurred on behalf of server")
                .build();

        return new ResponseEntity<>(apiErrorDto, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
