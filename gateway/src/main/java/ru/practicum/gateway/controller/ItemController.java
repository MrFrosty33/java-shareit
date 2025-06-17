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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import ru.practicum.gateway.utils.ShareItHeadersBuilder;
import ru.practicum.models.item.CommentDto;
import ru.practicum.models.item.ItemDto;
import ru.practicum.models.markers.OnCreate;
import ru.practicum.models.markers.OnUpdate;

import java.util.List;

@RestController
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@RequestMapping("/items")
@Validated
@Slf4j
public class ItemController {
    private final RestTemplate restTemplate;
    private final ShareItHeadersBuilder headersBuilder;

    @Value("${shareit.server-url}")
    private String serverUrl;

    @GetMapping("/{itemId}")
    public ResponseEntity<ItemDto> get(@PathVariable
                                       @NotNull(message = "ошибка валидации, id не может быть null")
                                       @Positive(message = "ошибка валидации, id должно быть положительным числом")
                                       Long itemId,
                                       @RequestHeader("X-Sharer-User-Id")
                                       @Positive(message = "ошибка валидации, userId должно быть положительным числом")
                                       Long userId) {
        log.info("ItemController: Начал выполнение метода getByRequestId");
        String url = serverUrl + "/items/" + itemId;

        HttpEntity<Void> entity = new HttpEntity<>(headersBuilder.getUserIdHeader(userId));
        ResponseEntity<ItemDto> result = restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                ItemDto.class
        );

        log.info("ItemController: ResponseEntity<ItemDto> от сервера по ссылке: {}. Status: {}, Body: {}",
                url, result.getStatusCode(), result.getBody());

        return result;
    }

    @GetMapping
    public ResponseEntity<List<ItemDto>> getAllItemsByUserId(@RequestHeader("X-Sharer-User-Id")
                                                             @NotNull(message = "ошибка валидации, userId не может быть null")
                                                             @Positive(message = "ошибка валидации, userId должно быть положительным числом")
                                                             Long userId) {
        log.info("ItemController: Начал выполнение метода getAllItemsByUserId");
        String url = serverUrl + "/items";

        HttpEntity<Void> entity = new HttpEntity<>(headersBuilder.getUserIdHeader(userId));
        ResponseEntity<List<ItemDto>> result = restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                new ParameterizedTypeReference<List<ItemDto>>() {
                }
        );

        log.info("ItemController: ResponseEntity<List<ItemDto>> от сервера по ссылке: {}. Status: {}, Body: {}",
                url, result.getStatusCode(), result.getBody());

        return result;
    }

    @GetMapping("/search")
    public ResponseEntity<List<ItemDto>> search(@RequestParam(name = "text")
                                                String text,
                                                @RequestHeader("X-Sharer-User-Id")
                                                @Positive(message = "ошибка валидации, userId должно быть положительным числом")
                                                Long userId) {
        log.info("ItemController: Начал выполнение метода search");
        String url = UriComponentsBuilder
                .fromHttpUrl(serverUrl + "items/search")
                .queryParam("text", text)
                .toUriString();

        HttpEntity<Void> entity = new HttpEntity<>(headersBuilder.getUserIdHeader(userId));
        ResponseEntity<List<ItemDto>> result = restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                new ParameterizedTypeReference<List<ItemDto>>() {
                }
        );

        log.info("ItemController: ResponseEntity<List<ItemDto>> от сервера по ссылке: {}. Status: {}, Body: {}",
                url, result.getStatusCode(), result.getBody());

        return result;
    }

    @PostMapping
    public ResponseEntity<ItemDto> save(@Validated(OnCreate.class)
                                        @RequestBody
                                        ItemDto itemDto,
                                        @RequestHeader("X-Sharer-User-Id")
                                        @Positive(message = "ошибка валидации, userId должно быть положительным числом")
                                        Long userId) {
        log.info("ItemController: Начал выполнение метода save");
        String url = serverUrl + "/items";

        HttpEntity<ItemDto> entity = new HttpEntity<>(itemDto, headersBuilder.getUserIdHeader(userId));
        ResponseEntity<ItemDto> result = restTemplate.exchange(
                url,
                HttpMethod.POST,
                entity,
                ItemDto.class
        );
        log.info("ItemController: ResponseEntity<ItemDto> от сервера по ссылке: {}. Status: {}, Body: {}",
                url, result.getStatusCode(), result.getBody());

        return result;
    }

    // нужен ли тут RequestHeader, или же id автора берём из полученного Dto объекта?
    @PostMapping("/{itemId}/comment")
    public ResponseEntity<CommentDto> addComment(@Validated @RequestBody CommentDto commentDto,
                                                 @PathVariable
                                                 @NotNull(message = "ошибка валидации, itemId не может быть null")
                                                 @Positive(message = "ошибка валидации, itemId должно быть положительным числом")
                                                 Long itemId,
                                                 @RequestHeader("X-Sharer-User-Id")
                                                 @Positive(message = "ошибка валидации, userId должно быть положительным числом")
                                                 Long userId) {
        log.info("ItemController: Начал выполнение метода addComment");
        String url = serverUrl + "/items/" + itemId + "/comment";


        HttpEntity<CommentDto> entity = new HttpEntity<>(commentDto, headersBuilder.getUserIdHeader(userId));
        ResponseEntity<CommentDto> result = restTemplate.exchange(
                url,
                HttpMethod.POST,
                entity,
                CommentDto.class
        );
        log.info("ItemController: ResponseEntity<CommentDto> от сервера по ссылке: {}. Status: {}, Body: {}",
                url, result.getStatusCode(), result.getBody());

        return result;
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<ItemDto> update(@Validated(OnUpdate.class) @RequestBody ItemDto itemDto,
                                          @PathVariable
                                          @NotNull(message = "ошибка валидации, itemId не может быть null")
                                          @Positive(message = "ошибка валидации, itemId должно быть положительным числом")
                                          Long itemId,
                                          @RequestHeader("X-Sharer-User-Id")
                                          @Positive(message = "ошибка валидации, userId должно быть положительным числом")
                                          Long userId) {
        log.info("ItemController: Начал выполнение метода update");
        String url = serverUrl + "/items/" + itemId;

        HttpEntity<ItemDto> entity = new HttpEntity<>(itemDto, headersBuilder.getUserIdHeader(userId));
        ResponseEntity<ItemDto> result = restTemplate.exchange(
                url,
                HttpMethod.PATCH,
                entity,
                ItemDto.class
        );
        log.info("ItemController: ResponseEntity<ItemDto> от сервера по ссылке: {}. Status: {}, Body: {}",
                url, result.getStatusCode(), result.getBody());

        return result;
    }

    @DeleteMapping("/{itemId}")
    public void delete(@PathVariable
                       @NotNull(message = "ошибка валидации, id не может быть null")
                       @Positive(message = "ошибка валидации, id должно быть положительным числом")
                       Long itemId,
                       @RequestHeader("X-Sharer-User-Id")
                       @NotNull(message = "ошибка валидации, userId не может быть null")
                       @Positive(message = "ошибка валидации, userId должно быть положительным числом")
                       Long userId) {
        log.info("ItemController: Начал выполнение метода delete");
        String url = serverUrl + "/items/" + itemId;

        HttpEntity<Void> entity = new HttpEntity<>(headersBuilder.getUserIdHeader(userId));
        ResponseEntity<Void> result = restTemplate.exchange(
                url,
                HttpMethod.DELETE,
                entity,
                Void.class
        );
        log.info("ItemController: Выполнил метод delete с id: {}. Status: {}", itemId, result.getStatusCode());
    }

    @DeleteMapping
    public void deleteAll() {
        log.info("ItemController: Начал выполнение метода deleteAll");
        String url = serverUrl + "/items";

        ResponseEntity<Void> result = restTemplate.exchange(
                url,
                HttpMethod.DELETE,
                null,
                Void.class
        );
        log.info("ItemController: Выполнил метод deleteAll. Status: {}", result.getStatusCode());
    }
}
