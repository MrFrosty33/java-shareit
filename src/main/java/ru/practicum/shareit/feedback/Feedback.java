package ru.practicum.shareit.feedback;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true)
public class Feedback {
    @NotNull(message = "ошибка валидации, id не может быть null")
    private Long id;

    @NotNull(message = "ошибка валидации, ownerId не может быть null")
    private Long ownerId;

    @NotNull(message = "ошибка валидации, renterId не может быть null")
    private Long renterId;

    @NotBlank(message = "ошибка валидации, comment не может быть null / Blank")
    @Size(max = 1000, message = "ошибка валидации, длина comment не может превышать 1000 символов")
    private String comment;
}
