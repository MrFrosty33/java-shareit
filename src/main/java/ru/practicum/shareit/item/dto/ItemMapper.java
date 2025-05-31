package ru.practicum.shareit.item.dto;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.ItemRepository;

@Component
@RequiredArgsConstructor
public class ItemMapper {
    private final ItemRepository itemRepository;


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
        //todo нужен userRepository & requestRepository
//        return Item.builder()
//                .name(itemDto.getName())
//                .description(itemDto.getDescription())
//                .ownerId(itemDto.getOwnerId() != null ? itemDto.getOwnerId() : null)
//                .requestId(itemDto.getRequestId() != null ? itemDto.getRequestId() : null)
//                .available(itemDto.getAvailable())
//                .build();
        return null;
    }

    public Item fromDto(ItemDto itemDto, Long id) {
        //todo нужен userRepository & requestRepository
//        return Item.builder()
//                .id(id)
//                .name(itemDto.getName())
//                .description(itemDto.getDescription())
//                .ownerId(itemDto.getOwnerId() != null ? itemDto.getOwnerId() : null)
//                .requestId(itemDto.getRequestId() != null ? itemDto.getRequestId() : null)
//                .available(itemDto.getAvailable())
//                .build();
        return null;
    }
}
