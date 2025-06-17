package ru.practicum.server.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.models.request.CreateItemRequestDto;
import ru.practicum.models.request.ItemRequestAnswer;
import ru.practicum.models.request.ItemRequestDto;
import ru.practicum.server.exception.InternalServerException;
import ru.practicum.server.exception.NotFoundException;
import ru.practicum.server.item.Item;
import ru.practicum.server.item.ItemRepository;
import ru.practicum.server.user.User;
import ru.practicum.server.user.UserRepository;
import ru.practicum.server.utilities.ExistenceValidator;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ItemRequestServiceImpl implements ItemRequestService, ExistenceValidator<ItemRequest> {
    private final ItemRequestRepository requestRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final ExistenceValidator<User> userValidator;
    private final ItemRequestMapper mapper;

    @Override
    public List<ItemRequestDto> getOthersRequests(Long userId) {
        userValidator.validateExists(userId);
        List<ItemRequestDto> result = requestRepository.findByRequesterIdNotOrderByCreatedAsc(userId).stream()
                .map(this::getDtoWithAnswers)
                .toList();
        log.info("User с id: {} получил список чужих ItemRequest. " +
                "Список отсортирован по дате от новых к старым", userId);
        return result;
    }

    @Override
    public ItemRequestDto getByRequestId(Long userId, Long requestId) {
        userValidator.validateExists(userId);
        validateExists(requestId);
        ItemRequestDto result = getDtoWithAnswers(requestRepository.findById(requestId).get());
        log.info("User с id: {}, посмотрел информацию о ItemRequest с id: {}", userId, requestId);
        return result;
    }

    @Override
    public List<ItemRequestDto> getAllByUserId(Long userId) {
        userValidator.validateExists(userId);
        List<ItemRequestDto> result = requestRepository.findByRequesterIdOrderByCreatedAsc(userId).stream()
                .map(this::getDtoWithAnswers)
                .toList();
        log.info("Получен список всех ItemRequest вместе с найденными желаемыми вещами" +
                " у User с id: {}. Список отсортирован по дате от новых к старым", userId);
        return result;
    }

    @Override
    @Transactional
    public CreateItemRequestDto save(Long requesterId, CreateItemRequestDto itemRequest) {
        // может несколько запутано, но идея в том,
        // чтобы при создании нового запроса не возвращать список подходящих предметов,
        // а вернуть только более простой DTO, с id, описанием и id его создателя.
        userValidator.validateExists(requesterId);
        ItemRequestDto dto = mapper.mapDtoFromCreateRequest(itemRequest);
        dto.setRequesterId(requesterId);
        dto.setCreated(LocalDateTime.now());

        CreateItemRequestDto result = mapper.mapCreateRequestFromEntity(requestRepository.save(getEntity(dto)));
        log.info("Сохранён ItemRequest с id: {}", result.getId());
        return result;
    }

    private Set<ItemRequestAnswer> findAnswers(Long requestId) {
        List<Item> queryResult = itemRepository.findByRequestId(requestId);
        log.info("Был найден список предметов по requestId: {}, преобразован в Set<ItemRequestAnswer> и передан далее",
                requestId);
        return queryResult.stream().map(mapper::mapAnswerFromItemEntity).collect(Collectors.toSet());
    }

    @Override
    public ItemRequestDto getDtoWithAnswers(ItemRequest entity) {
        ItemRequestDto result = mapper.toDto(entity);
        result.setItems(findAnswers(result.getId()));
        return result;
    }

    @Override
    public ItemRequest getEntity(ItemRequestDto dto) {
        ItemRequest result = mapper.toEntity(dto);

        if (dto.getRequesterId() != null) {
            result.setRequester(userRepository.findById(dto.getRequesterId()).orElseThrow(() -> {
                log.info("Попытка найти User с id: {}", dto.getRequesterId());
                return new NotFoundException("Owner с id: " + dto.getRequesterId() + " не найден");
            }));
        } else {
            log.info("Во время выполнения метода ItemRequestServiceImpl.getEntity(ItemRequestDto) " +
                    "произошла ошибка. requesterId == null");
            throw new InternalServerException
                    ("Ошибка: переданный DTO объект не содержит requesterId");
        }
        return result;
    }

    @Override
    public void validateExists(Long id) {
        if (requestRepository.findById(id).isEmpty()) {
            log.info("Попытка найти несуществующий ItemRequest с id: {}", id);
            throw new NotFoundException("ItemRequest с id: " + id + " не найден");
        }
    }
}
