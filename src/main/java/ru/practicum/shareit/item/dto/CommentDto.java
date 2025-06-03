package ru.practicum.shareit.item.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

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

    @NotNull(message = "ошибка валидации, userId не может быть Null")
    @Positive(message = "ошибка валидации, userId должно быть положительным числом")
    private Long authorId;

    @Builder.Default
    private LocalDate created = LocalDate.now();
}
