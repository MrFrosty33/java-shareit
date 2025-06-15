package ru.practicum.shareit.request.dto;

import lombok.Data;

@Data
public class ItemRequestAnswer {
    private Long itemId;
    private String itemName;
    private Long ownerId;
}
