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
import ru.practicum.gateway.exception.BadRequestParamException;
import ru.practicum.gateway.utils.ShareItHeadersBuilder;
import ru.practicum.models.booking.BookingCreate;
import ru.practicum.models.booking.BookingDto;
import ru.practicum.models.booking.State;
import ru.practicum.models.markers.OnCreate;

import java.util.List;

@RestController
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@RequestMapping(path = "/bookings")
@Validated
@Slf4j
public class BookingController {
    private final RestTemplate restTemplate;
    private final ShareItHeadersBuilder headersBuilder;

    @Value("${shareit.server-url}")
    private String serverUrl;

    @GetMapping("/{bookingId}")
    public ResponseEntity<BookingDto> get(@PathVariable
                                          @NotNull(message = "ошибка валидации, id не может быть null")
                                          @Positive(message = "ошибка валидации, id должно быть положительным числом")
                                          Long bookingId,
                                          @RequestHeader("X-Sharer-User-Id")
                                          @Positive(message = "ошибка валидации, " +
                                                  "bookerOrItemOwnerId должно быть положительным числом")
                                          Long bookerOrItemOwnerId) {
        log.info("BookingController: Начал выполнение метода get");
        String url = serverUrl + "/bookings/" + bookingId;

        HttpEntity<Void> entity = new HttpEntity<>(headersBuilder.getUserIdHeader(bookerOrItemOwnerId));
        ResponseEntity<BookingDto> result = restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                BookingDto.class
        );

        log.info("BookingController: Получил ResponseEntity<BookingDto> от сервера по ссылке: {}. Status: {}, Body: {}",
                url, result.getStatusCode(), result.getBody());

        return result;
    }

    @GetMapping
    public ResponseEntity<List<BookingDto>> getAllByStateAndBookerId(
            @RequestParam(name = "state", required = false, defaultValue = "ALL")
            String param,
            @RequestHeader("X-Sharer-User-Id")
            @Positive(message = "ошибка валидации, bookerId должно быть положительным числом")
            Long bookerId) {
        log.info("BookingController: Начал выполнение метода getAllByStateAndBookerId");

        if (State.stateValue(param) == null) {
            throw new BadRequestParamException("ошибка валидации, неконвертируемое значение state");
        }

        String url = UriComponentsBuilder
                .fromHttpUrl(serverUrl + "/bookings")
                .queryParam("state", param)
                .toUriString();

        HttpEntity<Void> entity = new HttpEntity<>(headersBuilder.getUserIdHeader(bookerId));
        ResponseEntity<List<BookingDto>> result = restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                new ParameterizedTypeReference<List<BookingDto>>() {
                }
        );

        log.info("BookingController: Получил ResponseEntity<List<BookingDto>> " +
                        "от сервера по ссылке: {}. Status: {}, Body: {}",
                url, result.getStatusCode(), result.getBody());

        return result;
    }

    @GetMapping("/owner")
    public ResponseEntity<List<BookingDto>> getAllByStateAndOwnerId(
            @RequestParam(name = "state", defaultValue = "ALL")
            String param,
            @RequestHeader("X-Sharer-User-Id")
            @Positive(message = "ошибка валидации, ownerId должно быть положительным числом")
            Long ownerId) {
        log.info("BookingController: Начал выполнение метода getAllByStateAndOwnerId");

        if (State.stateValue(param) == null) {
            throw new BadRequestParamException("ошибка валидации, неконвертируемое значение state");
        }

        String url = UriComponentsBuilder
                .fromHttpUrl(serverUrl + "/bookings/owner")
                .queryParam("state", param)
                .toUriString();

        HttpEntity<Void> entity = new HttpEntity<>(headersBuilder.getUserIdHeader(ownerId));
        ResponseEntity<List<BookingDto>> result = restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                new ParameterizedTypeReference<List<BookingDto>>() {
                }
        );

        log.info("BookingController: Получил ResponseEntity<List<BookingDto>> " +
                        "от сервера по ссылке: {}. Status: {}, Body: {}",
                url, result.getStatusCode(), result.getBody());

        return result;
    }

    @PostMapping
    public ResponseEntity<BookingDto> save(@Validated(OnCreate.class)
                                           @RequestBody
                                           BookingCreate bookingCreate,
                                           @RequestHeader("X-Sharer-User-Id")
                                           @Positive(message = "ошибка валидации, " +
                                                   "bookerId должно быть положительным числом")
                                           Long bookerId) {
        log.info("BookingController: Начал выполнение метода save");
        String url = serverUrl + "/bookings";

        HttpEntity<BookingCreate> entity = new HttpEntity<>(bookingCreate, headersBuilder.getUserIdHeader(bookerId));
        ResponseEntity<BookingDto> result = restTemplate.exchange(
                url,
                HttpMethod.POST,
                entity,
                BookingDto.class
        );
        log.info("BookingController: Получил ResponseEntity<BookingDto> от сервера по ссылке: {}. Status: {}, Body: {}",
                url, result.getStatusCode(), result.getBody());

        return result;
    }

    @PatchMapping("/{bookingId}")
    public ResponseEntity<BookingDto> approveBooking(@PathVariable
                                                     @NotNull(message = "ошибка валидации, id не может быть null")
                                                     @Positive(message = "ошибка валидации, " +
                                                             "id должно быть положительным числом")
                                                     Long bookingId,
                                                     @RequestHeader("X-Sharer-User-Id")
                                                     @Positive(message = "ошибка валидации, " +
                                                             "ownerId должно быть положительным числом")
                                                     Long ownerId,
                                                     @RequestParam
                                                     @NotNull(message = "ошибка валидации, " +
                                                             "approved не может быть null")
                                                     Boolean approved) {
        log.info("BookingController: Начал выполнение метода approveBooking");
        String url = UriComponentsBuilder
                .fromHttpUrl(serverUrl + "/bookings/" + bookingId)
                .queryParam("approved", approved)
                .toUriString();

        HttpEntity<Void> entity = new HttpEntity<>(headersBuilder.getUserIdHeader(ownerId));
        ResponseEntity<BookingDto> result = restTemplate.exchange(
                url,
                HttpMethod.PATCH,
                entity,
                BookingDto.class
        );
        log.info("BookingController: Получил ResponseEntity<BookingDto> от сервера по ссылке: {}. Status: {}, Body: {}",
                url, result.getStatusCode(), result.getBody());

        return result;
    }
}
