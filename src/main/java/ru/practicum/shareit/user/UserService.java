package ru.practicum.shareit.user;

import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

public interface UserService {
    UserDto get(Long id);

    List<UserDto> getAll();

    User save(User User);

    User update(User User);

    boolean delete(Long id);

    boolean deleteAll();
}
