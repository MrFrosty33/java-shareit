package ru.practicum.shareit.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true)
public class User {
    @NotNull(message = "ошибка валидации, id не может быть null")
    @Positive(message = "ошибка валидации, id должно быть положительным числом")
    private Long id;

    @NotNull(message = "ошибка валидации, name не может быть null")
    @NotBlank(message = "ошибка валидации, name не может быть Blank")
    private String name;

    @NotNull(message = "ошибка валидации, email не может быть null")
    @NotBlank(message = "ошибка валидации, email не может быть Blank")
    @Email(message = "ошибка валидации, email должен быть действительным")
    private String email;
}
