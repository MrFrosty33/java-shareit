package ru.practicum.shareit.booking.dto;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.user.UserRepository;

@Component
@RequiredArgsConstructor
@Slf4j
public class BookingMapper {
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    public BookingDto toDto(Booking booking) {
        return BookingDto.builder()
                .startDate(booking.getStartDate())
                .endDate(booking.getEndDate())
                .itemId(booking.getItem().getId())
                .bookerId(booking.getBooker().getId())
                .status(booking.getStatus())
                .build();
    }

    public Booking fromDto(BookingDto bookingDto) {
        return Booking.builder()
                .startDate(bookingDto.getStartDate())
                .endDate(bookingDto.getEndDate())
                .item(itemRepository.findById(bookingDto.getItemId()).orElseThrow(() -> {
                    log.info("Попытка найти Item с id: {}", bookingDto.getItemId());
                    return new NotFoundException("Item с id: " + bookingDto.getItemId() + " не найден");
                }))
                .booker(userRepository.findById(bookingDto.getBookerId()).orElseThrow(() -> {
                    log.info("Попытка найти User с id: {}", bookingDto.getBookerId());
                    return new NotFoundException("Booker с id: " + bookingDto.getBookerId() + " не найден");
                }))
                .status(bookingDto.getStatus())
                .build();
    }

    public Booking fromDto(BookingDto bookingDto, Long id) {
        Booking result = fromDto(bookingDto);
        result.setId(id);
        return result;
    }
}
