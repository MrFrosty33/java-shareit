package ru.practicum.shareit.user;

import ru.practicum.shareit.user.dto.UserDto;

public interface UserDataFiller {
    UserDto getDto(User entity);

    User getEntity(UserDto dto);
}
