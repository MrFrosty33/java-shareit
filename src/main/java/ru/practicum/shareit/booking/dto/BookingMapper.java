package ru.practicum.shareit.booking.dto;


import ru.practicum.shareit.booking.Booking;

public class BookingMapper {

    public BookingDto toDto(Booking booking) {
        return BookingDto.builder()
                .startDate(booking.getStartDate())
                .endDate(booking.getEndDate())
                .status(booking.getStatus())
                .build();
    }
}
