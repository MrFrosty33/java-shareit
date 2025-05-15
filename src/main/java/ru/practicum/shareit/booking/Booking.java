package ru.practicum.shareit.booking;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder(toBuilder = true)
public class Booking {
    private Long id;

    @NotNull(message = "ошибка валидации, startDate не может быть null")
    @FutureOrPresent(message = "ошибка валидации, startDate не может быть в прошлом")
    private LocalDate startDate;

    @NotNull(message = "ошибка валидации, endDate не может быть null")
    @FutureOrPresent(message = "ошибка валидации, endDate не может быть в прошлом")
    private LocalDate endDate;

    @NotNull(message = "ошибка валидации, status не может быть null")
    private Status status;

    @NotNull(message = "ошибка валидации, ownerId не может быть null")
    private Long ownerId;

    @NotNull(message = "ошибка валидации, renterId не может быть null")
    private Long renterId;

}
