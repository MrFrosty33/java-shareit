package ru.practicum.shareit.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder(toBuilder = true)
public class ItemRequest {
    @Positive(message = "ошибка валидации, id должно быть положительным числом")
    private Long id;

    @NotBlank(message = "ошибка валидации, description не может быть null / Blank")
    @Size(max = 1000, message = "ошшибка валидации, длина description не может превышать 1000 символов")
    private String description;

    @Positive(message = "ошибка валидации, requesterId должно быть положительным числом")
    private Long requesterId;

    private LocalDate created;
}
