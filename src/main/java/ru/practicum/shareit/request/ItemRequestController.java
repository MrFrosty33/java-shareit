package ru.practicum.shareit.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.markers.OnCreate;
import ru.practicum.shareit.request.dto.CreateItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import java.util.List;

@RestController
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@RequestMapping(path = "/requests")
@Validated
public class ItemRequestController {
    private final ItemRequestService requestService;

    @GetMapping
    public List<ItemRequestDto> get(@RequestHeader("X-Sharer-User-Id")
                              @NotNull(message = "ошибка валидации, userId не может быть null")
                              @Positive(message = "ошибка валидации, userId должно быть положительным числом")
                              Long userId) {
        return requestService.get(userId);
    }

    @GetMapping("/{requestId}")
    public ItemRequestDto getById(@RequestHeader("X-Sharer-User-Id")
                                  @NotNull(message = "ошибка валидации, userId не может быть null")
                                  @Positive(message = "ошибка валидации, userId должно быть положительным числом")
                                  Long userId,
                                  @PathVariable(name = "requestId") Long requestId) {
        return requestService.getById(userId, requestId);
    }

    @GetMapping("/all")
    public List<ItemRequestDto> getAllByUserId(@RequestHeader("X-Sharer-User-Id")
                                               @NotNull(message = "ошибка валидации, userId не может быть null")
                                               @Positive(message = "ошибка валидации, userId должно быть положительным числом")
                                               Long userId) {
        return requestService.getAllByUserId(userId);
    }

    @PostMapping
    public CreateItemRequestDto save(@RequestHeader("X-Sharer-User-Id")
                               @NotNull(message = "ошибка валидации, userId не может быть null")
                               @Positive(message = "ошибка валидации, userId должно быть положительным числом")
                               Long userId,
                                     @RequestBody @Validated(OnCreate.class) CreateItemRequestDto request) {
        return requestService.save(userId, request);
    }
}
