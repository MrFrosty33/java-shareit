package ru.practicum.gateway.exception;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Slf4j
@RestControllerAdvice(basePackages = "ru.practicum.gateway")
public class ApplicationExceptionHandler {

    @ExceptionHandler(HttpClientErrorException.class)
    public ResponseEntity<ErrorResponse> handleHttpClientError(HttpClientErrorException e) {
        // т.к. я не могу определить какой код ошибки изначательно содержится, не могу и выставить сразу @ResponseStatus
        // потому этот метод будет несколько отличаться от остальных, но зато возвращать нужный ответ
        String status = e.getStatusCode().toString();
        String rawBody = e.getResponseBodyAsString();
        String message = rawBody;

        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode json = mapper.readTree(rawBody);
            if (json.has("error")) {
                message = json.get("error").asText();
            }
        } catch (Exception ex) {
            log.info("ApplicationExceptionHandler: непредвиденная ошибка в методе handleHttpClientError");
        }

        log.info("ApplicationExceptionHandler получил {} с сообщением: {}", status, message);
        return ResponseEntity.status(e.getStatusCode()).body(new ErrorResponse(message));
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

    // в последующих 3-х методах можно подумать над упрощением сообщения, чтобы оно не было несколько строк в длину
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(BAD_REQUEST)
    public ErrorResponse handleMethodArgumentNotValid(MethodArgumentNotValidException e) {
        String exceptionName = e.getClass().getSimpleName();
        String message = e.getBindingResult().getFieldErrors().stream()
                .map(error -> String.format("%s: %s", error.getField(), error.getDefaultMessage()))
                .collect(Collectors.joining("; "));

        log.info("ApplicationExceptionHandler поймал {} с сообщением: {}",
                exceptionName, message);
        return new ErrorResponse(message);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(BAD_REQUEST)
    public ErrorResponse handleConstraintViolation(ConstraintViolationException e) {
        String exceptionName = e.getClass().getSimpleName();
        String message = e.getConstraintViolations().stream()
                .map(v -> v.getPropertyPath() + ": " + v.getMessage())
                .collect(Collectors.joining("; "));

        log.info("ApplicationExceptionHandler поймал {} с сообщением: {}",
                exceptionName, message);
        return new ErrorResponse(message);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(BAD_REQUEST)
    public ErrorResponse handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {
        String exceptionName = e.getClass().getSimpleName();
        String param = e.getName();
        String type = e.getRequiredType() != null ? e.getRequiredType().getSimpleName() : "Unknown";
        String value = String.valueOf(e.getValue());

        String message = String.format("Параметр '%s' со значением '%s' не может быть преобразован в тип %s",
                param, value, type);

        log.info("ApplicationExceptionHandler поймал {} с сообщением: {}",
                exceptionName, message);
        return new ErrorResponse(message);
    }

}
