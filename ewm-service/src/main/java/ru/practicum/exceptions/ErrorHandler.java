package ru.practicum.exceptions;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Collections;
import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;


public class ErrorHandler {

    private final StringWriter stringWriter = new StringWriter();
    private final PrintWriter printWriter = new PrintWriter(stringWriter);

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleConflict(final ConflictException validException) {
        return conflict(validException);
    }

    private ApiError conflict(final Exception e) {
        e.printStackTrace(printWriter);
        return ApiError.builder()
                .errors(Collections.singletonList(stringWriter.toString()))
                .status(HttpStatus.CONFLICT)
                .reason("Bad request.")
                .message(e.getMessage())
                .timestamp(LocalDateTime.now().toString())
                .build();
    }
}
