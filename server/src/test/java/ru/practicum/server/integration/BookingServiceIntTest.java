package ru.practicum.server.integration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.models.booking.BookingCreate;
import ru.practicum.models.booking.BookingDto;
import ru.practicum.models.booking.State;
import ru.practicum.server.booking.BookingRepository;
import ru.practicum.server.booking.BookingService;
import ru.practicum.server.item.Item;
import ru.practicum.server.item.ItemRepository;
import ru.practicum.server.user.User;
import ru.practicum.server.user.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static ru.practicum.models.booking.Status.REJECTED;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class BookingServiceIntTest {
    @Autowired
    private BookingService service;
    @Autowired
    private BookingRepository repository;
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private UserRepository userRepository;

    private final LocalDateTime start =
            LocalDateTime.of(2025, 6, 10, 14, 14, 0);
    private final LocalDateTime end =
            LocalDateTime.of(2025, 6, 17, 14, 14, 0);

    @BeforeEach
    void saveTestItemAndUser() {
        User u = User.builder()
                .name("U1")
                .email("t.1@example.com")
                .build(); // id: 1L
        userRepository.save(u);

        Item i = Item.builder()
                .name("item")
                .owner(u)
                .description("test")
                .available(true)
                .build(); // id: 1L

        itemRepository.save(i);
    }

    @Test
    void saveBooking_and_checkResult() {
        BookingCreate b = BookingCreate.builder()
                .itemId(1L)
                .start(start)
                .end(end)
                .build();

        BookingDto result = service.save(b, 1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertNotNull(result.getBooker());
        assertNotNull(result.getItem());
        assertEquals(start, result.getStart());
        assertEquals(end, result.getEnd());
    }

    @Test
    void saveBooking_and_getById() {
        BookingCreate b = BookingCreate.builder()
                .itemId(1L)
                .start(start)
                .end(end)
                .build();

        Long id = service.save(b, 1L).getId();
        BookingDto result = service.get(id, 1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertNotNull(result.getBooker());
        assertNotNull(result.getItem());
        assertEquals(start, result.getStart());
        assertEquals(end, result.getEnd());
    }

    @Test
    void saveBookings_and_getAllByStateAndBookerId() {
        BookingCreate b1 = BookingCreate.builder() // в прошлом
                .itemId(1L)
                .start(start)
                .end(end)
                .build();

        BookingCreate b2 = BookingCreate.builder() // в будущем
                .itemId(1L)
                .start(start.plusDays(20))
                .end(end.plusDays(25))
                .build();

        BookingCreate b3 = BookingCreate.builder() // в будущем
                .itemId(1L)
                .start(start.plusDays(10))
                .end(end.plusDays(10))
                .build();

        service.save(b1, 1L);
        service.save(b2, 1L);
        service.save(b3, 1L);

        List<BookingDto> result = service.getAllByStateAndBookerId(State.FUTURE, 1L);

        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    void saveBookings_and_getAllByStateAndOwnerId() {
        BookingCreate b1 = BookingCreate.builder() // в прошлом
                .itemId(1L)
                .start(start)
                .end(end)
                .build();

        BookingCreate b2 = BookingCreate.builder() // в будущем
                .itemId(1L)
                .start(start.plusDays(20))
                .end(end.plusDays(25))
                .build();

        BookingCreate b3 = BookingCreate.builder() // в будущем
                .itemId(1L)
                .start(start.plusDays(10))
                .end(end.plusDays(10))
                .build();

        service.save(b1, 1L);
        service.save(b2, 1L);
        service.save(b3, 1L);

        List<BookingDto> result = service.getAllByStateAndOwnerId(State.ALL, 1L);

        assertNotNull(result);
        assertEquals(3, result.size());
    }

    @Test
    void saveBooking_and_approveBooking() {
        BookingCreate b = BookingCreate.builder() // в прошлом
                .itemId(1L)
                .start(start)
                .end(end)
                .build();

        Long id = service.save(b, 1L).getId();
        BookingDto result = service.approveBooking(id, 1L, false);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertNotNull(result.getBooker());
        assertNotNull(result.getItem());
        assertEquals(start, result.getStart());
        assertEquals(end, result.getEnd());
        assertEquals(REJECTED, result.getStatus());
    }
}
