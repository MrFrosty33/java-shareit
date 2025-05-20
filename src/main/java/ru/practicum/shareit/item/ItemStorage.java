package ru.practicum.shareit.item;

import java.util.List;
import java.util.Set;

public interface ItemStorage {
    Set<Long> getIds();
    Item get(Long id);

    List<Item> getAll();

    Item save(Item item);

    Item update(Item item);

    boolean delete(Long id);

    boolean deleteAll();
}
