package ru.practicum.server.booking;

import ru.practicum.models.booking.BookingCreate;
import ru.practicum.models.booking.BookingDto;
import ru.practicum.models.booking.State;

import java.util.List;

public interface BookingService {
    BookingDto get(Long id, Long bookerOrItemOwnerId);

    List<BookingDto> getAllByStateAndBookerId(State state, Long bookerId);

    List<BookingDto> getAllByStateAndOwnerId(State state, Long ownerId);

    BookingDto save(BookingCreate booking, Long bookerId);

    BookingDto approveBooking(Long bookingId, Long ownerId, Boolean approved);
}
