package ru.practicum.shareit.request;

import ru.practicum.shareit.request.dto.CreateItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import java.util.List;

public interface ItemRequestService {
    List<ItemRequestDto> get(Long userId);

    ItemRequestDto getById(Long userId, Long requestId);

    List<ItemRequestDto> getAllByUserId(Long userId);

    CreateItemRequestDto save(Long requesterId, CreateItemRequestDto itemRequest);

    ItemRequestDto getDtoWithAnswers(ItemRequest entity);

    ItemRequest getEntity(ItemRequestDto dto);

//    void delete(Long id);
//
//    void deleteAll();
}
