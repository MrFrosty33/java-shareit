package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.NotFoundException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
@Slf4j

public class ItemStorageImpl implements ItemStorage {
    private final Map<Long, Item> inMemoryStorage = new HashMap<>();
    private Long nextId = 1L;

    @Override
    public Item get(Long id) {
        validateExists(id);
        log.info("Получен Item с id: {}", id);
        return inMemoryStorage.get(id);
    }

    @Override
    public List<Item> getAll() {
        log.info("Получен список всех Item");
        return inMemoryStorage.values().stream().toList();
    }

    @Override
    public Item save(Item item) {
        inMemoryStorage.put(nextId, item);
        log.info("Сохранён Item с id: {}", nextId);
        validateExists(nextId); // проверка, верно ли сохранился
        nextId++;
        return item;
    }

    @Override
    public Item update(Item item) {
        Item updatedItem = inMemoryStorage.get(item.getId());
        Long id = item.getId();

        validateExists(id);
        if (item.getName() != null && !item.getName().equals(updatedItem.getName())) {
            updatedItem.setName(item.getName());
        }
        if (item.getDescription() != null && !item.getDescription().equals(updatedItem.getDescription())) {
            updatedItem.setDescription(item.getDescription());
        }
        if (item.getAvailability() != null && !item.getAvailability().equals(updatedItem.getAvailability())) {
            updatedItem.setAvailability(item.getAvailability());
        }

        inMemoryStorage.replace(id, updatedItem);
        log.info("Обновлён Item с id: {}", id);
        validateExists(id);
        return updatedItem;
    }

    @Override
    public boolean delete(Long id) {
        validateExists(id);
        inMemoryStorage.remove(id);
        log.info("Удалён Item с id: {}", id);
        nextId--;
        return true;
    }

    @Override
    public boolean deleteAll() {
        inMemoryStorage.clear();
        log.info("Очищено хранилище Item");
        nextId = 1L;
        return true;
    }

    @Override
    public void validateExists(Long id) {
        if (!inMemoryStorage.containsKey(id)) {
            log.info("Попытка найти Item с id: {}", id);
            throw new NotFoundException("Item с id: " + id + " не найден");
        }
    }
}
