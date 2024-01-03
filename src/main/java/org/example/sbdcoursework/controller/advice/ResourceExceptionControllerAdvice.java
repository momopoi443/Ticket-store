package org.example.sbdcoursework.controller.advice;

import org.example.sbdcoursework.dto.ApiErrorDTO;
import org.example.sbdcoursework.exception.InternalEventStorageException;
import org.example.sbdcoursework.exception.InternalImageStorageException;
import org.example.sbdcoursework.exception.InvalidArgumentException;
import org.example.sbdcoursework.exception.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ResourceExceptionControllerAdvice {

    @ExceptionHandler({InvalidArgumentException.class})
    public ResponseEntity<ApiErrorDTO> handleInvalidArguments(
            InvalidArgumentException exception
    ) {
        ApiErrorDTO errorDTO = ApiErrorDTO.builder()
                .code(InvalidArgumentException.errorCode())
                .description(exception.getMessage())
                .build();

        return new ResponseEntity<>(errorDTO, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({NotFoundException.class})
    public ResponseEntity<ApiErrorDTO> handleNotFound(
            NotFoundException exception
    ) {
        ApiErrorDTO errorDTO = ApiErrorDTO.builder()
                .code(NotFoundException.errorCode())
                .description(exception.getMessage())
                .build();

        return new ResponseEntity<>(errorDTO, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({
            InternalEventStorageException.class,
            InternalImageStorageException.class
    })
    public ResponseEntity<ApiErrorDTO> handleInternalExceptions() {
        ApiErrorDTO errorDTO = ApiErrorDTO.builder()
                .code("internal.server.error")
                .description("Error occurred on behalf of server")
                .build();

        return new ResponseEntity<>(errorDTO, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
