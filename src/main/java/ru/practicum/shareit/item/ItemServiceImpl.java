package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ItemServiceImpl implements ItemService {
    private final ItemStorage itemStorage;
    private final ItemMapper itemMapper;

    @Override
    public ItemDto get(Long id) {
        ItemDto result = itemMapper.toDto(itemStorage.get(id));
        log.info("Результат получения Item по id был приведён в ItemDto объект и передан в контроллер");
        return result;
    }

    @Override
    public List<ItemDto> getAll() {
        List<ItemDto> result = itemStorage.getAll().stream().map(itemMapper::toDto).toList();
        log.info("Результат полечения всех Item был приведён в список ItemDto объектов и передан в контроллер");
        return result;
    }

    @Override
    public Item save(ItemDto itemDto, Long userId) {
        return null;
    }

    @Override
    public Item update(Item item) {
        return null;
    }

    @Override
    public boolean delete(Long id) {
        return false;
    }

    @Override
    public boolean deleteAll() {
        return false;
    }
}
