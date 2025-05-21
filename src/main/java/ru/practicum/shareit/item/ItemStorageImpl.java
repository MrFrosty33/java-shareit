package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Repository
@RequiredArgsConstructor
public class ItemStorageImpl implements ItemStorage {
    private final Map<Long, Item> inMemoryStorage = new HashMap<>();
    private Long nextId = 1L;

    @Override
    public Set<Long> getIds() {
        return inMemoryStorage.keySet();
    }

    @Override
    public Item get(Long id) {
        return inMemoryStorage.get(id);
    }

    @Override
    public List<Item> getAll() {
        return inMemoryStorage.values().stream().toList();
    }

    @Override
    public Item save(Item item) {
        Long id = nextId++;
        item.setId(id);
        inMemoryStorage.put(id, item);
        return inMemoryStorage.get(id);
    }

    @Override
    public Item update(Item item) {
        Item updatedItem = inMemoryStorage.get(item.getId());
        Long id = item.getId();

        if (item.getName() != null && !item.getName().equals(updatedItem.getName())) {
            updatedItem.setName(item.getName());
        }
        if (item.getDescription() != null && !item.getDescription().equals(updatedItem.getDescription())) {
            updatedItem.setDescription(item.getDescription());
        }
        if (item.getAvailable() != updatedItem.getAvailable()) {
            updatedItem.setAvailable(item.getAvailable());
        }

        inMemoryStorage.replace(id, updatedItem);
        return inMemoryStorage.get(id);
    }

    @Override
    public boolean delete(Long id) {
        inMemoryStorage.remove(id);
        nextId--;
        return true;
    }

    @Override
    public boolean deleteAll() {
        inMemoryStorage.clear();
        nextId = 1L;
        return true;
    }
}
