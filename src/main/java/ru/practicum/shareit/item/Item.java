package ru.practicum.shareit.item;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Data
@Builder(toBuilder = true)
public class Item {
    @NotNull(message = "ошибка валидации, id не может быть null")
    @Positive(message = "ошибка валидации, id должно быть положительным числом")
    private Long id;
    @NotNull(message = "ошибка валидации, name не может быть null")
    @NotBlank(message = "ошибка валидации, name не может быть Blank")
    private String name;

    @NotNull(message = "ошибка валидации, description не может быть null")
    @NotBlank(message = "ошибка валидации, description не может быть Blank")
    private String description;

    @Positive(message = "ошибка валидации, ownerId должно быть положительным числом")
    private Long ownerId;
    @Positive(message = "ошибка валидации, requestId должно быть положительным числом")
    private Long requestId;

    @NotNull(message = "ошибка валидации, available не может быть null")
    private Boolean available;

    @Getter
    private Long rentalCount = 0L;

    public void increaseRentalCount() {
        rentalCount++;
    }
}
