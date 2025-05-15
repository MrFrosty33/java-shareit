package ru.practicum.shareit.booking.dto;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.booking.Status;

import java.time.LocalDate;

@Data
@Builder(toBuilder = true)
public class BookingDto {
    @NotNull(message = "ошибка валидации, startDate не может быть null")
    @FutureOrPresent(message = "ошибка валидации, startDate не может быть в прошлом")
    private LocalDate startDate;

    @NotNull(message = "ошибка валидации, endDate не может быть null")
    @FutureOrPresent(message = "ошибка валидации, endDate не может быть в прошлом")
    private LocalDate endDate;

    @NotNull(message = "ошибка валидации, itemId не может быть null")
    @Positive(message = "ошибка валидации, itemId должно быть положительным числом")
    private Long itemId;

    @NotNull(message = "ошибка валидации, bookerId не может быть null")
    @Positive(message = "ошибка валидации, bookerId должно быть положительным числом")
    private Long bookerId;

    @NotNull(message = "ошибка валидации, status не может быть null")
    private Status status;
}
