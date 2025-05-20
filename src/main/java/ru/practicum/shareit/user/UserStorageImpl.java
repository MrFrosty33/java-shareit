package ru.practicum.shareit.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.ConflictException;
import ru.practicum.shareit.exception.NotFoundException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
@Slf4j
public class UserStorageImpl implements UserStorage {
    private final Map<Long, User> inMemoryStorage = new HashMap<>();
    private Long nextId = 1L;

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
        validateUniqueEmail(user.getEmail());
        Long id = nextId++;
        user.setId(id);

        inMemoryStorage.put(id, user);
        log.info("Сохранён User с id: {}", id);
        validateExists(id); // проверка, верно ли сохранился
        return user;
    }

    @Override
    public User update(User user) {
        Long id = user.getId();
        validateExists(id);
        User updatedUser = inMemoryStorage.get(id);

        if (user.getName() != null && !user.getName().equals(updatedUser.getName())) {
            updatedUser.setName(user.getName());
        }
        if (user.getEmail() != null && !user.getEmail().equals(updatedUser.getEmail())) {
            updatedUser.setEmail(user.getEmail());
        }

        inMemoryStorage.replace(id, updatedUser);
        log.info("Обновлён User с id: {}", id);
        validateExists(id);
        return updatedUser;
    }

    @Override
    public boolean delete(Long id) {
        validateExists(id);
        inMemoryStorage.remove(id);
        log.info("Удалён User с id: {}", id);
        nextId--;
        return true;
    }

    @Override
    public boolean deleteAll() {
        inMemoryStorage.clear();
        log.info("Очищено хранилище User");
        nextId = 1L;
        return true;
    }

    @Override
    public void validateExists(Long id) {
        if (!inMemoryStorage.containsKey(id)) {
            log.info("Попытка найти User с id: {}", id);
            throw new NotFoundException("User с id: " + id + " не найден");
        }
    }

    @Override
    public void validateUniqueEmail(String email) {
        if (inMemoryStorage.values().stream().anyMatch(user -> user.getEmail().equals(email))) {
            log.info("Попытка добавить нового пользователя с уже привязанным email: {}", email);
            throw new ConflictException("Email: " + email + " уже привязан");
        }
    }
}
