package ru.practicum.shareit.item.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true)
public class CommentDto {
    private Long id;

    @NotNull(message = "ошибка валидации, text не может быть Null")
    @NotBlank(message = "ошибка валидации, text не может быть Blank")
    @Size(max = 1000, message = "ошибка валидации, длина text не может превышать 1000 символов")
    private String text;

    @NotNull(message = "ошибка валидации, itemId не может быть Null")
    @Positive(message = "ошибка валидации, itemId должно быть положительным числом")
    private Long itemId;
}
