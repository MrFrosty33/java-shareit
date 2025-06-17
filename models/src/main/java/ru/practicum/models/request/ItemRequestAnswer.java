package ru.practicum.models.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class ItemRequestAnswer {
    private Long id;
    private String name;
    private Long ownerId;
    private boolean available;
}
