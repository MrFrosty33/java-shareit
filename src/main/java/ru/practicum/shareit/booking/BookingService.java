package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoCreate;

import java.util.List;

public interface BookingService {
    BookingDto get(Long id, Long bookerOrItemOwnerId);

    List<BookingDto> getAllByStateAndBookerId(State state, Long bookerId);

    List<BookingDto> getAllByStateAndOwnerId(State state, Long ownerId);

    BookingDto save(BookingDtoCreate booking, Long bookerId);

    BookingDto approveBooking(Long bookingId, Long ownerId, Boolean approved);

    void validateBookingExists(Long id);
}
