package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.ItemDto;

public interface ItemDataFiller {
    ItemDto getDto(Item entity);

    Item getEntity(ItemDto dto);
}
