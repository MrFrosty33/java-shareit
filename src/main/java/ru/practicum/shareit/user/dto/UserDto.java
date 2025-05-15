package ru.practicum.shareit.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true)
public class UserDto {
    @NotBlank(message = "ошибка валидации, name не может быть null / Blank")
    @Size(max = 100, message = "ошшибка валидации, длина name не может превышать 100 символов")
    private String name;

    @Email(message = "ошибка валидации, email должен быть действительным")
    private String email;
}
