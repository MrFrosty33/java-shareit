package ru.practicum.shareit.feedback;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

//TODO на подумать:

// Что есть отзыв, в какой момент он оставляется и каким образом?
// Получается ли он в контроллере предмета при возвращении? Или ему нужен свой контроллер?
// Нужна ли отдельная сущность под него вообще, или просто переменная у предмета / пользователя?
@Data
@Builder(toBuilder = true)
public class Feedback {
    private Long id;

    @NotNull(message = "ошибка валидации, ownerId не может быть null")
    private Long ownerId;

    @NotNull(message = "ошибка валидации, renterId не может быть null")
    private Long renterId;

    @NotBlank(message = "ошибка валидации, comment не может быть null / Blank")
    @Size(max = 1000, message = "ошибка валидации, длина comment не может превышать 1000 символов")
    private String comment;
}
