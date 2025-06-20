package ru.practicum.server.item;

import ru.practicum.models.item.CommentDto;
import ru.practicum.models.item.ItemDto;

import java.util.List;

public interface ItemService {
    ItemDto get(Long itemId, Long userId);

    List<ItemDto> getAllItemsByUserId(Long userId);

    Boolean isItemAvailable(Long itemId);

    List<ItemDto> search(String text, Long userId);

    ItemDto save(ItemDto item, Long userId);

    CommentDto addComment(CommentDto commentDto, Long itemId, Long userId);

    ItemDto update(ItemDto item, Long itemId, Long userId);

    void delete(Long itemId, Long userId);

    void deleteAll();
}
