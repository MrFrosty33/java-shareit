package ru.practicum.shareit.booking;

import java.util.List;

public interface BookingStorage {
    Booking get(Long id);

    List<Booking> getAll();

    Booking save(Booking booking);

    Booking update(Booking booking);

    boolean delete(Long id);

    boolean deleteAll();
}
