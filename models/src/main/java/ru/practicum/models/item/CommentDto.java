package ru.practicum.models.item;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class CommentDto {
    private Long id;

    //    @NotNull(message = "ошибка валидации, text не может быть Null")
//    @NotBlank(message = "ошибка валидации, text не может быть Blank")
    @Size(max = 1000, message = "ошибка валидации, длина text не может превышать 1000 символов")
    private String text;

    @Positive(message = "ошибка валидации, userId должно быть положительным числом")
    private String authorName;

    private LocalDateTime created;
}
