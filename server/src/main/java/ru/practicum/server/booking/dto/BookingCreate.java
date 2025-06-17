package ru.practicum.server.booking.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Data;
import ru.practicum.server.booking.Status;
import ru.practicum.server.markers.OnCreate;
import ru.practicum.server.markers.OnUpdate;

import java.time.LocalDateTime;

@Data
@Builder(toBuilder = true)
public class BookingCreate {
    private Long id;

    @NotNull(message = "ошибка валидации, itemId не может быть null", groups = {OnCreate.class})
    @Positive(message = "ошибка валидации, itemId должно быть положительным числом",
            groups = {OnCreate.class, OnUpdate.class})
    private Long itemId;

    @NotNull(message = "ошибка валидации, start не может быть null", groups = {OnCreate.class})
    @FutureOrPresent(message = "ошибка валидации, start не может быть в прошлом",
            groups = {OnCreate.class, OnUpdate.class})
    private LocalDateTime start;

    @NotNull(message = "ошибка валидации, end не может быть null", groups = {OnCreate.class})
    @Future(message = "ошибка валидации, end должно быть в будущем",
            groups = {OnCreate.class, OnUpdate.class})
    private LocalDateTime end;

    @Positive(message = "ошибка валидации, bookerId должно быть положительным числом",
            groups = {OnCreate.class, OnUpdate.class})
    private Long bookerId;

    private Status status;
}
