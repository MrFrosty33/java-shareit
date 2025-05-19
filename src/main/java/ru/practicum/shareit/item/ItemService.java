package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

public interface ItemService {
    ItemDto get(Long id);

    List<ItemDto> getAll();

    List<ItemDto> search(String text);

    ItemDto save(ItemDto itemDto, Long userId);

    ItemDto update(Item item);

    boolean delete(Long id);

    boolean deleteAll();
}
