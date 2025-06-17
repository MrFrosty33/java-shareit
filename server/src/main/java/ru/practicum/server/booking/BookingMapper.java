package ru.practicum.server.booking;


import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.models.booking.BookingCreate;
import ru.practicum.models.booking.BookingDto;

@Mapper(componentModel = "spring")
public interface BookingMapper {

    @Mapping(target = "start", source = "startDate")
    @Mapping(target = "end", source = "endDate")
    @Mapping(target = "item", ignore = true)
    @Mapping(target = "booker", ignore = true)
    BookingDto toDto(Booking booking);

    @Mapping(target = "startDate", source = "start")
    @Mapping(target = "endDate", source = "end")
    @Mapping(target = "item", ignore = true)
    @Mapping(target = "booker", ignore = true)
    Booking toEntity(BookingDto bookingDto);

    @Mapping(target = "item", ignore = true)
    @Mapping(target = "booker", ignore = true)
    BookingDto toDtoFromCreate(BookingCreate bookingCreate);

    @Mapping(target = "startDate", source = "start")
    @Mapping(target = "endDate", source = "end")
    @Mapping(target = "item", ignore = true)
    @Mapping(target = "booker", ignore = true)
    Booking toEntityFromCreate(BookingCreate bookingCreate);
}
