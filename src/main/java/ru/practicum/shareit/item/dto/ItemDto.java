package ru.practicum.shareit.item.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.item.Availability;

@Data
@Builder(toBuilder = true)
public class ItemDto {
    @NotBlank(message = "ошибка валидации, name не может быть null / Blank")
    @Size(max = 100, message = "ошибка валидации, длина name не может превышать 100 символов")
    private String name;

    @NotBlank(message = "ошибка валидации, description не может быть null / Blank")
    @Size(max = 1000, message = "ошибка валидации, длина description не может превышать 1000 символов")
    private String description;

    @Positive(message = "ошибка валидации, ownerId должно быть положительным числом")
    private Long ownerId;
    @Positive(message = "ошибка валидации, requestId должно быть положительным числом")
    private Long requestId;

    @NotNull(message = "ошибка валидации, availability не может быть null")
    private Availability availability;
}
