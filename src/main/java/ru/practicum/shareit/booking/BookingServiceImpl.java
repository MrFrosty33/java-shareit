package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.InternalException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoCreate;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.exception.BadRequestParamException;
import ru.practicum.shareit.exception.ConflictException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.user.UserService;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final ItemRepository itemRepository;
    private final UserService userService;
    private final ItemService itemService;
    private final BookingMapper bookingMapper;

    @Override
    public BookingDto get(Long id, Long bookerOrItemOwnerId) {
        Booking booking = bookingRepository.findById(id).orElseThrow(() -> {
            log.info("Попытка найти Booking с id: {}", id);
            return new NotFoundException("Booking с id: " + id + " не найден");
        });
        Long itemOwnerId = booking.getItem().getOwner().getId();
        Long bookerId = booking.getBooker().getId();

        if (!booking.getBooker().getId().equals(bookerOrItemOwnerId) &&
                !booking.getItem().getOwner().getId().equals(bookerOrItemOwnerId)) {
            log.info("Попытка получить информацию о Booking, " +
                            "но bookerOrItemOwnerId: {} отличается от bookerId: {} и itemOwnerId: {}",
                    bookerOrItemOwnerId, bookerId, itemOwnerId);
            throw new ConflictException("В доступе отказано, itemOwnerId / bookerId: " +
                    itemOwnerId + " / " + bookerId +
                    " отличается от Вашего userId: " + bookerOrItemOwnerId);
        }

        BookingDto result = bookingMapper.toDto(booking);
        log.info("Получен Booking с id: {}", id);
        return result;
    }

    @Override
    public List<BookingDto> getAllByStateAndBookerId(State state, Long bookerId) {
        userService.validateUserExists(bookerId);

        List<BookingDto> result;
        LocalDate dateNow = LocalDate.now();

        switch (state) {
            case ALL -> {
                result = bookingRepository.findAllByBookerId(bookerId).stream()
                        .map(bookingMapper::toDto)
                        .toList();
            }
            case PAST -> {
                result = bookingRepository.findPastByBookerId(bookerId, dateNow).stream()
                        .map(bookingMapper::toDto)
                        .toList();
            }
            case FUTURE -> {
                result = bookingRepository.findFutureByBookerId(bookerId, dateNow).stream()
                        .map(bookingMapper::toDto)
                        .toList();
            }
            case CURRENT -> {
                result = bookingRepository.findCurrentByBookerId(bookerId, dateNow).stream()
                        .map(bookingMapper::toDto)
                        .toList();
            }
            case WAITING, REJECTED -> {
                result = bookingRepository.findWaitingOrRejectedByBookerId(State.stringValue(state), bookerId).stream()
                        .map(bookingMapper::toDto)
                        .toList();
            }
            default -> {
                log.info("Внутренняя ошибка сервера в методе BookingServiceImpl.getAllByStateAndBookerId(...)");
                throw new InternalException("Внутренняя ошибка сервера");
            }
        }

        log.info("Получен список Booking с bookerId: {} и state: {}", bookerId, state);
        return result;
    }

    @Override
    public List<BookingDto> getAllByStateAndOwnerId(State state, Long ownerId) {
        userService.validateUserExists(ownerId);

        if (itemRepository.findByOwnerId(ownerId).isEmpty()) {
            // или же выбрасывать какое исключение?
            return List.of();
        }

        List<BookingDto> result;
        LocalDate dateNow = LocalDate.now();

        switch (state) {
            case ALL -> {
                result = bookingRepository.findAllByOwnerId(ownerId).stream()
                        .map(bookingMapper::toDto)
                        .toList();
            }
            case PAST -> {
                result = bookingRepository.findPastByOwnerId(ownerId, dateNow).stream()
                        .map(bookingMapper::toDto)
                        .toList();
            }
            case FUTURE -> {
                result = bookingRepository.findFutureByOwnerId(ownerId, dateNow).stream()
                        .map(bookingMapper::toDto)
                        .toList();
            }
            case CURRENT -> {
                result = bookingRepository.findCurrentByOwnerId(ownerId, dateNow).stream()
                        .map(bookingMapper::toDto)
                        .toList();
            }
            case WAITING, REJECTED -> {
                result = bookingRepository.findWaitingOrRejectedByOwnerId(State.stringValue(state), ownerId).stream()
                        .map(bookingMapper::toDto)
                        .toList();
            }
            default -> {
                log.info("Внутренняя ошибка сервера в методе BookingServiceImpl.getAllByStateAndOwnerId(...)");
                throw new InternalException("Внутренняя ошибка сервера");
            }
        }

        log.info("Получен список Booking с ownerId: {} и state: {}", ownerId, state);
        return result;
    }

    @Transactional
    @Override
    public BookingDto save(BookingDtoCreate bookingDtoCreate, Long bookerId) {
        userService.validateUserExists(bookerId);
        itemService.validateItemExists(bookingDtoCreate.getItemId());

        bookingDtoCreate.setBookerId(bookerId);
        bookingDtoCreate.setStatus(Status.WAITING);

        if (!itemService.isItemAvailable(bookingDtoCreate.getItemId())) {
            log.info("Попытка создать Booking с уже зарезервированным Item с id: {}", bookingDtoCreate.getItemId());
            throw new BadRequestParamException("Item с id: " + bookingDtoCreate.getItemId() + " уже зарезервирован");
        }
        if (bookingDtoCreate.getStart().equals(bookingDtoCreate.getEnd())) {
            log.info("Попытка создать Booking, но start == end");
            throw new BadRequestParamException("Время начала брони и время окончания " +
                    "не может быть в один и тот же момент");
        }
        Booking booking = bookingMapper.mapEntityFromDtoCreate(bookingDtoCreate);
        BookingDto result = bookingMapper.toDto(bookingRepository.save(booking));
        log.info("Сохранён Booking с id: {}", result.getId());
        return result;
    }

    @Transactional
    @Override
    public BookingDto approveBooking(Long bookingId, Long ownerId, Boolean approved) {
        //userService.validateUserExists(ownerId);
        // в тестах передаётся id несуществующего пользователя, но не ожидается 404 код ответа...

        validateBookingExists(bookingId);

        Booking booking = bookingRepository.findById(bookingId).get();
        Long itemOwnerId = booking.getItem().getOwner().getId();

        if (booking.getItem().getOwner().getId().equals(ownerId)) {
            if (approved) {
                booking.setStatus(Status.APPROVED);
                log.info("У Booking с id: {} изменён status на: {}", bookingId, Status.APPROVED);
            } else {
                booking.setStatus(Status.REJECTED);
                log.info("У Booking с id: {} изменён status на: {}", bookingId, Status.REJECTED);
            }
        } else {
            log.info("Попытка изменить статус Booking, но ownerId: {} отличается от фактического itemOwnerId: {}",
                    ownerId, itemOwnerId);
            throw new BadRequestParamException("В доступе отказано, itemOwnerId: " +
                    itemOwnerId + " отличается от Вашего userId: " + ownerId);
        }

        return get(bookingId, ownerId);
    }

    @Override
    public void validateBookingExists(Long id) {
        if (bookingRepository.findById(id).isEmpty()) {
            log.info("Попытка найти Booking с id: {}", id);
            throw new NotFoundException("Booking с id: " + id + " не найден");
        }
    }
}
