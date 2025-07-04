package ru.practicum.server.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.InternalException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.models.booking.Status;
import ru.practicum.models.item.CommentDto;
import ru.practicum.models.item.ItemDto;
import ru.practicum.models.system.LocalDateTimeProvider;
import ru.practicum.server.booking.Booking;
import ru.practicum.server.booking.BookingRepository;
import ru.practicum.server.exception.BadRequestParamException;
import ru.practicum.server.exception.ConflictException;
import ru.practicum.server.exception.NotFoundException;
import ru.practicum.server.request.ItemRequestRepository;
import ru.practicum.server.user.User;
import ru.practicum.server.user.UserRepository;
import ru.practicum.server.utilities.DataEnricher;
import ru.practicum.server.utilities.ExistenceValidator;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ItemServiceImpl implements ItemService, ExistenceValidator<Item>, DataEnricher<ItemDto, Item> {
    private final ItemRepository itemRepository;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;
    private final ItemRequestRepository requestRepository;
    private final UserRepository userRepository;
    private final ExistenceValidator<User> userValidator;
    private final CommentMapper commentMapper;
    private final ItemMapper itemMapper;

    @Override
    public ItemDto get(Long itemId, Long userId) {
        validateExists(itemId);

        // userId поступает в заголовке, но пока никак не применяется?
        // по ТЗ информацию о предмете может получить любой пользователь
        userValidator.validateExists(userId);

        ItemDto result = getDto(itemRepository.findById(itemId)
                .orElseThrow(() -> {
                    log.info("Попытка найти Item с id: {}", itemId);
                    return new NotFoundException("Item с id: " + itemId + " не найден");
                }));
        log.info("Получен Item с id: {}", itemId);
        return result;
    }

    @Override
    public List<ItemDto> getAllItemsByUserId(Long userId) {
        userValidator.validateExists(userId);

        List<ItemDto> result = itemRepository.findAll().stream()
                .filter(item -> item.getOwner().getId().equals(userId))
                .map(this::getDto)
                .toList();
        log.info("Получен список всех Item у пользователя с id: {}", userId);
        return result;
    }

    @Override
    public Boolean isItemAvailable(Long itemId) {
        Boolean result = itemRepository.isItemAvailable(itemId);
        log.info("Получена информация о доступносте Item с itemId: {}", itemId);
        return result;
    }

    @Override
    public List<ItemDto> search(String text, Long userId) {
        if (text.isEmpty()) return List.of();
        String textEdited = text.toLowerCase();

        List<ItemDto> result = getAllItemsByUserId(userId).stream()
                .filter(item ->
                        (item.getDescription().toLowerCase().contains(textEdited)
                                || item.getName().toLowerCase().contains(textEdited))
                                && item.getAvailable())
                .toList();
        log.info("Полученный список всех Item был отфильтрован, " +
                "содержащие text: {} в имени или описании экземпляры со статусом available: " +
                "true были переданы далее", text);
        return result;
    }

    @Transactional
    @Override
    public ItemDto save(ItemDto itemDto, Long userId) {
        userValidator.validateExists(userId);
        itemDto.setOwnerId(userId);
        ItemDto result = getDto(itemRepository.save(getEntity(itemDto)));
        log.info("Сохранён Item с id: {}", result.getId());
        return result;
    }

    @Transactional
    @Override
    public CommentDto addComment(CommentDto commentDto, Long itemId, Long userId) {
        validateExists(itemId);
        userValidator.validateExists(userId);

        LocalDateTime now = LocalDateTimeProvider.getLocalDateTime();
        List<Booking> queryResult = bookingRepository.getLastBookingByBookerIdAndItemId(userId, itemId, now);
        if (queryResult.isEmpty()) {
            log.info("Попытка добавить Comment, но userId: {} не брал эту вещь в аренду",
                    userId);
            throw new BadRequestParamException("userId: " + userId + " не брал эту вещь в аренду");
        }

        Booking booking = queryResult.getFirst();

        if (booking != null && booking.getBooker().getId().equals(userId)) {
            Comment comment = getCommentEntity(commentDto);
            comment.setItem(booking.getItem());
            comment.setAuthor(booking.getBooker());
            comment.setCreated(now);

            CommentDto result = getCommentDto(commentRepository.save(comment));
            log.info("Был добавлен Comment с id: {}", result.getId());
            return result;
        } else if (booking != null && booking.getStatus().equals(Status.APPROVED)) {
            log.info("Попытка добавить Comment, но booking.status: APPROVED");
            throw new BadRequestParamException("Невозможно добавить комментарий, Status = Approved");
        } else {
            log.info("Невозможно добавить комментарий: " +
                            "непредвиденное состояние бронирования. userId: {}, itemId: {}, bookingId: {}",
                    userId, itemId, booking.getId());
            throw new InternalException("Ошибка сервера: непредвиденное состояние бронирования");
        }
    }

    @Transactional
    @Override
    public ItemDto update(ItemDto itemDto, Long itemId, Long userId) {
        // Важно проверять, существует ли уже запись в БД
        // если не существует, то вместо обновления сохранится новая запись, а это не то, что мы хотим,
        // но может быть ошибка, т.к. ID назначается тут вручную и он уже может быть занят
        validateExists(itemId);
        userValidator.validateExists(userId);

        if (itemDto.getOwnerId() != null && !itemDto.getOwnerId().equals(userId)) {
            log.info("Попытка обновить Item, но ownerId: {} не сходится с userId: {}", itemDto.getOwnerId(), userId);
            throw new ConflictException("В доступе отказано, ownerId: " + itemDto.getOwnerId() +
                    " отличается от Вашего userId: " + userId);
        }

        Item item = itemRepository.findById(itemId).get();
        if (itemDto.getName() != null && !itemDto.getName().isBlank()) {
            item.setName(itemDto.getName());
        }
        if (itemDto.getDescription() != null && !itemDto.getDescription().isBlank()) {
            item.setDescription(itemDto.getDescription());
        }
        if (itemDto.getAvailable() != null) {
            item.setAvailable(itemDto.getAvailable());
        }

        log.info("Обновлён Item с id: {}", itemId);
        return getDto(item);
    }

    @Transactional
    @Override
    public void delete(Long itemId, Long userId) {
        userValidator.validateExists(userId);

        Optional<Item> findItemResult = itemRepository.findById(itemId);

        if (findItemResult.isPresent()) {
            Item item = findItemResult.get();

            if (item.getOwner() != null && !item.getOwner().getId().equals(userId)) {
                log.info("Попытка удалить Item, но ownerId: {} не сходится с userId: {}",
                        item.getOwner().getId(), userId);
                throw new ConflictException("ownerId: " + item.getOwner().getId() +
                        " отличается от Вашего userId: " + userId);
            }

            itemRepository.delete(item);
            log.info("Удалён Item с id: {}", itemId);
        } else {
            log.info("Попытка удалить несуществующий Item с id: {}", itemId);
        }
    }

    @Transactional
    @Override
    public void deleteAll() {
        if (itemRepository.findAll().isEmpty()) {
            log.info("Попытка очистить таблицу Item, но она уже пуста");
        } else {
            itemRepository.deleteAll();
            log.info("Очищено таблица Item");
        }
    }

    @Override
    public void validateExists(Long id) {
        if (itemRepository.findById(id).isEmpty()) {
            log.info("Попытка найти Item с id: {}", id);
            throw new NotFoundException("Item с id: " + id + " не найден");
        }
    }

    @Override
    public ItemDto getDto(Item entity) {
        ItemDto result = itemMapper.toDto(entity);
        //todo в будущем, вероятно, придётся lastBooking, nextBooking подтягивать.
        // пока требуется просто, чтобы эти поля были и имели значения null

        result.setComments(commentRepository.findAllByItemId(result.getId()).stream()
                .map(this::getCommentDto)
                .toList());

        return result;
    }

    private CommentDto getCommentDto(Comment entity) {
        return commentMapper.toDto(entity);
    }

    @Override
    public Item getEntity(ItemDto dto) {
        Item result = itemMapper.toEntity(dto);

        if (dto.getOwnerId() != null) {
            result.setOwner(userRepository.findById(dto.getOwnerId()).orElseThrow(() -> {
                log.info("Попытка найти User с id: {}", dto.getOwnerId());
                return new NotFoundException("Owner с id: " + dto.getOwnerId() + " не найден");
            }));
        }
        if (dto.getRequestId() != null) {
            result.setRequest(requestRepository.findById(dto.getRequestId()).orElseThrow(() -> {
                log.info("Попытка найти ItemRequest с id: {}", dto.getRequestId());
                return new NotFoundException("Request с id: " + dto.getRequestId() + " не найден");
            }));
        }

        return result;
    }

    private Comment getCommentEntity(CommentDto dto) {
        return commentMapper.toEntity(dto);
    }
}
