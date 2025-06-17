package ru.practicum.models.booking;

import lombok.Builder;
import lombok.Data;
import ru.practicum.models.item.ItemDto;
import ru.practicum.models.user.UserDto;

import java.time.LocalDateTime;

@Data
@Builder(toBuilder = true)
public class BookingDto {
    private Long id;

    private ItemDto item;

    private LocalDateTime start;

    private LocalDateTime end;

    private UserDto booker;

    private Status status;
}
