package ru.practicum.exceptions;




public class BadRequestException extends RuntimeException {
    public BadRequestException() {
    }

    public BadRequestException(final String error) {
        super(error);
    }
}
