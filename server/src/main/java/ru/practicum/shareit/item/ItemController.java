package ru.practicum.shareit.item;

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
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.markers.OnCreate;
import ru.practicum.shareit.markers.OnUpdate;

import java.util.List;

@RestController
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@RequestMapping("/items")
@Validated
public class ItemController {
    private final ItemService itemService;

    @GetMapping("/{itemId}")
    public ItemDto get(@PathVariable
                           @NotNull(message = "ошибка валидации, id не может быть null")
                           @Positive(message = "ошибка валидации, id должно быть положительным числом")
                           Long itemId,
                       @RequestHeader("X-Sharer-User-Id")
                           @Positive(message = "ошибка валидации, userId должно быть положительным числом")
                           Long userId) {
        return itemService.get(itemId, userId);
    }

    @GetMapping
    public List<ItemDto> getAllItemsByUserId(@RequestHeader("X-Sharer-User-Id")
                                                 @NotNull(message = "ошибка валидации, userId не может быть null")
                                                 @Positive(message = "ошибка валидации, userId должно быть положительным числом")
                                                 Long userId) {
        return itemService.getAllItemsByUserId(userId);
    }

    @GetMapping("/search")
    public List<ItemDto> search(@RequestParam(name = "text")
                                String text,
                                @RequestHeader("X-Sharer-User-Id")
                                @Positive(message = "ошибка валидации, userId должно быть положительным числом")
                                Long userId) {
        return itemService.search(text, userId);
    }

    @PostMapping
    public ItemDto save(@Validated(OnCreate.class) @RequestBody ItemDto itemDto,
                        @RequestHeader("X-Sharer-User-Id")
                        @Positive(message = "ошибка валидации, userId должно быть положительным числом")
                        Long userId) {
        return itemService.save(itemDto, userId);
    }

    // нужен ли тут RequestHeader, или же id автора берём из полученного Dto объекта?
    @PostMapping("/{itemId}/comment")
    public CommentDto addComment(@Validated @RequestBody CommentDto commentDto,
                                 @PathVariable
                                 @NotNull(message = "ошибка валидации, itemId не может быть null")
                                 @Positive(message = "ошибка валидации, itemId должно быть положительным числом")
                                 Long itemId,
                                 @RequestHeader("X-Sharer-User-Id")
                                 @Positive(message = "ошибка валидации, userId должно быть положительным числом") Long userId) {
        return itemService.addComment(commentDto, itemId, userId);
    }

    @PatchMapping("/{itemId}")
    public ItemDto update(@Validated(OnUpdate.class) @RequestBody ItemDto itemDto,
                          @PathVariable
                          @NotNull(message = "ошибка валидации, itemId не может быть null")
                          @Positive(message = "ошибка валидации, itemId должно быть положительным числом")
                          Long itemId,
                          @RequestHeader("X-Sharer-User-Id")
                              @Positive(message = "ошибка валидации, userId должно быть положительным числом")
                              Long userId) {
        return itemService.update(itemDto, itemId, userId);
    }

    @DeleteMapping("/{itemId}")
    public void delete(@PathVariable
                           @NotNull(message = "ошибка валидации, id не может быть null")
                           @Positive(message = "ошибка валидации, id должно быть положительным числом")
                           Long itemId,
                       @RequestHeader("X-Sharer-User-Id")
                           @NotNull(message = "ошибка валидации, userId не может быть null")
                           @Positive(message = "ошибка валидации, userId должно быть положительным числом")
                           Long userId) {
        itemService.delete(itemId, userId);
    }

    @DeleteMapping
    public void deleteAll() {
        itemService.deleteAll();
    }
}
