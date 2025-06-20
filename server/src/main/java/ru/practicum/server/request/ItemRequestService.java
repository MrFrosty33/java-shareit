package ru.practicum.server.request;

import ru.practicum.models.request.CreateItemRequestDto;
import ru.practicum.models.request.ItemRequestDto;

import java.util.List;

public interface ItemRequestService {
    List<ItemRequestDto> getOthersRequests(Long userId);

    ItemRequestDto getByRequestId(Long userId, Long requestId);

    List<ItemRequestDto> getAllByUserId(Long userId);

    CreateItemRequestDto save(Long requesterId, CreateItemRequestDto itemRequest);

    ItemRequestDto getDtoWithAnswers(ItemRequest entity);

    ItemRequest getEntity(ItemRequestDto dto);

//    void delete(Long id);
//
//    void deleteAll();
}
