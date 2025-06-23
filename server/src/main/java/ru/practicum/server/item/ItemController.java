package ru.practicum.server.item;

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
import ru.practicum.models.item.CommentDto;
import ru.practicum.models.item.ItemDto;

import java.util.List;

@RestController
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@RequestMapping("/items")
public class ItemController {
    private final ItemService itemService;

    @GetMapping("/{itemId}")
    public ItemDto get(@PathVariable
                           Long itemId,
                       @RequestHeader("X-Sharer-User-Id")
                           Long userId) {
        return itemService.get(itemId, userId);
    }

    @GetMapping
    public List<ItemDto> getAllItemsByUserId(@RequestHeader("X-Sharer-User-Id") Long userId) {
        return itemService.getAllItemsByUserId(userId);
    }

    @GetMapping("/search")
    public List<ItemDto> search(@RequestParam(name = "text")
                                String text,
                                @RequestHeader("X-Sharer-User-Id")
                                Long userId) {
        return itemService.search(text, userId);
    }

    @PostMapping
    public ItemDto save(@RequestBody
                        ItemDto itemDto,
                        @RequestHeader("X-Sharer-User-Id")
                        Long userId) {
        return itemService.save(itemDto, userId);
    }

    // нужен ли тут RequestHeader, или же id автора берём из полученного Dto объекта?
    @PostMapping("/{itemId}/comment")
    public CommentDto addComment(@RequestBody
                                 CommentDto commentDto,
                                 @PathVariable
                                 Long itemId,
                                 @RequestHeader("X-Sharer-User-Id")
                                 Long userId) {
        return itemService.addComment(commentDto, itemId, userId);
    }

    @PatchMapping("/{itemId}")
    public ItemDto update(@RequestBody
                          ItemDto itemDto,
                          @PathVariable
                          Long itemId,
                          @RequestHeader("X-Sharer-User-Id")
                          Long userId) {
        return itemService.update(itemDto, itemId, userId);
    }

    @DeleteMapping("/{itemId}")
    public void delete(@PathVariable
                           Long itemId,
                       @RequestHeader("X-Sharer-User-Id")
                           Long userId) {
        itemService.delete(itemId, userId);
    }

    @DeleteMapping
    public void deleteAll() {
        itemService.deleteAll();
    }
}
