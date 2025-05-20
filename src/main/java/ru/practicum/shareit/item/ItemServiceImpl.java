package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.ConflictException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.user.UserStorage;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ItemServiceImpl implements ItemService {
    private final ItemStorage itemStorage;
    private final UserStorage userStorage;
    private final ItemMapper itemMapper;

    @Override
    public ItemDto get(Long id, Long userId) {
        userStorage.validateExists(userId);

        ItemDto result = itemMapper.toDto(itemStorage.get(id));
        // если другой владелец, то информацию о предмете мы не провозглашаем?
        if (!result.getOwnerId().equals(userId)) {
            log.info("Попытка получить Item, но ownerId: {} не сходится с userId: {}", result.getOwnerId(), userId);
            throw new ConflictException("ownerId: " + result.getOwnerId() +
                    " отличается от переданного userId: " + userId);
        }
        log.info("Результат получения Item по id был приведён в ItemDto объект и передан далее");
        return result;
    }

    @Override
    public List<ItemDto> getAll() {
        List<ItemDto> result = itemStorage.getAll().stream().map(itemMapper::toDto).toList();
        log.info("Результат получения всех Item был приведён в список ItemDto объектов и передан далее");
        return result;
    }

    @Override
    public List<ItemDto> search(String text) {
        List<ItemDto> result = getAll().stream()
                .filter(itemDto ->
                        itemDto.getDescription().contains(text) || itemDto.getName().contains(text)).toList();
        log.info("Список всех ItemDto был отфильтрован, " +
                "содержащие text: {} в имени или описании экземпляры были переданы далее", text);
        return result;
    }

    @Override
    public ItemDto save(ItemDto itemDto, Long userId) {
        userStorage.validateExists(userId);

        // я правильно понимаю, что на данный момент проверка на владельца вещи должна выглядеть примерно так?
        if (!itemDto.getOwnerId().equals(userId)) {
            log.info("Попытка сохранить Item, но ownerId: {} не сходится с userId: {}", itemDto.getOwnerId(), userId);
            throw new ConflictException("ownerId: " + itemDto.getOwnerId() +
                    " отличается от переданного userId: " + userId);
        }

        ItemDto result = itemMapper.toDto(itemStorage.save(itemMapper.fromDto(itemDto)));
        log.info("Результат сохранения Item был приведён в ItemDto объект и передан в контроллер");
        return result;
    }

    @Override
    public ItemDto update(ItemDto itemDto, Long itemId, Long userId) {
        userStorage.validateExists(userId);

        if (!itemDto.getOwnerId().equals(userId)) {
            log.info("Попытка обновить Item, но ownerId: {} не сходится с userId: {}", itemDto.getOwnerId(), userId);
            throw new ConflictException("ownerId: " + itemDto.getOwnerId() +
                    " отличается от переданного userId: " + userId);
        }

        ItemDto result = itemMapper.toDto(itemStorage.update(itemMapper.fromDto(itemDto, itemId)));
        log.info("Результат обновления Item был приведён в ItemDto объект и передан в контроллер");
        return result;
    }

    @Override
    public boolean delete(Long id, Long userId) {
        userStorage.validateExists(userId);
        Item item = itemStorage.get(id);

        if (!item.getOwnerId().equals(userId)) {
            log.info("Попытка удалить Item, но ownerId: {} не сходится с userId: {}", item.getOwnerId(), userId);
            throw new ConflictException("ownerId: " + item.getOwnerId() +
                    " отличается от переданного userId: " + userId);
        }
        return itemStorage.delete(id);
    }

    @Override
    public boolean deleteAll() {
        return itemStorage.deleteAll();
    }
}
