package ru.practicum.shareit.item.dto;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.Item;

@Component
public class ItemMapper {

    public ItemDto toDto(Item item) {
        return ItemDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .ownerId(item.getOwner() != null ? item.getOwner().getId() : null)
                .requestId(item.getRequest() != null ? item.getRequest().getId() : null)
                .available(item.getAvailable())
                .build();
    }

    public Item fromDto(ItemDto itemDto) {
        //todo подтягивать из репозитория
        return Item.builder()
                .name(itemDto.getName())
                .description(itemDto.getDescription())
                .ownerId(itemDto.getOwnerId() != null ? itemDto.getOwnerId() : null)
                .requestId(itemDto.getRequestId() != null ? itemDto.getRequestId() : null)
                .available(itemDto.getAvailable())
                .build();
    }

    public Item fromDto(ItemDto itemDto, Long id) {
        //todo подтягивать из репозитория
        return Item.builder()
                .id(id)
                .name(itemDto.getName())
                .description(itemDto.getDescription())
                .ownerId(itemDto.getOwnerId() != null ? itemDto.getOwnerId() : null)
                .requestId(itemDto.getRequestId() != null ? itemDto.getRequestId() : null)
                .available(itemDto.getAvailable())
                .build();
    }
}
