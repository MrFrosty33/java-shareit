package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

public interface ItemService {
    ItemDto get(Long itemId, Long userId);

    List<ItemDto> getAllItemsByUserId(Long userId);

    List<ItemDto> search(String text, Long userId);

    ItemDto save(ItemDto item, Long userId);

    ItemDto update(ItemDto item, Long itemId, Long userId);

    boolean delete(Long itemId, Long userId);

    boolean deleteAll();

    void validateItemExists(Long id);
}
