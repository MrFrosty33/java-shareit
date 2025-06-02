package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.user.UserService;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final UserService userService;

    @Override
    public BookingDto get(Long id, Long bookerOrItemOwnerId) {
        //todo Может быть выполнено либо автором бронирования, либо владельцем вещи
        return null;
    }

    @Override
    public List<BookingDto> getAllByStateAndBookerId(State state, Long bookerId) {
        //todo Получение списка всех бронирований текущего пользователя
        // Бронирования должны возвращаться отсортированными по дате от более новых к более старым.

        //todo конвертация state -> String status
        return List.of();
    }

    @Override
    public List<BookingDto> getAllByStateAndOwnerId(State state, Long bookerId) {
        //todo конвертация state -> String status
        // должен владеть какой-то вещью
        return List.of();
    }

    @Transactional
    @Override
    public BookingDto save(BookingDto bookingDto, Long bookerId) {
        return null;
    }

    @Transactional
    @Override
    public BookingDto approveBooking(Long bookingId, Long ownerId, boolean approved) {
        return null;
    }
}
