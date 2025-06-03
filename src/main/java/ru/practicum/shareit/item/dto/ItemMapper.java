package ru.practicum.shareit.item.dto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.CommentRepository;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.request.ItemRequestRepository;
import ru.practicum.shareit.user.UserRepository;

@Component
@RequiredArgsConstructor
@Slf4j
public class ItemMapper {
    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;
    private final UserRepository userRepository;
    private final ItemRequestRepository requestRepository;


    public ItemDto toDto(Item item) {
        return ItemDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .ownerId(item.getOwner() != null ? item.getOwner().getId() : null)
                .requestId(item.getRequest() != null ? item.getRequest().getId() : null)
                .comments(commentRepository.findAllByItemId(item.getId()).stream()
                        .map(commentMapper::toDto)
                        .toList())
                .available(item.getAvailable())
                .build();
    }

    public Item fromDto(ItemDto itemDto) {
        Item result = Item.builder()
                .name(itemDto.getName())
                .description(itemDto.getDescription())
                .available(itemDto.getAvailable())
                .build();
        if (itemDto.getOwnerId() != null) {
            result.setOwner(userRepository.findById(itemDto.getOwnerId()).orElseThrow(() -> {
                log.info("Попытка найти User с id: {}", itemDto.getOwnerId());
                return new NotFoundException("Owner с id: " + itemDto.getOwnerId() + " не найден");
            }));
        }
        if (itemDto.getRequestId() != null) {
            result.setRequest(requestRepository.findById(itemDto.getRequestId()).orElseThrow(() -> {
                log.info("Попытка найти ItemRequest с id: {}", itemDto.getRequestId());
                return new NotFoundException("Request с id: " + itemDto.getRequestId() + " не найден");
            }));
        }
        return result;
    }

    public Item fromDto(ItemDto itemDto, Long id) {
        Item result = fromDto(itemDto);
        result.setId(id);
        return result;
    }
}
