package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.ConflictException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.user.UserService;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserService userService;
    private final ItemMapper itemMapper;

    @Override
    public ItemDto get(Long itemId, Long userId) {
        validateItemExists(itemId);

        // userId поступает в заголовке, но пока никак не применяется?
        // по ТЗ информацию о предмете может получить любой пользователь
        userService.validateUserExists(userId);

        ItemDto result = itemMapper.toDto(itemRepository.findById(itemId)
                .orElseThrow(() -> {
                    log.info("Попытка найти Item с id: {}", itemId);
                    return new NotFoundException("Item с id: " + itemId + " не найден");
                }));
        log.info("Получен Item с id: {}", itemId);
        return result;
    }

    @Override
    public List<ItemDto> getAllItemsByUserId(Long userId) {
        userService.validateUserExists(userId);

        List<ItemDto> result = itemRepository.findAll().stream()
                .filter(item -> item.getOwner().getId().equals(userId))
                .map(itemMapper::toDto)
                .toList();
        log.info("Получен список всех Item у пользователя с id: {}", userId);
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
        userService.validateUserExists(userId);
        itemDto.setOwnerId(userId);
        ItemDto result = itemMapper.toDto(itemRepository.save(itemMapper.fromDto(itemDto)));
        log.info("Сохранён Item с id: {}", result.getId());
        return result;
    }

    @Transactional
    @Override
    public ItemDto update(ItemDto itemDto, Long itemId, Long userId) {
        // Важно проверять, существует ли уже запись в БД
        // если не существует, то вместо обновления сохранится новая запись, а это не то, что мы хотим,
        // но может быть ошибка, т.к. ID назначается тут вручную и он уже может быть занят

        //todo подумать ещё над методом. Если поля Null - так и перезапишется в БД...
        validateItemExists(itemId);
        userService.validateUserExists(userId);

        if (itemDto.getOwnerId() != null && !itemDto.getOwnerId().equals(userId)) {
            log.info("Попытка обновить Item, но ownerId: {} не сходится с userId: {}", itemDto.getOwnerId(), userId);
            throw new ConflictException("ownerId: " + itemDto.getOwnerId() +
                    " отличается от переданного userId: " + userId);
        }

        ItemDto result = itemMapper.toDto(itemRepository.save(itemMapper.fromDto(itemDto, itemId)));
        log.info("Обновлён Item с id: {}", itemId);
        return result;
    }

    @Transactional
    @Override
    public void delete(Long itemId, Long userId) {
        userService.validateUserExists(userId);

        Optional<Item> findItemResult = itemRepository.findById(itemId);

        if (findItemResult.isPresent()) {
            Item item = findItemResult.get();

            if (item.getOwner() != null && !item.getOwner().getId().equals(userId)) {
                log.info("Попытка удалить Item, но ownerId: {} не сходится с userId: {}",
                        item.getOwner().getId(), userId);
                throw new ConflictException("ownerId: " + item.getOwner().getId() +
                        " отличается от переданного userId: " + userId);
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
    public void validateItemExists(Long id) {
        if (itemRepository.findById(id).isEmpty()) {
            log.info("Попытка найти Item с id: {}", id);
            throw new NotFoundException("Item с id: " + id + " не найден");
        }
    }
}
