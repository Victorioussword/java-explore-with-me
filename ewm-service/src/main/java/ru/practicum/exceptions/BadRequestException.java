package ru.practicum.exceptions;



  // todo удалено указание статуса ответа @ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Bad request")
public class BadRequestException extends RuntimeException {
    public BadRequestException() {
    }

    public BadRequestException(final String error) {
        super(error);
    }
}
