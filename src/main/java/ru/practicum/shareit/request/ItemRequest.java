package ru.practicum.shareit.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true)
public class ItemRequest {
    private Long id;

    @NotBlank(message = "ошибка валидации, itemName не может быть null / Blank")
    @Size(max = 100, message = "ошшибка валидации, длина itemName не может превышать 100 символов")
    private String itemName;
}
