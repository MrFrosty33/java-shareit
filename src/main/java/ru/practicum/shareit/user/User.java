package ru.practicum.shareit.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true)
public class User {
    //TODO у объектов БД @NotNull на поля?

    @Positive(message = "ошибка валидации, id должно быть положительным числом")
    private Long id;

    @NotBlank(message = "ошибка валидации, name не может быть null / Blank")
    @Size(max = 100, message = "ошшибка валидации, длина name не может превышать 100 символов")
    private String name;

    @NotBlank(message = "ошибка валидации, email не может быть null / Blank")
    @Email(message = "ошибка валидации, email должен быть действительным")
    private String email;
}
