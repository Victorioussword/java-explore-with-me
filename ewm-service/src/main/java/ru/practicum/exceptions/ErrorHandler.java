package ru.practicum.exceptions;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Collections;
import java.time.LocalDateTime;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ErrorHandler {

    private final StringWriter stringWriter = new StringWriter();
    private final PrintWriter printWriter = new PrintWriter(stringWriter);

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleConflict(final ConflictException conflictException) {
        log.debug("Получен статус 409 Conflict {}", conflictException.getMessage(), conflictException);
        return conflict(conflictException);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError handleNotFound(final ObjectNotFoundException objectNotFoundException) {
        log.debug("Получен статус 409 Conflict {}",objectNotFoundException.getMessage(), objectNotFoundException);
        return conflict(objectNotFoundException);
    }


    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleBadRequest(final BadRequestException badRequestException) {
        log.debug("Получен статус 409 Conflict {}",badRequestException.getMessage(), badRequestException);
        return conflict(badRequestException);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleDataIntegrityViolationException(final DataIntegrityViolationException dataIntegrityViolationException) {
        log.debug("Получен статус 409 Conflict {}", dataIntegrityViolationException.getMessage(), dataIntegrityViolationException);
        return conflict(dataIntegrityViolationException);
    }



    private ApiError conflict(final Exception exception) {
        log.debug("Получен статус 409 Conflict {}", exception.getMessage(), exception);
        exception.printStackTrace(printWriter);
        return ApiError.builder()
                .errors(Collections.singletonList(stringWriter.toString()))
                .status(HttpStatus.CONFLICT)
                .reason("Bad request.")
                .message(exception.getMessage())
                .timestamp(LocalDateTime.now().toString())
                .build();
    }

}
