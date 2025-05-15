package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
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
        UserDto result = userMapper.toDto(userStorage.get(id));
        log.info("Результат получения User по id был приведён в UserDto объект");
        return result;
    }

    @Override
    public List<UserDto> getAll() {
        List<UserDto> result = userStorage.getAll().stream().map(userMapper::toDto).toList();
        log.info("Результат полечения всех User был приведён в список UserDto объектов");
        return result;
    }

    @Override
    public UserDto save(User user) {
        user.setId(userStorage.nextId());
        UserDto result = userMapper.toDto(userStorage.save(user));
        log.info("Результат сохранения User был приведён в UserDto объект");
        return result;
    }

    @Override
    public UserDto update(User user) {
        UserDto result = userMapper.toDto(userStorage.update(user));
        log.info("Результат обновления User был приведён в UserDto объект");
        return result;
    }

    @Override
    public boolean delete(Long id) {
        return userStorage.delete(id);
    }

    @Override
    public boolean deleteAll() {
        return userStorage.deleteAll();
    }
}
