package ru.practicum.shareit.user;

import java.util.List;

public interface UserStorage {
    User get(Long id);

    List<User> getAll();

    User save(User User);

    User update(User User);

    boolean delete(Long id);

    boolean deleteAll();
}
