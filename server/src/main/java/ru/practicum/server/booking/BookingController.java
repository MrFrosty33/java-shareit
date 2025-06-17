package ru.practicum.server.booking;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
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
import ru.practicum.server.booking.dto.BookingCreate;
import ru.practicum.server.booking.dto.BookingDto;
import ru.practicum.server.markers.OnCreate;

import java.util.List;

@RestController
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@RequestMapping(path = "/bookings")
@Validated
public class BookingController {
    private final BookingService bookingService;

    @GetMapping("/{bookingId}")
    public BookingDto get(@PathVariable
                          @NotNull(message = "ошибка валидации, id не может быть null")
                          @Positive(message = "ошибка валидации, id должно быть положительным числом")
                          Long bookingId,
                          @RequestHeader("X-Sharer-User-Id")
                          @Positive(message = "ошибка валидации, bookerOrItemOwnerId должно быть положительным числом")
                          Long bookerOrItemOwnerId) {
        return bookingService.get(bookingId, bookerOrItemOwnerId);
    }

    @GetMapping
    public List<BookingDto> getAllByStateAndBookerId(
            @RequestParam(name = "state", required = false, defaultValue = "ALL")
            String param,
            @RequestHeader("X-Sharer-User-Id")
            @Positive(message = "ошибка валидации, bookerId должно быть положительным числом")
            Long bookerId) {
        return bookingService.getAllByStateAndBookerId(State.stateValue(param), bookerId);
    }

    @GetMapping("/owner")
    public List<BookingDto> getAllByStateAndOwnerId(
            @RequestParam(name = "state", required = false, defaultValue = "ALL")
            String param,
            @RequestHeader("X-Sharer-User-Id")
            @Positive(message = "ошибка валидации, ownerId должно быть положительным числом")
            Long ownerId) {
        return bookingService.getAllByStateAndOwnerId(State.stateValue(param), ownerId);
    }

    @PostMapping
    public BookingDto save(@Validated(OnCreate.class)
                           @RequestBody
                               BookingCreate bookingCreate,
                           @RequestHeader("X-Sharer-User-Id")
                           @Positive(message = "ошибка валидации, bookerId должно быть положительным числом")
                           Long bookerId) {
        return bookingService.save(bookingCreate, bookerId);
    }

    @PatchMapping("{bookingId}")
    public BookingDto approveBooking(@PathVariable
                                     @NotNull(message = "ошибка валидации, id не может быть null")
                                     @Positive(message = "ошибка валидации, id должно быть положительным числом")
                                     Long bookingId,
                                     @RequestHeader("X-Sharer-User-Id")
                                     @Positive(message = "ошибка валидации, ownerId должно быть положительным числом")
                                     Long ownerId,
                                     @RequestParam
                                     @NotNull(message = "ошибка валидации, approved не может быть null")
                                         Boolean approved) {
        return bookingService.approveBooking(bookingId, ownerId, approved);
    }

    // update & delete пока не требуются?


}
