package ru.practicum.shareit.item.dto;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.Item;

@Component
public class ItemMapper {

    public ItemDto toDto(Item item) {
        return ItemDto.builder()
                .name(item.getName())
                .description(item.getDescription())
                .ownerId(item.getOwnerId() != null ? item.getOwnerId() : null)
                .requestId(item.getRequestId() != null ? item.getRequestId() : null)
                .availability(item.getAvailability())
                .build();
    }

    public Item fromDto(ItemDto itemDto) {
        return Item.builder()
                .name(itemDto.getName())
                .description(itemDto.getDescription())
                .availability(itemDto.getAvailability())
                .build();
    }
}
