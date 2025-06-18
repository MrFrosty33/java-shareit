package ru.practicum.server.exception;

import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.NoSuchElementException;

@Slf4j
@RestControllerAdvice(basePackages = "ru.practicum.server")
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
    @ResponseStatus(HttpStatus.BAD_REQUEST)
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
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleOther(Exception e) {
        String exceptionName = e.getClass().getSimpleName();
        String message = e.getMessage();

        log.info("ApplicationExceptionHandler поймал {} с сообщением: {}",
                exceptionName, message);
        return new ErrorResponse(message);
    }

    @ExceptionHandler(NoSuchElementException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleNoSuchElement(NoSuchElementException e) {
        String exceptionName = e.getClass().getSimpleName();
        String message = e.getMessage();

        log.info("ApplicationExceptionHandler поймал {} с сообщением: {}",
                exceptionName, message);
        return new ErrorResponse(message);
    }
    // в последующих 3-х методах можно подумать над упрощением сообщения, чтобы оно не было несколько строк в длину
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleMethodArgumentNotValid(MethodArgumentNotValidException e) {
        String exceptionName = e.getClass().getSimpleName();
        String message = e.getMessage();

        log.info("ApplicationExceptionHandler поймал {} с сообщением: {}",
                exceptionName, message);
        return new ErrorResponse(message);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleConstraintViolation(ConstraintViolationException e) {
        String exceptionName = e.getClass().getSimpleName();
        String message = e.getMessage();

        log.info("ApplicationExceptionHandler поймал {} с сообщением: {}",
                exceptionName, message);
        return new ErrorResponse(message);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {
        String exceptionName = e.getClass().getSimpleName();
        String message = e.getMessage();

        log.info("ApplicationExceptionHandler поймал {} с сообщением: {}",
                exceptionName, message);
        return new ErrorResponse(message);
    }

}
