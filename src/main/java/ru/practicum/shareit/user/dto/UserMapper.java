package ru.practicum.shareit.user.dto;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.user.User;

@Component
public class UserMapper {

    public UserDto toDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .build();
    }

    public User fromDto(UserDto userDto) {
        return User.builder()
                .name(userDto.getName())
                .email(userDto.getEmail())
                .build();
    }

    public User fromDto(UserDto userDto, Long id) {
        return User.builder()
                .id(id)
                .name(userDto.getName())
                .email(userDto.getEmail())
                .build();
    }
}
