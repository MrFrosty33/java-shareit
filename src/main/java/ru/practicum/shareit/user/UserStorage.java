package ru.practicum.shareit.user;

import java.util.List;

public interface UserStorage {
    User get(Long id);

    List<User> getAll();

    User save(User user);

    User update(User user);

    boolean delete(Long id);

    boolean deleteAll();

    void validateExists(Long id);

    void validateUniqueEmail(String email);
}
