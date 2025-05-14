package ru.practicum.shareit.item;

import java.util.List;

public interface ItemStorage {
    Item get(Long id);

    List<Item> getAll();

    Item save(Item item);

    Item update(Item item);

    boolean delete(Long id);

    boolean deleteAll();

}
