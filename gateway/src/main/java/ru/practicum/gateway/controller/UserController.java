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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import ru.practicum.models.markers.OnCreate;
import ru.practicum.models.markers.OnUpdate;
import ru.practicum.models.user.UserDto;

import java.util.List;

@RestController
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@RequestMapping(path = "/users")
@Validated
@Slf4j
public class UserController {
    private final RestTemplate restTemplate;

    @Value("${shareit.server-url}")
    private String serverUrl;

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> get(@PathVariable
                                       @NotNull(message = "ошибка валидации, id не может быть null")
                                       @Positive(message = "ошибка валидации, id должно быть положительным числом")
                                       Long id) {
        log.info("UserController: Начал выполнение метода get");
        String url = serverUrl + "/users/" + id;
        ResponseEntity<UserDto> result = restTemplate.getForEntity(url, UserDto.class);

        log.info("UserController: Получил ResponseEntity<UserDto> от сервера по ссылке: {}. Status: {}, Body: {}",
                url, result.getStatusCode(), result.getBody());

        return result;
    }

    @GetMapping
    public ResponseEntity<List<UserDto>> getAll() {
        log.info("UserController: Начал выполнение метода getAll");
        String url = serverUrl + "/users";
        ResponseEntity<List<UserDto>> result = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<UserDto>>() {
                }
        );

        log.info("UserController: Получил ResponseEntity<List<UserDto>> от сервера по ссылке: {}. Status: {}, Body: {}",
                url, result.getStatusCode(), result.getBody());

        return result;
    }

    @PostMapping
    public ResponseEntity<UserDto> save(@Validated(OnCreate.class) @RequestBody UserDto userDto) {
        log.info("UserController: Начал выполнение метода save");
        String url = serverUrl + "/users";

        ResponseEntity<UserDto> result = restTemplate.postForEntity(url, userDto, UserDto.class);
        log.info("UserController: Получил ResponseEntity<UserDto> от сервера по ссылке: {}. Status: {}, Body: {}",
                url, result.getStatusCode(), result.getBody());

        return result;

    }

    @PatchMapping("/{id}")
    public ResponseEntity<UserDto> update(@Validated(OnUpdate.class) @RequestBody UserDto userDto,
                                          @PathVariable
                                          @NotNull(message = "ошибка валидации, id не может быть null")
                                          @Positive(message = "ошибка валидации, id должно быть положительным числом")
                                          Long id) {
        log.info("UserController: Начал выполнение метода update");
        String url = serverUrl + "/users/" + id;

        HttpEntity<UserDto> entity = new HttpEntity<>(userDto);
        ResponseEntity<UserDto> result = restTemplate.exchange(url, HttpMethod.PATCH, entity, UserDto.class);

        log.info("UserController: Получил ResponseEntity<UserDto> от сервера по ссылке: {}. Status: {}, Body: {}",
                url, result.getStatusCode(), result.getBody());

        return result;
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable
                       @NotNull(message = "ошибка валидации, id не может быть null")
                       @Positive(message = "ошибка валидации, id должно быть положительным числом")
                       Long id) {
        log.info("UserController: Начал выполнение метода delete");
        String url = serverUrl + "/users/" + id;

        ResponseEntity<Void> result = restTemplate.exchange(
                url,
                HttpMethod.DELETE,
                null,
                Void.class
        );

        log.info("UserController: Выполнил метод delete с id: {}. Status: {}", id, result.getStatusCode());
    }

    @DeleteMapping
    public void deleteAll() {
        log.info("UserController: Начал выполнение метода deleteAll");
        String url = serverUrl + "/users";

        ResponseEntity<Void> result = restTemplate.exchange(
                url,
                HttpMethod.DELETE,
                null,
                Void.class
        );

        log.info("UserController: Выполнил метод deleteAll. Status: {}", result.getStatusCode());
    }
}
