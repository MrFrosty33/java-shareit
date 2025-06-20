package ru.practicum.server.booking;

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
import ru.practicum.models.booking.BookingCreate;
import ru.practicum.models.booking.BookingDto;
import ru.practicum.models.booking.State;
import ru.practicum.models.markers.OnCreate;

import java.util.List;

@RestController
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@RequestMapping(path = "/bookings")
@Validated
public class BookingController {
    private final BookingService bookingService;

    @GetMapping("/{bookingId}")
    public BookingDto get(@PathVariable
                          Long bookingId,
                          @RequestHeader("X-Sharer-User-Id")
                          Long bookerOrItemOwnerId) {
        return bookingService.get(bookingId, bookerOrItemOwnerId);
    }

    @GetMapping
    public List<BookingDto> getAllByStateAndBookerId(
            @RequestParam
            String state,
            @RequestHeader("X-Sharer-User-Id")
            Long bookerId) {
        return bookingService.getAllByStateAndBookerId(State.stateValue(state), bookerId);
    }

    @GetMapping("/owner")
    public List<BookingDto> getAllByStateAndOwnerId(
            @RequestParam
            String state,
            @RequestHeader("X-Sharer-User-Id")
            Long ownerId) {
        return bookingService.getAllByStateAndOwnerId(State.stateValue(state), ownerId);
    }

    @PostMapping
    public BookingDto save(@Validated(OnCreate.class)
                           @RequestBody
                               BookingCreate bookingCreate,
                           @RequestHeader("X-Sharer-User-Id")
                           Long bookerId) {
        return bookingService.save(bookingCreate, bookerId);
    }

    @PatchMapping("/{bookingId}")
    public BookingDto approveBooking(@PathVariable
                                     Long bookingId,
                                     @RequestHeader("X-Sharer-User-Id")
                                     Long ownerId,
                                     @RequestParam
                                     Boolean approved) {
        return bookingService.approveBooking(bookingId, ownerId, approved);
    }
}
