package ru.practicum.server.item;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.models.item.CommentDto;

@Mapper(componentModel = "spring")
public interface CommentMapper {
    @Mapping(target = "authorName", source = "comment.author.name")
    CommentDto toDto(Comment comment);

    @Mapping(target = "item", ignore = true)
    @Mapping(target = "author", ignore = true)
    Comment toEntity(CommentDto commentDto);
}
