package ru.practicum.gateway.controller;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import ru.practicum.gateway.utils.ShareItHeadersBuilder;
import ru.practicum.models.markers.OnCreate;
import ru.practicum.models.request.CreateItemRequestDto;
import ru.practicum.models.request.ItemRequestDto;

import java.util.List;

@RestController
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@RequestMapping(path = "/requests")
@Validated
@Slf4j
public class ItemRequestController {
    private final RestTemplate restTemplate;
    private final ShareItHeadersBuilder headersBuilder;

    @Value("${shareit.server-url}")
    private String serverUrl;

    @GetMapping
    public ResponseEntity<List<ItemRequestDto>> getAllByUserId(@RequestHeader("X-Sharer-User-Id")
                                                               @NotNull(message = "ошибка валидации, userId не может быть null")
                                                               @Positive(message = "ошибка валидации, userId должно быть положительным числом")
                                                               Long userId) {
        log.info("ItemRequestController: Начал выполнение метода getAllByUserId");
        String url = serverUrl + "/requests";

        HttpEntity<Void> entity = new HttpEntity<>(headersBuilder.getUserIdHeader(userId));
        ResponseEntity<List<ItemRequestDto>> result = restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                new ParameterizedTypeReference<List<ItemRequestDto>>() {
                }
        );

        log.info("ItemRequestController: Получил ResponseEntity<List<ItemRequestDto>> от сервера по ссылке: {}. Status: {}, Body: {}",
                url, result.getStatusCode(), result.getBody());

        return result;
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<ItemRequestDto> getByRequestId(@RequestHeader("X-Sharer-User-Id")
                                                         @NotNull(message = "ошибка валидации, userId не может быть null")
                                                         @Positive(message = "ошибка валидации, userId должно быть положительным числом")
                                                         Long userId,
                                                         @PathVariable(name = "requestId") Long requestId) {
        log.info("ItemRequestController: Начал выполнение метода getByRequestId");
        String url = serverUrl + "/requests/" + requestId;


        HttpEntity<Void> entity = new HttpEntity<>(headersBuilder.getUserIdHeader(userId));
        ResponseEntity<ItemRequestDto> result = restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                ItemRequestDto.class
        );

        log.info("ItemRequestController: Получил ResponseEntity<ItemRequestDto> от сервера по ссылке: {}. Status: {}, Body: {}",
                url, result.getStatusCode(), result.getBody());

        return result;
    }

    @GetMapping("/all")
    public ResponseEntity<List<ItemRequestDto>> getOthersRequests(@RequestHeader("X-Sharer-User-Id")
                                                                  @NotNull(message = "ошибка валидации, userId не может быть null")
                                                                  @Positive(message = "ошибка валидации, userId должно быть положительным числом")
                                                                  Long userId) {
        log.info("ItemRequestController: Начал выполнение метода getOthersRequests");
        String url = serverUrl + "/requests/all";


        HttpEntity<Void> entity = new HttpEntity<>(headersBuilder.getUserIdHeader(userId));
        ResponseEntity<List<ItemRequestDto>> result = restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                new ParameterizedTypeReference<List<ItemRequestDto>>() {
                }
        );

        log.info("ItemRequestController: Получил ResponseEntity<List<ItemRequestDto>> от сервера по ссылке: {}. Status: {}, Body: {}",
                url, result.getStatusCode(), result.getBody());

        return result;
    }

    @PostMapping
    public ResponseEntity<CreateItemRequestDto> save(@RequestHeader("X-Sharer-User-Id")
                                                     @NotNull(message = "ошибка валидации, userId не может быть null")
                                                     @Positive(message = "ошибка валидации, userId должно быть положительным числом")
                                                     Long userId,
                                                     @RequestBody @Validated(OnCreate.class) CreateItemRequestDto request) {
        log.info("ItemRequestController: Начал выполнение метода save");
        String url = serverUrl + "/requests";

        HttpEntity<CreateItemRequestDto> entity = new HttpEntity<>(request, headersBuilder.getUserIdHeader(userId));
        ResponseEntity<CreateItemRequestDto> result = restTemplate.exchange(
                url,
                HttpMethod.POST,
                entity,
                CreateItemRequestDto.class
        );
        log.info("ItemRequestController: Получил ResponseEntity<CreateItemRequestDto> от сервера по ссылке: {}. Status: {}, Body: {}",
                url, result.getStatusCode(), result.getBody());

        return result;
    }
}
