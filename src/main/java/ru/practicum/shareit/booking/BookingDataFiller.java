package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.BookingDto;

public interface BookingDataFiller {
    BookingDto getDto(Booking entity);

    Booking getEntity(BookingDto dto);
}
