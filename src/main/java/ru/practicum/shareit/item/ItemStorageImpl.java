package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
@Slf4j
public class ItemStorageImpl implements ItemStorage {
    @Override
    public Long nextId() {
        return 0L;
    }

    @Override
    public Item get(Long id) {
        return null;
    }

    @Override
    public List<Item> getAll() {
        return List.of();
    }

    @Override
    public Item save(Item item) {
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

    @Override
    public void validateExists(Long id) {

    }
}
