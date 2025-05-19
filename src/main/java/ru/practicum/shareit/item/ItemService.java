package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

public interface ItemService {
    ItemDto get(Long id, Long userId);

    List<ItemDto> getAll();

    List<ItemDto> search(String text);

    ItemDto save(Item item, Long userId);

    ItemDto update(Item item, Long userId);

    boolean delete(Long id, Long userId);

    boolean deleteAll();
}
