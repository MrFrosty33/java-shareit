package ru.practicum.shareit.booking.dto;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.booking.Status;
import ru.practicum.shareit.markers.OnCreate;
import ru.practicum.shareit.markers.OnUpdate;

import java.time.LocalDate;

@Data
@Builder(toBuilder = true)
public class BookingDto {
    private Long id;

    @NotNull(message = "ошибка валидации, startDate не может быть null", groups = {OnCreate.class})
    // обновление может происходить в будущем, и стоит оставить возможность установить дату в прошлом
    // поэтому только лишь OnCreate в группах для валидаций указан
    @FutureOrPresent(message = "ошибка валидации, startDate не может быть в прошлом", groups = {OnCreate.class})
    private LocalDate startDate;

    @NotNull(message = "ошибка валидации, endDate не может быть null", groups = {OnCreate.class})
    @FutureOrPresent(message = "ошибка валидации, endDate не может быть в прошлом", groups = {OnCreate.class})
    private LocalDate endDate;

    @NotNull(message = "ошибка валидации, itemId не может быть null", groups = {OnCreate.class})
    @Positive(message = "ошибка валидации, itemId должно быть положительным числом",
            groups = {OnCreate.class, OnUpdate.class})
    private Long itemId;

    @NotNull(message = "ошибка валидации, bookerId не может быть null", groups = {OnCreate.class})
    @Positive(message = "ошибка валидации, bookerId должно быть положительным числом",
            groups = {OnCreate.class, OnUpdate.class})
    private Long bookerId;

    @NotNull(message = "ошибка валидации, status не может быть null", groups = {OnCreate.class})
    private Status status;
}
