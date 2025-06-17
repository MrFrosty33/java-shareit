package ru.practicum.server.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;
import ru.practicum.server.markers.OnCreate;
import ru.practicum.server.markers.OnUpdate;

@Data
@Builder(toBuilder = true)
public class UserDto {
    private Long id;

    @NotNull(message = "ошибка валидации, name не может быть Null", groups = {OnCreate.class})
    @NotBlank(message = "ошибка валидации, name не может быть Blank", groups = {OnCreate.class})
    @Size(max = 100, message = "ошибка валидации, длина name не может превышать 100 символов",
            groups = {OnCreate.class, OnUpdate.class})
    private String name;

    @NotNull(message = "ошибка валидации, email не может быть Null", groups = {OnCreate.class})
    @NotBlank(message = "ошибка валидации, name не может быть Blank", groups = {OnCreate.class})
    @Email(message = "ошибка валидации, email должен быть действительным",
            groups = {OnCreate.class, OnUpdate.class})
    @Size(max = 100, message = "ошибка валидации, длина email не может превышать 100 символов",
            groups = {OnCreate.class, OnUpdate.class})
    private String email;
}
