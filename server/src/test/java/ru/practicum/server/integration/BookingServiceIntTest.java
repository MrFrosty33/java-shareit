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
import ru.practicum.server.booking.BookingRepository;
import ru.practicum.server.booking.BookingService;
import ru.practicum.server.item.Item;
import ru.practicum.server.item.ItemRepository;
import ru.practicum.server.user.User;
import ru.practicum.server.user.UserRepository;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

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
        LocalDateTime start = LocalDateTime.of(2025, 6, 10, 14, 14, 0);
        LocalDateTime end = LocalDateTime.of(2025, 6, 17, 14, 14, 0);

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
        LocalDateTime start = LocalDateTime.of(2025, 6, 10, 14, 14, 0);
        LocalDateTime end = LocalDateTime.of(2025, 6, 17, 14, 14, 0);

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
}
