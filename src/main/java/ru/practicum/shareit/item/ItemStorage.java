package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

public interface ItemStorage {
    Long nextId();

    Item get(Long id);

    List<Item> getAll();


    Item save(Item item);

    Item update(Item item);

    boolean delete(Long id);

    boolean deleteAll();

    void validateExists(Long id);
}
