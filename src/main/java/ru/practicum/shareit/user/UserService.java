package ru.practicum.shareit.user;

import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

public interface UserService {
    User get(Long id);

    List<User> getAll();

    User save(UserDto userDto);

    User update(UserDto userDto, Long id);

    boolean delete(Long id);

    boolean deleteAll();

    void validateUserExists(Long id);
}
