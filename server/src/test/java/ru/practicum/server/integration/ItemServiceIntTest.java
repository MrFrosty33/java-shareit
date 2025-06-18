package ru.practicum.server.integration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import ru.practicum.models.booking.Status;
import ru.practicum.models.item.CommentDto;
import ru.practicum.models.item.ItemDto;
import ru.practicum.models.system.LocalDateTimeProvider;
import ru.practicum.server.booking.Booking;
import ru.practicum.server.booking.BookingRepository;
import ru.practicum.server.exception.NotFoundException;
import ru.practicum.server.item.ItemRepository;
import ru.practicum.server.item.ItemService;
import ru.practicum.server.user.User;
import ru.practicum.server.user.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class ItemServiceIntTest {
    @Autowired
    private ItemRepository repository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private ItemService service;

    @BeforeEach
    void saveTestUsers() {
        User u1 = User.builder().name("U1").email("t.1@example.com").build(); // id: 1L
        userRepository.save(u1);
    }

    @Test
    void saveItem_and_checkResult() {
        ItemDto i = ItemDto.builder()
                .name("item")
                .description("test")
                .available(true)
                .build();

        ItemDto result = service.save(i, 1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("item", result.getName());
        assertEquals("test", result.getDescription());
        assertEquals(true, result.getAvailable());
    }

    @Test
    void saveItem_and_getById() {
        ItemDto i = ItemDto.builder()
                .name("item")
                .description("test")
                .available(true)
                .build();

        service.save(i, 1L);
        ItemDto result = service.get(1L, 1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("item", result.getName());
        assertEquals("test", result.getDescription());
        assertEquals(true, result.getAvailable());
    }

    @Test
    void saveItems_and_getAllItemsByUserId() {
        ItemDto i1 = ItemDto.builder()
                .name("item.1")
                .description("test")
                .available(true)
                .build();
        ItemDto i2 = ItemDto.builder()
                .name("item.2")
                .description("test")
                .available(true)
                .build();
        ItemDto i3 = ItemDto.builder()
                .name("item.3")
                .description("test")
                .available(false)
                .build();

        service.save(i1, 1L);
        service.save(i2, 1L);
        service.save(i3, 1L);
        List<ItemDto> result = service.getAllItemsByUserId(1L);

        assertNotNull(result);
        assertEquals(3, result.size());
    }

    @Test
    void saveItem_and_isItemAvailable() {
        ItemDto i = ItemDto.builder()
                .name("item")
                .description("test")
                .available(false)
                .build();

        service.save(i, 1L);
        Boolean result = service.isItemAvailable(1L);

        assertNotNull(result);
        assertFalse(result);
    }

    @Test
    void saveItems_and_search() {
        ItemDto i1 = ItemDto.builder() // будет найден
                .name("item.1")
                .description("test")
                .available(true)
                .build();
        ItemDto i2 = ItemDto.builder() // неподходящее описание
                .name("item.2")
                .description("2025")
                .available(true)
                .build();
        ItemDto i3 = ItemDto.builder() // неподходящий статус доступности
                .name("item.3")
                .description("test")
                .available(false)
                .build();

        service.save(i1, 1L);
        service.save(i2, 1L);
        service.save(i3, 1L);
        List<ItemDto> result = service.search("test", 1L);

        assertNotNull(result);
        assertEquals(1, result.size());

    }

    @Test
    void saveItem_and_addComment() {
        LocalDateTime now = LocalDateTimeProvider.getLocalDateTime();
        CommentDto c = CommentDto.builder()
                .id(1L)
                .text("comment.1")
                .build();

        ItemDto i = ItemDto.builder() // будет найден
                .name("item.1")
                .description("test")
                .available(true)
                .build();
        service.save(i, 1L);

        Booking b = Booking.builder()
                .item(repository.findById(1L).get())
                .booker(userRepository.findById(1L).get())
                .startDate(LocalDateTime.of(2025, 6, 10, 14, 14, 0))
                .endDate(LocalDateTime.of(2025, 6, 17, 14, 14, 0))
                .status(Status.WAITING)
                .build();
        bookingRepository.save(b);

        CommentDto result = service.addComment(c, 1L, 1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("comment.1", result.getText());
        assertEquals("U1", result.getAuthorName());
        assertTrue(result.getCreated().isEqual(now) || result.getCreated().isAfter(now));
    }

    @Test
    void saveItem_and_update() {
        ItemDto i1 = ItemDto.builder()
                .name("item")
                .description("test")
                .available(false)
                .build();
        ItemDto i2 = ItemDto.builder()
                .name("item.updated")
                .description("test.updated")
                .available(false)
                .build();

        service.save(i1, 1L);
        ItemDto result = service.update(i2, 1L, 1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("item.updated", result.getName());
        assertEquals("test.updated", result.getDescription());
        assertFalse(result.getAvailable());
    }

    @Test
    void deleteItem_and_getDeletedItem_shouldThrowException() {
        ItemDto i = ItemDto.builder()
                .name("item")
                .description("test")
                .available(false)
                .build();

        Long id = service.save(i, 1L).getId();
        service.delete(id, 1L);

        assertThrowsExactly(NotFoundException.class, () -> service.get(id, 1L));
    }

    @Test
    void deleteItems_and_getDeletedItemsList_shouldBeEmpty() {
        ItemDto i1 = ItemDto.builder()
                .name("item")
                .description("test")
                .available(false)
                .build();
        ItemDto i2 = ItemDto.builder()
                .name("item.updated")
                .description("test.updated")
                .available(false)
                .build();

        service.save(i1, 1L);
        service.save(i2, 1L);
        service.deleteAll();

        assertTrue(service.getAllItemsByUserId(1L).isEmpty());
    }
}
