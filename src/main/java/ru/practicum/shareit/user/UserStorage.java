package ru.practicum.shareit.user;

import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;
import java.util.Set;

public interface UserStorage {
    public Set<Long> getIds();

    User get(Long id);

    List<User> getAll();

    User save(User user);

    User update(UserDto userDto, Long id);

    boolean delete(Long id);

    boolean deleteAll();

}
