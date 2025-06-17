package ru.practicum.gateway.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import ru.practicum.models.markers.OnCreate;
import ru.practicum.models.user.UserDto;

@RestController
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@RequestMapping(path = "/users")
@Validated
@Slf4j
public class UserController {
    private final RestTemplate restTemplate;

    @Value("${SHAREIT_SERVER_URL}")
    private String serverUrl;

//    @GetMapping("/{id}")
//    public UserDto get(@PathVariable
//                       @NotNull(message = "ошибка валидации, id не может быть null")
//                       @Positive(message = "ошибка валидации, id должно быть положительным числом")
//                       Long id) {
//        return userService.get(id);
//    }
//
//    @GetMapping
//    public List<UserDto> getAll() {
//        return userService.getAll();
//}

    @PostMapping
    public ResponseEntity<UserDto> save(@Validated(OnCreate.class) @RequestBody UserDto userDto) {
        log.info("Начал выполнение метода save");
        String url = serverUrl + "/users";

        ResponseEntity<UserDto> result = restTemplate.postForEntity(url, userDto, UserDto.class);

        log.info("Выполнил метод save");
        return ResponseEntity.status(result.getStatusCode()).body(result.getBody());

    }

//    @PatchMapping("/{id}")
//    public UserDto update(@Validated(OnUpdate.class) @RequestBody UserDto userDto,
//                          @PathVariable
//                          @NotNull(message = "ошибка валидации, id не может быть null")
//                          @Positive(message = "ошибка валидации, id должно быть положительным числом")
//                          Long id) {
//        return userService.update(userDto, id);
//    }
//
//    @DeleteMapping("/{id}")
//    public void delete(@PathVariable
//                         @NotNull(message = "ошибка валидации, id не может быть null")
//                         @Positive(message = "ошибка валидации, id должно быть положительным числом")
//                         Long id) {
//        userService.delete(id);
//    }
//
//    @DeleteMapping
//    public void deleteAll() {
//        userService.deleteAll();
//    }
}
