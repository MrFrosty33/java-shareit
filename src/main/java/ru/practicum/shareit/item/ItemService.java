package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

public interface ItemService {
    Item get(Long itemId, Long userId);

    List<Item> getAllItemsByUserId(Long userId);

    List<Item> search(String text, Long userId);

    Item save(ItemDto item, Long userId);

    Item update(ItemDto item, Long itemId, Long userId);

    boolean delete(Long itemId, Long userId);

    boolean deleteAll();

    void validateItemExists(Long id);
}
