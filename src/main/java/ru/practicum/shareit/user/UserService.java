package ru.practicum.shareit.user;

import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

public interface UserService {
    UserDto get(Long id);

    List<UserDto> getAll();

    UserDto save(UserDto userDto);

    UserDto update(UserDto userDto, Long id);

    boolean delete(Long id);

    boolean deleteAll();

    void validateUserExists(Long id);
}
