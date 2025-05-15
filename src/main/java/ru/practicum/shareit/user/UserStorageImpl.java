package ru.practicum.shareit.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.NotFoundException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
@Slf4j
public class UserStorageImpl implements UserStorage {
    private final Map<Long, User> inMemoryStorage = new HashMap<>();

    @Override
    public Long nextId() {
        return inMemoryStorage.size() + 1L;
    }

    @Override
    public User get(Long id) {
        validateExists(id);
        log.info("Получен User с id: {}", id);
        return inMemoryStorage.get(id);
    }

    @Override
    public List<User> getAll() {
        log.info("Получен список всех User");
        return inMemoryStorage.values().stream().toList();
    }

    @Override
    public User save(User user) {
        Long id = user.getId();

        inMemoryStorage.put(id, user);
        log.info("Сохранён User с id: {}", id);
        validateExists(id); // проверка, верно ли сохранился
        return user;
    }

    @Override
    public User update(User user) {
        Long id = user.getId();

        validateExists(user.getId());
        inMemoryStorage.replace(id, user);
        log.info("Обновлён User с id: {}", id);
        validateExists(id);
        return user;
    }

    @Override
    public boolean delete(Long id) {
        validateExists(id);
        inMemoryStorage.remove(id);
        log.info("Удалён User с id: {}", id);
        return true;
    }

    @Override
    public boolean deleteAll() {
        inMemoryStorage.clear();
        log.info("Очищено хранилище User");
        return true;
    }

    @Override
    public void validateExists(Long id) {
        if (!inMemoryStorage.containsKey(id)) {
            log.info("User с id: {} ", id);
            throw new NotFoundException("User с id: " + id + " не найден");
        }
    }
}
