package ru.practicum.exceptions;


public class ConflictException extends RuntimeException {
    public ConflictException(final String error) {
        super(error);
    }
}
