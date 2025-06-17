package ru.practicum.models.exception;

public class BadRequestParamException extends RuntimeException {
    public BadRequestParamException(String message) {
        super(message);
    }
}
