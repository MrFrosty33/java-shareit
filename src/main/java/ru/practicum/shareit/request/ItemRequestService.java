package ru.practicum.shareit.request;

import java.util.List;

public interface ItemRequestService {
    ItemRequest get(Long id);

    List<ItemRequest> getAll();

    ItemRequest save(ItemRequest itemRequest);

    ItemRequest update(ItemRequest itemRequest);

    boolean delete(Long id);

    boolean deleteAll();
}
