package ru.practicum.server.booking.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.server.booking.Status;
import ru.practicum.server.item.dto.ItemDto;
import ru.practicum.server.user.dto.UserDto;

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
