package ru.practicum.server.integration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.models.request.CreateItemRequestDto;
import ru.practicum.models.request.ItemRequestDto;
import ru.practicum.models.system.LocalDateTimeProvider;
import ru.practicum.server.item.ItemRepository;
import ru.practicum.server.request.ItemRequestRepository;
import ru.practicum.server.request.ItemRequestService;
import ru.practicum.server.user.User;
import ru.practicum.server.user.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class ItemRequestServiceIntTest {
    @Autowired
    private ItemRequestService service;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    private ItemRequestRepository repository;

    @BeforeEach
    void saveTestUsers() {
        User u1 = User.builder().name("U1").email("t.1@example.com").build(); // id: 1L
        User u2 = User.builder().name("U2").email("t.2@example.com").build(); // id: 2L
        User u3 = User.builder().name("U3").email("t.3@example.com").build(); // id: 3L
        userRepository.saveAll(List.of(u1, u2, u3));
    }

    @Test
    void saveRequest_and_checkResult() {
        CreateItemRequestDto r = CreateItemRequestDto.builder().description("test").build();
        LocalDateTime now = LocalDateTimeProvider.getLocalDateTime();

        CreateItemRequestDto result = service.save(3L, r);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(3L, result.getRequesterId());
        assertEquals("test", result.getDescription());
        assertTrue(result.getCreated().isEqual(now) || result.getCreated().isAfter(now));
    }

    @Test
    void saveRequest_and_getByRequestId() {
        CreateItemRequestDto r = CreateItemRequestDto.builder().description("test").build();
        LocalDateTime now = LocalDateTimeProvider.getLocalDateTime();

        service.save(3L, r);
        ItemRequestDto result = service.getByRequestId(3L, 1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(3L, result.getRequesterId());
        assertEquals("test", result.getDescription());
        assertTrue(result.getCreated().isEqual(now) || result.getCreated().isAfter(now));
    }

    @Test
    void saveRequests_and_getAllByUserId() {
        CreateItemRequestDto r1 = CreateItemRequestDto.builder().description("test.1").build();
        CreateItemRequestDto r2 = CreateItemRequestDto.builder().description("test.2").build();
        CreateItemRequestDto r3 = CreateItemRequestDto.builder().description("test.3").build();

        service.save(3L, r1);
        service.save(3L, r2);
        service.save(1L, r3);

        List<ItemRequestDto> result = service.getAllByUserId(3L);

        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    void saveRequests_and_getOthersRequests() {
        CreateItemRequestDto r1 = CreateItemRequestDto.builder().description("test.1").build();
        CreateItemRequestDto r2 = CreateItemRequestDto.builder().description("test.2").build();
        CreateItemRequestDto r3 = CreateItemRequestDto.builder().description("test.3").build();
        CreateItemRequestDto r4 = CreateItemRequestDto.builder().description("test.3").build();
        CreateItemRequestDto r5 = CreateItemRequestDto.builder().description("test.3").build();

        service.save(3L, r1);
        service.save(1L, r2);
        service.save(1L, r3);
        service.save(2L, r4);
        service.save(1L, r5);

        List<ItemRequestDto> result = service.getOthersRequests(3L);

        assertNotNull(result);
        assertEquals(4, result.size());
    }

}
