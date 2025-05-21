package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.ConflictException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserStorage userStorage;
    private final UserMapper userMapper;

    @Override
    public UserDto get(Long id) {
        validateUserExists(id);
        UserDto result = userMapper.toDto(userStorage.get(id));
        log.info("Получен User с id: {}", id);
        return result;
    }

    @Override
    public List<UserDto> getAll() {
        List<UserDto> result = userStorage.getAll().stream().map(userMapper::toDto).toList();
        log.info("Получен список всех User");
        return result;
    }

    @Override
    public UserDto save(UserDto userDto) {
        validateUniqueEmail(userDto.getEmail());
        UserDto result = userMapper.toDto(userStorage.save(userMapper.fromDto(userDto)));
        log.info("Сохранён User с id: {}", result.getId());
        return result;
    }

    @Override
    public UserDto update(UserDto userDto, Long id) {
        validateUserExists(id);
        validateUniqueEmail(userDto.getEmail());
        UserDto result = userMapper.toDto(userStorage.update(userDto, id));
        log.info("Обновлён User с id: {}", id);
        return result;
    }

    @Override
    public boolean delete(Long id) {
        validateUserExists(id);
        boolean result = userStorage.delete(id);
        log.info("Удалён User с id: {}", id);
        return result;
    }

    @Override
    public boolean deleteAll() {
        boolean result = userStorage.deleteAll();
        log.info("Очищено хранилище User");
        return result;
    }

    // при подключении БД в будущем наверно стоит вынести в отдельный класс валидатор?
    @Override
    public void validateUserExists(Long id) {
        if (!userStorage.getIds().contains(id)) {
            log.info("Попытка найти несуществующего User с id: {}", id);
            throw new NotFoundException("User с id: " + id + " не найден");
        }
    }

    private void validateUniqueEmail(String email) {
        if (userStorage.getAll().stream().anyMatch(user -> user.getEmail().equals(email))) {
            log.info("Попытка добавить нового пользователя с уже привязанным email: {}", email);
            throw new ConflictException("Email: " + email + " уже привязан");
        }
    }
}
