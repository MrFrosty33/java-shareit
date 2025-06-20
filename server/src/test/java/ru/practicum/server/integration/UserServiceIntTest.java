package ru.practicum.server.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.models.user.UserDto;
import ru.practicum.server.exception.NotFoundException;
import ru.practicum.server.user.User;
import ru.practicum.server.user.UserRepository;
import ru.practicum.server.user.UserServiceImpl;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class UserServiceIntTest {
    @Autowired
    private UserServiceImpl service;
    @Autowired
    private UserRepository repository;

    @Test
    void saveUser_and_getSavedUser() {
        User savedUser = repository.save(
                User.builder().name("U").email("t.1@example.com").build()
        );

        UserDto result = service.get(savedUser.getId());

        assertNotNull(result);
        assertEquals(savedUser.getId(), result.getId());
        assertEquals("U", result.getName());
        assertEquals("t.1@example.com", result.getEmail());
    }

    @Test
    void saveUsers_and_getSavedUsersList() {
        User u1 = User.builder().name("U1").email("t.1@example.com").build();
        User u2 = User.builder().name("U2").email("t.2@example.com").build();
        User u3 = User.builder().name("U3").email("t.3@example.com").build();
        List<User> savedUsers = repository.saveAll(List.of(u1, u2, u3));

        List<UserDto> result = service.getAll();
        assertNotNull(result);
        int i = 1;

        for (UserDto u : result) {
            assertEquals(savedUsers.get(i - 1).getId(), u.getId());
            assertEquals("U" + i, u.getName());
            assertEquals("t." + i + "@example.com", u.getEmail());
            i++;
        }
    }

    @Test
    void saveUser_and_checkResult() {
        UserDto u = UserDto.builder().name("U").email("t.1@example.com").build();
        UserDto result = service.save(u);

        assertNotNull(result);
        assertNotNull(result.getId());
        assertTrue(result.getId() > 0);
        assertEquals(u.getName(), result.getName());
        assertEquals(u.getEmail(), result.getEmail());
    }

    @Test
    void saveUser_and_updateSavedUser() {
        // здесь вылетает
        assertTrue(repository.findAll().isEmpty());
        UserDto u1 = UserDto.builder().name("U1").email("t.1@example.com").build();
        UserDto u2 = UserDto.builder().name("U2").email("t.2@example.com").build();
        Long id = service.save(u1).getId();
        UserDto result = service.update(u2, id);

        assertNotNull(result);
        assertNotNull(result.getId());
        assertTrue(result.getId() > 0);
        assertEquals(u2.getName(), result.getName());
        assertEquals(u2.getEmail(), result.getEmail());
    }

    @Test
    void deleteUser_and_getDeletedUser_shouldThrowException() {
        UserDto u = UserDto.builder().name("U").email("t.1@example.com").build();
        Long id = service.save(u).getId();
        service.delete(id);

        assertThrowsExactly(NotFoundException.class, () -> service.get(id));
    }

    @Test
    void mapEntity_and_checkResult() {
        UserDto u = UserDto.builder().name("U").email("t.1@example.com").build();
        User result = service.getEntity(u);

        assertNotNull(result);
        assertEquals(u.getName(), result.getName());
        assertEquals(u.getEmail(), result.getEmail());
    }

    @Test
    void mapDto_and_checkResult() {
        User u = User.builder().name("U").email("t.1@example.com").build();
        UserDto result = service.getDto(u);

        assertNotNull(result);
        assertEquals(u.getName(), result.getName());
        assertEquals(u.getEmail(), result.getEmail());
    }

}
