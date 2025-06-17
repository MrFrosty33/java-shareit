package ru.practicum.server.user.dto;

import org.mapstruct.Mapper;
import ru.practicum.server.user.User;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDto toDto(User user);

    User toEntity(UserDto userDto);
}
