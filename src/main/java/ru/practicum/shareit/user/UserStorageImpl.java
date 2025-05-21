package ru.practicum.shareit.user;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Repository
public class UserStorageImpl implements UserStorage {
    private final Map<Long, User> inMemoryStorage = new HashMap<>();
    private Long nextId = 1L;

    // чтобы вынести логику валидацию в класс сервис
    @Override
    public Set<Long> getIds() {
        return inMemoryStorage.keySet();
    }

    @Override
    public User get(Long id) {
        return inMemoryStorage.get(id);
    }

    @Override
    public List<User> getAll() {
        return inMemoryStorage.values().stream().toList();
    }

    @Override
    public User save(User user) {
        Long id = nextId++;
        user.setId(id);
        inMemoryStorage.put(id, user);
        return inMemoryStorage.get(id);
    }

    @Override
    public User update(UserDto userDto, Long id) {
        User updatedUser = inMemoryStorage.get(id);

        if (userDto.getName() != null && !userDto.getName().equals(updatedUser.getName())) {
            updatedUser.setName(userDto.getName());
        }
        if (userDto.getEmail() != null && !userDto.getEmail().equals(updatedUser.getEmail())) {
            updatedUser.setEmail(userDto.getEmail());
        }

        inMemoryStorage.replace(id, updatedUser);
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
