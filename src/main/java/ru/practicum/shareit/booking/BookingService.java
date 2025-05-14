package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.BookingDto;

import java.util.List;

public interface BookingService {
    BookingDto get(Long id);

    List<BookingDto> getAll();

    Booking save(Booking booking);

    Booking update(Booking booking);

    boolean delete(Long id);

    boolean deleteAll();
}
