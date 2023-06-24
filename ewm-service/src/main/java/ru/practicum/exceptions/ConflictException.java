package ru.practicum.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT, reason = "Conflict")
public class ConflictException extends RuntimeException {
    public ConflictException(final String error) {
        super(error);
    }
}
