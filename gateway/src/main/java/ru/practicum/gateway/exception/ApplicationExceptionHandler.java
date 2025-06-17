package ru.practicum.gateway.exception;

import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@Slf4j
@RestControllerAdvice(basePackages = "ru.practicum.gateway")
public class ApplicationExceptionHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNotFound(NotFoundException e) {
        String exceptionName = e.getClass().getSimpleName();
        String message = e.getMessage();

        log.info("ApplicationExceptionHandler поймал {} с сообщением: {}",
                exceptionName, message);
        return new ErrorResponse(message);
    }

    @ExceptionHandler
    @ResponseStatus(BAD_REQUEST)
    public ErrorResponse handleBadParam(BadRequestParamException e) {
        String exceptionName = e.getClass().getSimpleName();
        String message = e.getMessage();

        log.info("ApplicationExceptionHandler поймал {} с сообщением: {}",
                exceptionName, message);
        return new ErrorResponse(message);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleConflict(ConflictException e) {
        String exceptionName = e.getClass().getSimpleName();
        String message = e.getMessage();

        log.info("ApplicationExceptionHandler поймал {} с сообщением: {}",
                exceptionName, message);
        return new ErrorResponse(message);
    }

    @ExceptionHandler({InternalServerException.class, Exception.class})
    @ResponseStatus(INTERNAL_SERVER_ERROR)
    public ErrorResponse handleOther(Exception e) {
        String exceptionName = e.getClass().getSimpleName();
        String message = e.getMessage();

        log.info("ApplicationExceptionHandler поймал {} с сообщением: {}",
                exceptionName, message);
        return new ErrorResponse(message);
    }

    @ExceptionHandler(HttpClientErrorException.class)
    public ResponseEntity<ErrorResponse> handleHttpClientError(HttpClientErrorException e) {
        // т.к. я не могу определить какой код ошибки изначательно содержится, не могу и выставить сразу @ResponseStatus
        // потому этот метод будет несколько отличаться от остальных, но зато возвращать нужный ответ
        String exceptionName = e.getClass().getSimpleName();
        String message = e.getMessage();

        log.info("ApplicationExceptionHandler поймал {} с сообщением: {}", exceptionName, message);
        return ResponseEntity.status(e.getStatusCode()).body(new ErrorResponse(message));
    }

    // в последующих 3-х методах можно подумать над упрощением сообщения, чтобы оно не было несколько строк в длину
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(BAD_REQUEST)
    public ErrorResponse handleMethodArgumentNotValid(MethodArgumentNotValidException e) {
        String exceptionName = e.getClass().getSimpleName();
        String message = e.getMessage();

        log.info("ApplicationExceptionHandler поймал {} с сообщением: {}",
                exceptionName, message);
        return new ErrorResponse(message);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(BAD_REQUEST)
    public ErrorResponse handleConstraintViolation(ConstraintViolationException e) {
        String exceptionName = e.getClass().getSimpleName();
        String message = e.getMessage();

        log.info("ApplicationExceptionHandler поймал {} с сообщением: {}",
                exceptionName, message);
        return new ErrorResponse(message);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(BAD_REQUEST)
    public ErrorResponse handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {
        String exceptionName = e.getClass().getSimpleName();
        String message = e.getMessage();

        log.info("ApplicationExceptionHandler поймал {} с сообщением: {}",
                exceptionName, message);
        return new ErrorResponse(message);
    }

}
