package ru.practicum.exceptions;




// todo удалено указание статуса ответа @ResponseStatus(value = HttpStatus.CONFLICT, reason = "Conflict")

public class ConflictException extends RuntimeException {
    public ConflictException(final String error) {
        super(error);
    }
}
