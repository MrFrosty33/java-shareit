package ru.practicum.shareit.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder(toBuilder = true)
public class UserDto {
    @NotBlank(message = "ошибка валидации, name не может быть null / Blank")
    @Size(max = 100, message = "ошшибка валидации, длина name не может превышать 100 символов")
    private String name;

    // DTO должен иметь айдишники вещей, или может непосредственно объекты?
    @NotNull(message = "ошибка валидации, items не может быть null")
    private List<Long> items;
}
