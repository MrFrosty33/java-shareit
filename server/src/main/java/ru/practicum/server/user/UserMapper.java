package ru.practicum.server.user;

import org.mapstruct.Mapper;
import ru.practicum.models.user.UserDto;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDto toDto(User user);

    User toEntity(UserDto userDto);
}
