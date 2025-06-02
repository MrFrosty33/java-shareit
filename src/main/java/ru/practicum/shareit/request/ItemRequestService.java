package ru.practicum.shareit.request;

import ru.practicum.shareit.request.dto.ItemRequestDto;

import java.util.List;

public interface ItemRequestService {
    ItemRequestDto get(Long id);

    List<ItemRequestDto> getAll();

    ItemRequestDto save(ItemRequestDto itemRequest);

    ItemRequestDto update(ItemRequestDto itemRequest);

    void delete(Long id);

    void deleteAll();
}
