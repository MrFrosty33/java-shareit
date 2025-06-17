package ru.practicum.server.request;

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
import ru.practicum.models.request.CreateItemRequestDto;
import ru.practicum.models.request.ItemRequestDto;

import java.util.List;

@RestController
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@RequestMapping(path = "/requests")
@Validated
public class ItemRequestController {
    private final ItemRequestService requestService;

    @GetMapping
    public List<ItemRequestDto> getAllByUserId(@RequestHeader("X-Sharer-User-Id") Long userId) {
        return requestService.getAllByUserId(userId);
    }

    @GetMapping("/{requestId}")
    public ItemRequestDto getByRequestId(@RequestHeader("X-Sharer-User-Id")
                                             Long userId,
                                         @PathVariable(name = "requestId") Long requestId) {
        return requestService.getByRequestId(userId, requestId);
    }

    @GetMapping("/all")
    public List<ItemRequestDto> getOthersRequests(@RequestHeader("X-Sharer-User-Id") Long userId) {
        return requestService.getOthersRequests(userId);
    }

    @PostMapping
    public CreateItemRequestDto save(@RequestHeader("X-Sharer-User-Id")
                                         Long userId,
                                     @RequestBody CreateItemRequestDto request) {
        return requestService.save(userId, request);
    }
}
