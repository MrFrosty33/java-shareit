package ru.practicum.shareit.request;

import ru.practicum.shareit.request.dto.ItemRequestDto;

import java.util.List;

public interface ItemRequestService {
    ItemRequestDto get(Long userId);

    ItemRequestDto getById(Long userId, Long requestId);

    List<ItemRequestDto> getAllByUserId(Long userId);

    ItemRequestDto save(ItemRequestDto itemRequest);

//    void delete(Long id);
//
//    void deleteAll();
}
