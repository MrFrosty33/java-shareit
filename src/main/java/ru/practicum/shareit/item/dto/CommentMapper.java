package ru.practicum.shareit.item.dto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.Comment;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.user.UserRepository;

@Component
@RequiredArgsConstructor
@Slf4j
public class CommentMapper {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    public CommentDto toDto(Comment comment) {
        return CommentDto.builder()
                .id(comment.getId())
                .text(comment.getText())
                .itemId(comment.getItem().getId())
                .authorId(comment.getAuthor().getId())
                .created(comment.getCreated())
                .build();
    }

    public Comment fromDto(CommentDto commentDto) {
        return Comment.builder()
                .text(commentDto.getText())
                .item(itemRepository.findById(commentDto.getItemId()).orElseThrow(() -> {
                    log.info("Попытка найти Item с id: {}", commentDto.getItemId());
                    return new NotFoundException("Item с id: " + commentDto.getItemId() + " не найден");
                }))
                .author(userRepository.findById(commentDto.getAuthorId()).orElseThrow(() -> {
                    log.info("Попытка найти User с id: {}", commentDto.getAuthorId());
                    return new NotFoundException("Author с id: " + commentDto.getAuthorId() + " не найден");
                }))
                .created(commentDto.getCreated())
                .build();
    }

    public Comment fromDto(CommentDto commentDto, Long id) {
        Comment result = fromDto(commentDto);
        result.setId(id);
        return result;
    }

}
