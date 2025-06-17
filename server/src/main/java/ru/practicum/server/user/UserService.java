package ru.practicum.server.user;

import ru.practicum.models.user.UserDto;

import java.util.List;

public interface UserService {
    UserDto get(Long id);

    List<UserDto> getAll();

    UserDto save(UserDto userDto);

    UserDto update(UserDto userDto, Long id);

    void delete(Long id);

    void deleteAll();
}
