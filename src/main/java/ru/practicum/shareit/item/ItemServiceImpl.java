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
    public Item get(Long itemId, Long userId) {
        validateItemExists(itemId);

        // userId поступает в заголовке, но пока никак не применяется?
        // по ТЗ информацию о предмете может получить любой пользователь
        userService.validateUserExists(userId);

        Item result = itemStorage.get(itemId);
        log.info("Получен Item с id: {}", itemId);
        return result;
    }

    @Override
    public List<Item> getAllItemsByUserId(Long userId) {
        userService.validateUserExists(userId);

        List<Item> result = itemStorage.getAll().stream().filter(item -> item.getOwnerId().equals(userId)).toList();
        log.info("Получен список всех Item у пользователя с id: {}", userId);
        return result;
    }

    @Override
    public List<Item> search(String text, Long userId) {
        // если текст пуст, то возвращаем пустой список? Или стоит всё же что-то искать?
        if (text.isEmpty()) return List.of();
        String textEdited = text.toLowerCase();

        List<Item> result = getAllItemsByUserId(userId).stream()
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
    public Item save(ItemDto itemDto, Long userId) {
        userService.validateUserExists(userId);
        itemDto.setOwnerId(userId);
        Item result = itemStorage.save(itemMapper.fromDto(itemDto));
        log.info("Сохранён Item с id: {}", result.getId());
        return result;
    }

    @Override
    public Item update(ItemDto itemDto, Long itemId, Long userId) {
        userService.validateUserExists(userId);

        // я правильно понимаю, что на данный момент проверка на владельца вещи должна выглядеть примерно так?
        if (itemDto.getOwnerId() != null && !itemDto.getOwnerId().equals(userId)) {
            log.info("Попытка обновить Item, но ownerId: {} не сходится с userId: {}", itemDto.getOwnerId(), userId);
            throw new ConflictException("ownerId: " + itemDto.getOwnerId() +
                    " отличается от переданного userId: " + userId);
        }

        Item result = itemStorage.update(itemMapper.fromDto(itemDto, itemId));
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
