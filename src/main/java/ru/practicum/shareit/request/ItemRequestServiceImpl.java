package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.utilities.ExistenceValidator;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ItemRequestServiceImpl implements ItemRequestService, ExistenceValidator<ItemRequest> {
    private final ItemRequestRepository requestRepository;

    @Override
    public ItemRequestDto get(Long userId) {
        return null;
    }

    @Override
    public ItemRequestDto getById(Long userId, Long requestId) {
        return null;
    }

    @Override
    public List<ItemRequestDto> getAllByUserId(Long userId) {
        return List.of();
    }

    @Override
    @Transactional
    public ItemRequestDto save(ItemRequestDto itemRequest) {
        return null;
    }

    @Override
    public void validateExists(Long id) {
        if (requestRepository.findById(id).isEmpty()) {
            log.info("Попытка найти несуществующий ItemRequest с id: {}", id);
            throw new NotFoundException("ItemRequest с id: " + id + " не найден");
        }
    }
}
