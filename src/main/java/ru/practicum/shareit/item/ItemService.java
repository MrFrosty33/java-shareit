package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;

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

    void validateItemExists(Long id);
}
