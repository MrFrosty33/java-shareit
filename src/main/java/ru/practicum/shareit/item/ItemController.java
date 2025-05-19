package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
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
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

@RestController
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@RequestMapping("/items")
public class ItemController {
    private final ItemService itemService;

    @GetMapping("/{id}")
    public ItemDto get(@PathVariable
                       @NotNull(message = "ошибка валидации, id не может быть null")
                       @Positive(message = "ошибка валидации, id должно быть положительным числом")
                           Long id,
                       @RequestHeader("X-Sharer-User-Id")
                           @NotNull(message = "ошибка валидации, userId не может быть null")
                           @Positive(message = "ошибка валидации, userId должно быть положительным числом")
                           Long userId) {
        return itemService.get(id, userId);
    }

    @GetMapping
    public List<ItemDto> getAll() {
        return itemService.getAll();
    }

    @GetMapping("/search")
    public List<ItemDto> search(@RequestParam
                                @NotBlank(message = "ошибка валидации, userId не может быть null / Blank")
                                String text) {
        return itemService.search(text);
    }

    @PostMapping
    public ItemDto save(@Valid @RequestBody Item item,
                        @RequestHeader("X-Sharer-User-Id")
                        @NotNull(message = "ошибка валидации, userId не может быть null")
                        @Positive(message = "ошибка валидации, userId должно быть положительным числом")
                        Long userId) {
        return itemService.save(item, userId);
    }

    @PatchMapping
    public ItemDto update(@Valid @RequestBody Item item,
                          @RequestHeader("X-Sharer-User-Id")
                          @NotNull(message = "ошибка валидации, userId не может быть null")
                          @Positive(message = "ошибка валидации, userId должно быть положительным числом")
                          Long userId) {
        //TODO редактировать может только владелец вещи
        return itemService.update(item, userId);
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable
                         @NotNull(message = "ошибка валидации, id не может быть null")
                         @Positive(message = "ошибка валидации, id должно быть положительным числом")
                             Long id,
                         @RequestHeader("X-Sharer-User-Id")
                             @NotNull(message = "ошибка валидации, userId не может быть null")
                             @Positive(message = "ошибка валидации, userId должно быть положительным числом")
                             Long userId) {
        itemService.delete(id, userId);
        return "Ok";
    }

    @DeleteMapping
    public String deleteAll() {
        itemService.deleteAll();
        return "Ok";
    }

}
