package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.ConflictException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.utilities.Validator;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService, Validator<User>, UserDataFiller {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public UserDto get(Long id) {
        validateExists(id);
        UserDto result = getDto(userRepository.findById(id)
                .orElseThrow(() -> {
                    log.info("Попытка найти User с id: {}", id);
                    return new NotFoundException("User с id: " + id + " не найден");
                }));
        log.info("Получен User с id: {}", id);
        return result;
    }

    @Override
    public List<UserDto> getAll() {
        List<UserDto> result = userRepository.findAll().stream().map(this::getDto).toList();
        log.info("Получен список всех User");
        return result;
    }

    @Transactional
    @Override
    public UserDto save(UserDto userDto) {
        UserDto result = getDto(userRepository.save(getEntity(userDto)));
        log.info("Сохранён User с id: {}", result.getId());
        return result;
    }

    @Transactional
    @Override
    public UserDto update(UserDto userDto, Long id) {
        validateExists(id);
        if (userRepository.getEmails(id).contains(userDto.getEmail())) {
            log.info("Попытка обновить email у пользователя с id: {} на уже занятый email: {}", id, userDto.getEmail());
            throw new ConflictException("Email: " + userDto.getEmail() + " уже занят другим пользователем");
        }

        User user = userRepository.findById(id).get();
        if (userDto.getName() != null && !userDto.getName().isBlank()) user.setName(userDto.getName());
        if (userDto.getEmail() != null) user.setEmail(userDto.getEmail());
        log.info("Обновлён User с id: {}", id);
        // т.к. @Transactional, вызов метода репозитория save не требуется
        return getDto(user);
    }

    @Transactional
    @Override
    public void delete(Long id) {
        validateExists(id);
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
    public void validateExists(Long id) {
        if (userRepository.findById(id).isEmpty()) {
            log.info("Попытка найти несуществующего User с id: {}", id);
            throw new NotFoundException("User с id: " + id + " не найден");
        }
    }

    // Чтобы в будущем просто сюда добавлять необходимые поля, и не переписывать каждый метод
    @Override
    public UserDto getDto(User user) {
        return userMapper.toDto(user);
    }

    @Override
    public User getEntity(UserDto dto) {
        return userMapper.toEntity(dto);
    }
}
