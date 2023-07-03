package ru.practicum.stat.exceptions;

public class BadRequestException extends RuntimeException  {

    public BadRequestException(String message) {
        super(message);
    }
}
