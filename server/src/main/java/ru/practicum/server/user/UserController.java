package ru.practicum.server.user;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.models.markers.OnCreate;
import ru.practicum.models.markers.OnUpdate;
import ru.practicum.models.user.UserDto;

import java.util.List;

@RestController
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@RequestMapping(path = "/users")
public class UserController {
    private final UserService userService;

    @GetMapping("/{id}")
    public UserDto get(@PathVariable
                       @NotNull(message = "ошибка валидации, id не может быть null")
                       @Positive(message = "ошибка валидации, id должно быть положительным числом")
                       Long id) {
        return userService.get(id);
    }

    @GetMapping
    public List<UserDto> getAll() {
        return userService.getAll();
    }

    @PostMapping
    public UserDto save(@Validated(OnCreate.class) @RequestBody UserDto userDto) {
        return userService.save(userDto);
    }

    @PatchMapping("/{id}")
    public UserDto update(@Validated(OnUpdate.class) @RequestBody UserDto userDto,
                          @PathVariable
                          @NotNull(message = "ошибка валидации, id не может быть null")
                          @Positive(message = "ошибка валидации, id должно быть положительным числом")
                          Long id) {
        return userService.update(userDto, id);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable
                       @NotNull(message = "ошибка валидации, id не может быть null")
                       @Positive(message = "ошибка валидации, id должно быть положительным числом")
                       Long id) {
        userService.delete(id);
    }

    @DeleteMapping
    public void deleteAll() {
        userService.deleteAll();
    }
}