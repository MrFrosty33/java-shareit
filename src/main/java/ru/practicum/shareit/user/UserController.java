package ru.practicum.shareit.user;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

@RestController
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@RequestMapping(path = "/users")
public class UserController {
    private final UserService userService;

    @GetMapping("/{id}")
    public UserDto get(@PathVariable @NotNull @Positive Long id) {
        return userService.get(id);
    }

    @GetMapping
    public List<UserDto> getAll() {
        return userService.getAll();
    }

    @PostMapping
    public UserDto save(@Valid @RequestBody User user) {
        return userService.save(user);
    }

    @PatchMapping
    public UserDto update(@Valid @RequestBody User user) {
        return userService.update(user);
    }

    @DeleteMapping("{id}")
    public String delete(@PathVariable @NotNull @Positive Long id) {
        userService.delete(id);
        return "Ok";
    }

    @DeleteMapping
    public String deleteAll() {
        userService.deleteAll();
        return "Ok";
    }
}
