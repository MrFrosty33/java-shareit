package ru.practicum.shareit.validator;

public interface Validator<T> {
    void validateExists(Long id);
}
