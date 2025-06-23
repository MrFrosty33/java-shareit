package ru.practicum.server.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

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

}
