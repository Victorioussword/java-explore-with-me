package ru.practicum.exceptions;


import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.Set;


public class BadEmailException extends ConstraintViolationException {


    public BadEmailException(String message, Set<? extends ConstraintViolation<?>> constraintViolations) {
        super(message, constraintViolations);
    }
}
