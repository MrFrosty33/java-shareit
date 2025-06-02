package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public UserDto get(Long id) {
        validateUserExists(id);
        UserDto result = userMapper.toDto(userRepository.findById(id)
                .orElseThrow(() -> {
                    log.info("Попытка найти User с id: {}", id);
                    return new NotFoundException("User с id: " + id + " не найден");
                }));
        log.info("Получен User с id: {}", id);
        return result;
    }

    @Override
    public List<UserDto> getAll() {
        List<UserDto> result = userRepository.findAll().stream().map(userMapper::toDto).toList();
        log.info("Получен список всех User");
        return result;
    }

    @Transactional
    @Override
    public UserDto save(UserDto userDto) {
        UserDto result = userMapper.toDto(userRepository.save(userMapper.fromDto(userDto)));
        log.info("Сохранён User с id: {}", result.getId());
        return result;
    }

    @Transactional
    @Override
    public UserDto update(UserDto userDto, Long id) {
        validateUserExists(id);


        UserDto result = userMapper.toDto(userRepository.save(userMapper.fromDto(userDto, id)));
        log.info("Обновлён User с id: {}", id);
        return result;
    }

    @Transactional
    @Override
    public void delete(Long id) {
        validateUserExists(id);
        userRepository.delete(userRepository.findById(id).get());
        log.info("Удалён User с id: {}", id);
    }

    @Transactional
    @Override
    public void deleteAll() {
        if (userRepository.findAll().isEmpty()) {
            log.info("Попытка очистить таблицу User, но она уже пуста");
        } else {
            userRepository.deleteAll();
            log.info("Очищено хранилище User");
        }
    }

    // при подключении БД в будущем наверно стоит вынести в отдельный класс валидатор?
    @Override
    public void validateUserExists(Long id) {
        if (userRepository.findById(id).isEmpty()) {
            log.info("Попытка найти несуществующего User с id: {}", id);
            throw new NotFoundException("User с id: " + id + " не найден");
        }
    }
}
