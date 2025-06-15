package ru.practicum.shareit.request.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true)
public class ItemRequestAnswer {
    private Long itemId;
    private String itemName;
    private Long ownerId;
    private boolean available;
}
