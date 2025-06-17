package ru.practicum.server.item.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;
import ru.practicum.server.markers.OnCreate;
import ru.practicum.server.markers.OnUpdate;

import java.util.List;

@Data
@Builder(toBuilder = true)
public class ItemDto {
    private Long id;

    @NotNull(message = "ошибка валидации, name не может быть Null", groups = {OnCreate.class})
    @NotBlank(message = "ошибка валидации, name не может быть Blank", groups = {OnCreate.class})
    @Size(max = 100, message = "ошибка валидации, длина name не может превышать 100 символов",
            groups = {OnCreate.class, OnUpdate.class})
    private String name;

    @NotNull(message = "ошибка валидации, name не может быть Null", groups = {OnCreate.class})
    @NotBlank(message = "ошибка валидации, description не может быть Blank", groups = {OnCreate.class})
    @Size(max = 1000, message = "ошибка валидации, длина description не может превышать 1000 символов",
            groups = {OnCreate.class, OnUpdate.class})
    private String description;

    @Positive(message = "ошибка валидации, ownerId должно быть положительным числом",
            groups = {OnCreate.class, OnUpdate.class})
    private Long ownerId;

    @Positive(message = "ошибка валидации, requestId должно быть положительным числом",
            groups = {OnCreate.class, OnUpdate.class})
    private Long requestId;

    // нужно для тестов, но не требовалось реализовывать вроде пока
    private Long lastBooking;
    private Long nextBooking;

    private List<CommentDto> comments;

    @NotNull(message = "ошибка валидации, available не может быть null", groups = {OnCreate.class})
    private Boolean available;
}
