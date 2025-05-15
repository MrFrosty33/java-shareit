package ru.practicum.shareit.booking.dto;


import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.Booking;

@Component
public class BookingMapper {

    public BookingDto toDto(Booking booking) {
        return BookingDto.builder()
                .startDate(booking.getStartDate())
                .endDate(booking.getEndDate())
                .status(booking.getStatus())
                .build();
    }
}
