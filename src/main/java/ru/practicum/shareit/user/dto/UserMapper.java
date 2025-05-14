package ru.practicum.shareit.user.dto;

import ru.practicum.shareit.user.User;

public class UserMapper {

    public UserDto toDto(User user) {
        return UserDto.builder()
                .name(user.getName())
                .items(user.getItems().keySet().stream().toList())
                .build();
    }
}
