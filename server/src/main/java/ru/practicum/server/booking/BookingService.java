package ru.practicum.server.booking;

import ru.practicum.server.booking.dto.BookingCreate;
import ru.practicum.server.booking.dto.BookingDto;

import java.util.List;

public interface BookingService {
    BookingDto get(Long id, Long bookerOrItemOwnerId);

    List<BookingDto> getAllByStateAndBookerId(State state, Long bookerId);

    List<BookingDto> getAllByStateAndOwnerId(State state, Long ownerId);

    BookingDto save(BookingCreate booking, Long bookerId);

    BookingDto approveBooking(Long bookingId, Long ownerId, Boolean approved);
}
