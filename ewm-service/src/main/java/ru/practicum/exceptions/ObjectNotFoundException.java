package ru.practicum.exceptions;



 // todo удалено  @ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "The search was fallen")
public class ObjectNotFoundException extends RuntimeException {
    public ObjectNotFoundException(String message) {
        super(message);
    }
}
