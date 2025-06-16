package ru.practicum.shareit.item.dto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.shareit.item.Item;

@Mapper(componentModel = "spring")
public interface ItemMapper {
    @Mapping(target = "ownerId",
            expression = "java(item.getOwner() != null ? item.getOwner().getId() : null)")
    @Mapping(target = "requestId",
            expression = "java(item.getRequest() != null ? item.getRequest().getId() : null)")
    @Mapping(target = "comments", ignore = true)
    @Mapping(target = "lastBooking", ignore = true)
    @Mapping(target = "nextBooking", ignore = true)
    ItemDto toDto(Item item);

    @Mapping(target = "owner", ignore = true)
    @Mapping(target = "request", ignore = true)
    Item toEntity(ItemDto itemDto);
}
