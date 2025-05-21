package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.ConflictException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.user.UserService;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ItemServiceImpl implements ItemService {
    private final ItemStorage itemStorage;
    private final UserService userService;
    private final ItemMapper itemMapper;

    @Override
    public ItemDto get(Long itemId, Long userId) {
        validateItemExists(itemId);

        // userId поступает в заголовке, но пока никак не применяется?
        // по ТЗ информацию о предмете может получить любой пользователь
        userService.validateUserExists(userId);

        ItemDto result = itemMapper.toDto(itemStorage.get(itemId));
        log.info("Получен Item с id: {}", itemId);
        return result;
    }

    @Override
    public List<ItemDto> getAllItemsByUserId(Long userId) {
        userService.validateUserExists(userId);

        List<ItemDto> result = itemStorage.getAll().stream()
                .filter(item -> item.getOwnerId().equals(userId))
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
                "содержащие text: {} в имени или описании экземпляры со статусом available: true были переданы далее", text);
        return result;
    }

    @Override
    public ItemDto save(ItemDto itemDto, Long userId) {
        userService.validateUserExists(userId);
        itemDto.setOwnerId(userId);
        ItemDto result = itemMapper.toDto(itemStorage.save(itemMapper.fromDto(itemDto)));
        log.info("Сохранён Item с id: {}", result.getId());
        return result;
    }

    @Override
    public ItemDto update(ItemDto itemDto, Long itemId, Long userId) {
        userService.validateUserExists(userId);

        // я правильно понимаю, что на данный момент проверка на владельца вещи должна выглядеть примерно так?
        if (itemDto.getOwnerId() != null && !itemDto.getOwnerId().equals(userId)) {
            log.info("Попытка обновить Item, но ownerId: {} не сходится с userId: {}", itemDto.getOwnerId(), userId);
            throw new ConflictException("ownerId: " + itemDto.getOwnerId() +
                    " отличается от переданного userId: " + userId);
        }

        ItemDto result = itemMapper.toDto(itemStorage.update(itemMapper.fromDto(itemDto, itemId)));
        log.info("Обновлён Item с id: {}", itemId);
        return result;
    }

    @Override
    public boolean delete(Long itemId, Long userId) {
        validateItemExists(itemId);
        userService.validateUserExists(userId);
        boolean result = itemStorage.delete(itemId);
        log.info("Удалён Item с id: {}", itemId);
        return result;
    }

    @Override
    public boolean deleteAll() {
        boolean result = itemStorage.deleteAll();
        log.info("Очищено хранилище Item");
        return result;
    }

    @Override
    public void validateItemExists(Long id) {
        if (!itemStorage.getIds().contains(id)) {
            log.info("Попытка найти Item с id: {}", id);
            throw new NotFoundException("Item с id: " + id + " не найден");
        }
    }
}
