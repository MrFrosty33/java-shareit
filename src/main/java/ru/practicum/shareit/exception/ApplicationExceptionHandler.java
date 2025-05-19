package ru.practicum.shareit.exception;

import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice(basePackages = "ru.practicum.shareit")
public class ApplicationExceptionHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNotFound(NotFoundException e) {
        log.info("ApplicationExceptionHandler поймал {} с сообщением: {}",
                e.getClass().getSimpleName(), e.getMessage());
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleBadParam(BadRequestParamException e) {
        log.info("ApplicationExceptionHandler поймал {} с сообщением: {}",
                e.getClass().getSimpleName(), e.getMessage());
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler({MethodArgumentNotValidException.class, ConstraintViolationException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleValidation(Exception e) {
        log.info("ApplicationExceptionHandler поймал {} с сообщением: {}",
                e.getClass().getSimpleName(), e.getMessage());
        return new ErrorResponse("Валидация не прошла: " + e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleConflict(ConflictException e) {
        log.info("ApplicationExceptionHandler поймал {} с сообщением: {}",
                e.getClass().getSimpleName(), e.getMessage());
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler({InternalServerException.class, Exception.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleOther(Exception e) {
        log.info("ApplicationExceptionHandler поймал {} с сообщением: {}",
                e.getClass().getSimpleName(), e.getMessage());
        return new ErrorResponse("Внутрення ошибка сервера: " + e.getMessage());
    }


}
