package ru.practicum.shareit.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.item.Availability;

import java.util.Map;

// получается, что User является и владельцем, и потенциальным арендатором?
// может не иметь вещей, а только арендовывать, так?
@Data
@Builder(toBuilder = true)
public class User {
    private Long id;

    @NotBlank(message = "ошибка валидации, name не может быть null / Blank")
    @Size(max = 100, message = "ошшибка валидации, длина name не может превышать 100 символов")
    private String name;

    @NotBlank(message = "ошибка валидации, login не может быть null / Blank")
    @Size(max = 100, message = "ошшибка валидации, длина login не может превышать 100 символов")
    private String login;

    @Email(message = "ошибка валидации, email должен быть действительным")
    private String email;

    // таблица с его предметами. Может стоит сохранять не id, а непосредственно предмет?
    // Или излишняя нагрузка?
    @NotNull(message = "ошибка валидации, items не может быть null")
    private Map<Long, Availability> items;
}
