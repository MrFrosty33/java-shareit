package ru.practicum.shareit.request.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.markers.OnCreate;
import ru.practicum.shareit.markers.OnUpdate;

import java.time.LocalDate;

@Data
@Builder(toBuilder = true)
public class ItemRequestDto {
    private Long id;

    @NotNull(message = "ошибка валидации, description не может быть Null", groups = {OnCreate.class})
    @NotBlank(message = "ошибка валидации, description не может быть null / Blank",
            groups = {OnCreate.class, OnUpdate.class})
    @Size(max = 1000, message = "ошибка валидации, длина description не может превышать 1000 символов",
            groups = {OnCreate.class, OnUpdate.class})
    private String description;

    @Positive(message = "ошибка валидации, requesterId должно быть положительным числом",
            groups = {OnCreate.class, OnUpdate.class})
    private Long requesterId;

    private LocalDate created;
}
