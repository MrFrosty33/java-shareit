package ru.practicum.models.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true)
public class ItemRequestAnswer {
    private Long id;
    private String name;
    private Long ownerId;
    private boolean available;
}
