package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemUpdate;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

@RestController
@RequestMapping("/items")
public class ItemController {

    @GetMapping("/{id}")
    public UserDto get(@PathVariable @NotNull @Positive Long id) {
        return null;
    }

    @GetMapping
    public List<UserDto> getAll() {
        return null;
    }

    @PostMapping
    public UserDto save(@Valid @RequestBody ItemDto itemDto,
                        @RequestHeader("X-Sharer-User-Id") @NotNull @Positive Long userId) {
        return null;
    }

    @PatchMapping
    public UserDto update(@Valid @RequestBody ItemUpdate item) {
        //TODO редактировать может только владелец вещи
        return null;
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable @NotNull @Positive Long id) {
        return "Ok";
    }

    @DeleteMapping
    public String deleteAll() {
        return "Ok";
    }

}
