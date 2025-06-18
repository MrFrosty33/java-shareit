package ru.practicum.gateway.mock;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestTemplate;
import ru.practicum.gateway.controller.UserController;
import ru.practicum.models.user.UserDto;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.practicum.gateway.util.JsonStringMapper.asJsonString;


@WebMvcTest(UserController.class)
@AutoConfigureMockMvc
public class UserControllerTest {
    @Autowired
    private MockMvc mvc;

    @Value("${shareit.server-url}")
    private String url;

    @MockBean
    private RestTemplate restTemplate;

    @Test
    void testGetNegativeId_shouldReturnBadRequest() throws Exception {
        mvc.perform(get("/users/-1"))
                .andExpect(status().isBadRequest());

        // валидация не проходит, вызовов быть не должно
        verify(restTemplate, never()).exchange(
                anyString(),
                any(HttpMethod.class),
                any(),
                any(Class.class));
    }

    @Test
    void testGetNullId_shouldReturnBadRequest() throws Exception {
        mvc.perform(get("/users/null"))
                .andExpect(status().isBadRequest());

        verify(restTemplate, never()).exchange(
                anyString(),
                any(HttpMethod.class),
                any(),
                any(Class.class));
    }

    @Test
    void testGet_shouldReturnOk() throws Exception {
        UserDto u = UserDto.builder()
                .id(1L)
                .name("test.1")
                .email("t.1@example.com")
                .build();

        Mockito.when(restTemplate.getForEntity(
                        eq(url + "/users/1"), eq(UserDto.class)))
                .thenReturn(new ResponseEntity<>(u, HttpStatus.OK));

        mvc.perform(get("/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("test.1"))
                .andExpect(jsonPath("$.email").value("t.1@example.com"));

        verify(restTemplate, times(1))
                .getForEntity(eq(url + "/users/1"), eq(UserDto.class));
    }

    @Test
    void testGetAll_shouldReturnOk() throws Exception {
        UserDto u1 = UserDto.builder()
                .id(1L)
                .name("test.1")
                .email("t.1@example.com")
                .build();
        UserDto u2 = UserDto.builder()
                .id(2L)
                .name("test.2")
                .email("t.2@example.com")
                .build();

        List<UserDto> u = List.of(u1, u2);
        ParameterizedTypeReference<List<UserDto>> responseType =
                new ParameterizedTypeReference<List<UserDto>>() {
                };


        Mockito.when(restTemplate.exchange(
                        eq(url + "/users"),
                        eq(HttpMethod.GET),
                        eq(null),
                        eq(responseType)))
                .thenReturn(new ResponseEntity<>(u, HttpStatus.OK));

        mvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("test.1"))
                .andExpect(jsonPath("$[0].email").value("t.1@example.com"))

                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].name").value("test.2"))
                .andExpect(jsonPath("$[1].email").value("t.2@example.com"));
    }

    @Test
    void testSaveInvalidEmail_shouldReturnBadRequest() throws Exception {
        String content = """
                {
                  "name": "test.1",
                  "email": "t.1-example.com"
                }
                """;

        mvc.perform(post("/users")
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        verify(restTemplate, never()).exchange(
                anyString(),
                any(HttpMethod.class),
                any(),
                any(Class.class));
    }

    @Test
    void testSaveInvalidName_shouldReturnBadRequest() throws Exception {
        String content = """
                {
                  "name": "",
                  "email": "t.1@example.com"
                }
                """;

        mvc.perform(post("/users")
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        verify(restTemplate, never()).exchange(
                anyString(),
                any(HttpMethod.class),
                any(),
                any(Class.class));
    }

    @Test
    void testSave_shouldReturnOk() throws Exception {
        UserDto u = UserDto.builder()
                .id(1L)
                .name("test.1")
                .email("t.1@example.com")
                .build();

        Mockito.when(restTemplate.postForEntity(
                        eq(url + "/users"),
                        eq(u),
                        eq(UserDto.class)
                ))
                .thenReturn(new ResponseEntity<>(u, HttpStatus.OK));

        mvc.perform(post("/users")
                        .content(asJsonString(u))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("test.1"))
                .andExpect(jsonPath("$.email").value("t.1@example.com"));
    }

    @Test
    void testUpdateInvalidEmail_shouldReturnBadRequest() throws Exception {
        String content = """
                {
                  "email": "t.1-example.com"
                }
                """;

        mvc.perform(patch("/users/1")
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        verify(restTemplate, never()).exchange(
                anyString(),
                any(HttpMethod.class),
                any(),
                any(Class.class));

    }

    @Test
    void testUpdate_shouldReturnOk() throws Exception {
        UserDto u = UserDto.builder()
                .id(1L)
                .name("test.1")
                .email("t.1@example.com")
                .build();

        Mockito.when(restTemplate.exchange(
                        eq(url + "/users/1"),
                        eq(HttpMethod.PATCH),
                        eq(new HttpEntity<>(u)),
                        eq(UserDto.class)))
                .thenReturn(new ResponseEntity<>(u, HttpStatus.OK));

        mvc.perform(patch("/users/1")
                        .content(asJsonString(u))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("test.1"))
                .andExpect(jsonPath("$.email").value("t.1@example.com"));

        verify(restTemplate, times(1))
                .exchange(eq(url + "/users/1"),
                        eq(HttpMethod.PATCH),
                        eq(new HttpEntity<>(u)),
                        eq(UserDto.class));
    }

    @Test
    void testDeleteNullId_shouldReturnBadRequest() throws Exception {
        mvc.perform(delete("/users/null"))
                .andExpect(status().isBadRequest());

        verify(restTemplate, never()).exchange(
                anyString(),
                any(HttpMethod.class),
                any(),
                any(Class.class));
    }

    @Test
    void testDeleteNegativeId_shouldReturnBadRequest() throws Exception {
        mvc.perform(delete("/users/-1"))
                .andExpect(status().isBadRequest());

        verify(restTemplate, never()).exchange(
                anyString(),
                any(HttpMethod.class),
                any(),
                any(Class.class));
    }

    @Test
    void testDelete_shouldReturnOk() throws Exception {
        Mockito.when(restTemplate.exchange(
                        url + "/users/1",
                        HttpMethod.DELETE,
                        null,
                        Void.class
                ))
                .thenReturn(new ResponseEntity<>(HttpStatus.OK));

        mvc.perform(delete("/users/1"))
                .andExpect(status().isOk());

        verify(restTemplate, times(1))
                .exchange(eq(url + "/users/1"),
                        eq(HttpMethod.DELETE),
                        eq(null),
                        eq(Void.class));
    }

    @Test
    void testDeleteAll_shouldReturnOk() throws Exception {
        Mockito.when(restTemplate.exchange(
                        url + "/users",
                        HttpMethod.DELETE,
                        null,
                        Void.class
                ))
                .thenReturn(new ResponseEntity<>(HttpStatus.OK));

        mvc.perform(delete("/users"))
                .andExpect(status().isOk());

        verify(restTemplate, times(1))
                .exchange(eq(url + "/users"),
                        eq(HttpMethod.DELETE),
                        eq(null),
                        eq(Void.class));
    }


}
