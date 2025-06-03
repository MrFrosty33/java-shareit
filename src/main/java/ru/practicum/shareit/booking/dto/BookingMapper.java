package ru.practicum.shareit.booking.dto;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.dto.UserMapper;

@Component
@RequiredArgsConstructor
@Slf4j
public class BookingMapper {
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final ItemMapper itemMapper;
    private final UserMapper userMapper;

    public BookingDto toDto(Booking booking) {
        return BookingDto.builder()
                .id(booking.getId())
                .start(booking.getStartDate())
                .end(booking.getEndDate())
                .item(itemMapper.toDto(booking.getItem()))
                .booker(userMapper.toDto(booking.getBooker()))
                .status(booking.getStatus())
                .build();
    }

    public Booking fromDto(BookingDto bookingDto) {
        return Booking.builder()
                .startDate(bookingDto.getStart())
                .endDate(bookingDto.getEnd())
                .item(itemRepository.findById(bookingDto.getItem().getId()).orElseThrow(() -> {
                    log.info("Попытка найти Item с id: {}", bookingDto.getItem().getId());
                    return new NotFoundException("Item с id: " + bookingDto.getItem().getId() + " не найден");
                }))
                .booker(userRepository.findById(bookingDto.getBooker().getId()).orElseThrow(() -> {
                    log.info("Попытка найти User с id: {}", bookingDto.getBooker().getId());
                    return new NotFoundException("Booker с id: " + bookingDto.getBooker().getId() + " не найден");
                }))
                .status(bookingDto.getStatus())
                .build();
    }

    public Booking fromDto(BookingDto bookingDto, Long id) {
        Booking result = fromDto(bookingDto);
        result.setId(id);
        return result;
    }

    public BookingDto mapDtoFromDtoCreate(BookingDtoCreate bookingDtoCreate) {
        return BookingDto.builder()
                .item(itemMapper.toDto(itemRepository.findById(bookingDtoCreate.getItemId()).orElseThrow(() -> {
                    log.info("Попытка найти Item с id: {}", bookingDtoCreate.getItemId());
                    return new NotFoundException("Item с id: " + bookingDtoCreate.getItemId() + " не найден");
                })))
                .booker(userMapper.toDto(userRepository.findById(bookingDtoCreate.getBookerId()).orElseThrow(() -> {
                    log.info("Попытка найти User с id: {}", bookingDtoCreate.getBookerId());
                    return new NotFoundException("Booker с id: " + bookingDtoCreate.getBookerId() + " не найден");
                })))
                .start(bookingDtoCreate.getStart())
                .end(bookingDtoCreate.getEnd())
                .status(bookingDtoCreate.getStatus())
                .build();
    }

    public Booking mapEntityFromDtoCreate(BookingDtoCreate bookingDtoCreate) {
        return Booking.builder()
                .item(itemRepository.findById(bookingDtoCreate.getItemId()).orElseThrow(() -> {
                    log.info("Попытка найти Item с id: {}", bookingDtoCreate.getItemId());
                    return new NotFoundException("Item с id: " + bookingDtoCreate.getItemId() + " не найден");
                }))
                .booker(userRepository.findById(bookingDtoCreate.getBookerId()).orElseThrow(() -> {
                    log.info("Попытка найти User с id: {}", bookingDtoCreate.getBookerId());
                    return new NotFoundException("Booker с id: " + bookingDtoCreate.getBookerId() + " не найден");
                }))
                .startDate(bookingDtoCreate.getStart())
                .endDate(bookingDtoCreate.getEnd())
                .status(bookingDtoCreate.getStatus())
                .build();
    }
}
