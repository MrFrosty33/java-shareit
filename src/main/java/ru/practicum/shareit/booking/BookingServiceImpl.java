package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.InternalException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingCreate;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.exception.BadRequestParamException;
import ru.practicum.shareit.exception.ConflictException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.utilities.DataEnricher;
import ru.practicum.shareit.utilities.ExistenceValidator;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookingServiceImpl implements BookingService, ExistenceValidator<Booking>, DataEnricher<BookingDto, Booking> {
    private final BookingRepository bookingRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final DataEnricher<ItemDto, Item> itemDataEnricher;
    private final DataEnricher<UserDto, User> userDataEnricher;
    private final ExistenceValidator<User> userValidator;
    private final ExistenceValidator<Item> itemValidator;
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

        BookingDto result = getDto(booking);
        log.info("Получен Booking с id: {}", id);
        return result;
    }

    @Override
    public List<BookingDto> getAllByStateAndBookerId(State state, Long bookerId) {
        userValidator.validateExists(bookerId);

        List<BookingDto> result;
        LocalDateTime dateNow = LocalDateTime.now();

        switch (state) {
            case ALL -> {
                result = bookingRepository.findAllByBookerId(bookerId).stream()
                        .map(this::getDto)
                        .toList();
            }
            case PAST -> {
                result = bookingRepository.findPastByBookerId(bookerId, dateNow).stream()
                        .map(this::getDto)
                        .toList();
            }
            case FUTURE -> {
                result = bookingRepository.findFutureByBookerId(bookerId, dateNow).stream()
                        .map(this::getDto)
                        .toList();
            }
            case CURRENT -> {
                result = bookingRepository.findCurrentByBookerId(bookerId, dateNow).stream()
                        .map(this::getDto)
                        .toList();
            }
            case WAITING, REJECTED -> {
                result = bookingRepository.findWaitingOrRejectedByBookerId(State.stringValue(state), bookerId).stream()
                        .map(this::getDto)
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
        userValidator.validateExists(ownerId);

        if (itemRepository.findByOwnerId(ownerId).isEmpty()) {
            // или же выбрасывать какое исключение?
            return List.of();
        }

        List<BookingDto> result;
        LocalDateTime dateNow = LocalDateTime.now();

        switch (state) {
            case ALL -> {
                result = bookingRepository.findAllByOwnerId(ownerId).stream()
                        .map(this::getDto)
                        .toList();
            }
            case PAST -> {
                result = bookingRepository.findPastByOwnerId(ownerId, dateNow).stream()
                        .map(this::getDto)
                        .toList();
            }
            case FUTURE -> {
                result = bookingRepository.findFutureByOwnerId(ownerId, dateNow).stream()
                        .map(this::getDto)
                        .toList();
            }
            case CURRENT -> {
                result = bookingRepository.findCurrentByOwnerId(ownerId, dateNow).stream()
                        .map(this::getDto)
                        .toList();
            }
            case WAITING, REJECTED -> {
                result = bookingRepository.findWaitingOrRejectedByOwnerId(State.stringValue(state), ownerId).stream()
                        .map(this::getDto)
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
    public BookingDto save(BookingCreate bookingCreate, Long bookerId) {
        userValidator.validateExists(bookerId);
        itemValidator.validateExists(bookingCreate.getItemId());

        bookingCreate.setBookerId(bookerId);
        bookingCreate.setStatus(Status.WAITING);

        if (!itemRepository.isItemAvailable(bookingCreate.getItemId())) {
            log.info("Попытка создать Booking с уже зарезервированным Item с id: {}", bookingCreate.getItemId());
            throw new BadRequestParamException("Item с id: " + bookingCreate.getItemId() + " уже зарезервирован");
        }
        if (bookingCreate.getStart().equals(bookingCreate.getEnd())) {
            log.info("Попытка создать Booking, но start == end");
            throw new BadRequestParamException("Время начала брони и время окончания " +
                    "не может быть в один и тот же момент");
        }
        Booking booking = getEntityFromCreate(bookingCreate);
        BookingDto result = getDto(bookingRepository.save(booking));
        log.info("Сохранён Booking с id: {}", result.getId());
        return result;
    }

    @Transactional
    @Override
    public BookingDto approveBooking(Long bookingId, Long ownerId, Boolean approved) {
        //userValidator.validateExists(ownerId);
        // в целом-то, можно и не проверять
        // если ownerId не сойдётся с владельцем вещи, то и неважно, существует он или нет
        // и пусть тогда будет BadRequestParamException
        validateExists(bookingId);

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
    public void validateExists(Long id) {
        if (bookingRepository.findById(id).isEmpty()) {
            log.info("Попытка найти Booking с id: {}", id);
            throw new NotFoundException("Booking с id: " + id + " не найден");
        }
    }

    @Override
    public BookingDto getDto(Booking entity) {
        BookingDto result = bookingMapper.toDto(entity);
        result.setItem(itemDataEnricher.getDto(entity.getItem()));
        result.setBooker(userDataEnricher.getDto(entity.getBooker()));
        return result;
    }

    private BookingDto getDtoFromCreate(BookingCreate create) {
        BookingDto result = bookingMapper.toDtoFromCreate(create);
        result.setItem(itemDataEnricher.getDto(itemRepository.findById(create.getItemId()).orElseThrow(() -> {
            log.info("Попытка найти Item с id: {}", create.getItemId());
            return new NotFoundException("Item с id: " + create.getItemId() + " не найден");
        })));
        result.setBooker(userDataEnricher.getDto(userRepository.findById(create.getBookerId()).orElseThrow(() -> {
            log.info("Попытка найти User с id: {}", create.getBookerId());
            return new NotFoundException("Booker с id: " + create.getBookerId() + " не найден");
        })));
        return result;
    }

    @Override
    public Booking getEntity(BookingDto dto) {
        Booking result = bookingMapper.toEntity(dto);
        result.setItem(itemRepository.findById(dto.getItem().getId()).orElseThrow(() -> {
            log.info("Попытка найти Item с id: {}", dto.getItem().getId());
            return new NotFoundException("Item с id: " + dto.getItem().getId() + " не найден");
        }));
        result.setBooker(userRepository.findById(dto.getBooker().getId()).orElseThrow(() -> {
            log.info("Попытка найти User с id: {}", dto.getBooker().getId());
            return new NotFoundException("Booker с id: " + dto.getBooker().getId() + " не найден");
        }));
        return result;
    }

    private Booking getEntityFromCreate(BookingCreate create) {
        Booking result = bookingMapper.toEntityFromCreate(create);
        result.setItem(itemRepository.findById(create.getItemId()).orElseThrow(() -> {
            log.info("Попытка найти Item с id: {}", create.getItemId());
            return new NotFoundException("Item с id: " + create.getItemId() + " не найден");
        }));
        result.setBooker(userRepository.findById(create.getBookerId()).orElseThrow(() -> {
            log.info("Попытка найти User с id: {}", create.getBookerId());
            return new NotFoundException("Booker с id: " + create.getBookerId() + " не найден");
        }));
        return result;
    }
}
