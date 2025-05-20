package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

public interface ItemService {
    ItemDto get(Long id, Long userId);

    List<ItemDto> getAll();

    List<ItemDto> search(String text);

    ItemDto save(ItemDto item, Long userId);

    ItemDto update(ItemDto item, Long itemId, Long userId);

    boolean delete(Long id, Long userId);

    boolean deleteAll();
}
