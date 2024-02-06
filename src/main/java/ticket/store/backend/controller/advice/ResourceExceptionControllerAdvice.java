package ticket.store.backend.controller.advice;

import ticket.store.backend.dto.ApiErrorDto;
import ticket.store.backend.exception.internal.InternalEventStorageException;
import ticket.store.backend.exception.internal.InternalImageStorageException;
import ticket.store.backend.exception.external.InvalidArgumentException;
import ticket.store.backend.exception.external.NotFoundException;
import ticket.store.backend.exception.internal.InternalTicketStorageException;
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
