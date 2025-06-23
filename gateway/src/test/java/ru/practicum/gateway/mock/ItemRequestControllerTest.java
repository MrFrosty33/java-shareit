package ru.practicum.gateway.mock;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
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
import ru.practicum.gateway.controller.ItemRequestController;
import ru.practicum.gateway.utils.ShareItHeadersBuilder;
import ru.practicum.models.request.CreateItemRequestDto;
import ru.practicum.models.request.ItemRequestAnswer;
import ru.practicum.models.request.ItemRequestDto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.practicum.gateway.util.JsonStringMapper.asJsonString;

@WebMvcTest(ItemRequestController.class)
@SuppressWarnings("checkstyle:RegexpSinglelineJava")
@AutoConfigureMockMvc
public class ItemRequestControllerTest {
    @Autowired
    private MockMvc mvc;

    @Value("${shareit.server-url}")
    private String url;

    @MockBean
    private RestTemplate restTemplate;

    @MockBean
    private ShareItHeadersBuilder headersBuilder;

    LocalDateTime now = LocalDateTime.of(2025, 6, 18, 23, 7, 15);

    @Test
    void testGetByNegativeId_shouldReturnBadRequest() throws Exception {
        mvc.perform(get("/requests/-1")
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isBadRequest());

        // валидация не проходит, вызовов быть не должно
        verify(restTemplate, never()).exchange(
                anyString(),
                any(HttpMethod.class),
                any(),
                any(Class.class));
    }

    @Test
    void testGetById_negativeHeader_shouldReturnBadRequest() throws Exception {
        mvc.perform(get("/requests/1")
                        .header("X-Sharer-User-Id", -1L))
                .andExpect(status().isBadRequest());

        verify(restTemplate, never()).exchange(
                anyString(),
                any(HttpMethod.class),
                any(),
                any(Class.class));
    }

    @Test
    void testGetByRequestId_shouldReturnOk() throws Exception {
        ItemRequestAnswer i1 = ItemRequestAnswer.builder()
                .id(1L)
                .name("item.answer.1")
                .ownerId(13L)
                .available(true)
                .build();
        ItemRequestAnswer i2 = ItemRequestAnswer.builder()
                .id(2L)
                .name("item.answer.2")
                .ownerId(13L)
                .available(false)
                .build();

        ItemRequestDto r = ItemRequestDto.builder()
                .id(1L)
                .items(Set.of(i1, i2))
                .requesterId(3L)
                .description("test")
                .created(now)
                .build();

        Mockito.when(restTemplate.exchange(
                        eq(url + "/requests/1"),
                        eq(HttpMethod.GET),
                        ArgumentMatchers.any(HttpEntity.class),
                        eq(ItemRequestDto.class)))
                .thenReturn(new ResponseEntity<>(r, HttpStatus.OK));

        mvc.perform(get("/requests/1")
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.description").value("test"))
                .andExpect(jsonPath("$.requesterId").value(3))
                .andExpect(jsonPath("$.created").value(now.toString()))
                .andExpect(jsonPath("$.items.length()").value(2))
                .andExpect(jsonPath("$.items[?(@.id == 1)].name").value("item.answer.1"))
                .andExpect(jsonPath("$.items[?(@.id == 2)].available").value(false));

        verify(restTemplate, times(1))
                .exchange(
                        eq(url + "/requests/1"),
                        eq(HttpMethod.GET),
                        ArgumentMatchers.any(HttpEntity.class),
                        eq(ItemRequestDto.class)
                );
    }

    @Test
    void testGetOthers_negativeHeader_shouldReturnBadRequest() throws Exception {
        mvc.perform(get("/requests/all")
                        .header("X-Sharer-User-Id", -1L))
                .andExpect(status().isBadRequest());

        verify(restTemplate, never()).exchange(
                anyString(),
                any(HttpMethod.class),
                any(),
                any(Class.class));
    }

    @Test
    void testGetOthersHeaderNull_shouldReturnBadRequest() throws Exception {
        mvc.perform(get("/requests/all")
                        .header("X-Sharer-User-Id", "null"))
                .andExpect(status().isBadRequest());

        verify(restTemplate, never()).exchange(
                anyString(),
                any(HttpMethod.class),
                any(),
                any(Class.class));
    }

    @Test
    void testGetOthersRequests_shouldReturnOk() throws Exception {
        ItemRequestAnswer i1 = ItemRequestAnswer.builder()
                .id(1L)
                .name("item.1")
                .ownerId(2L)
                .available(true)
                .build();

        ItemRequestDto r1 = ItemRequestDto.builder()
                .id(1L)
                .description("test")
                .requesterId(3L)
                .created(now)
                .items(Set.of(i1))
                .build();

        List<ItemRequestDto> responseList = List.of(r1);

        Mockito.when(restTemplate.exchange(
                        eq(url + "/requests/all"),
                        eq(HttpMethod.GET),
                        ArgumentMatchers.<HttpEntity<Void>>any(),
                        ArgumentMatchers.<ParameterizedTypeReference<List<ItemRequestDto>>>any()))
                .thenReturn(new ResponseEntity<>(responseList, HttpStatus.OK));

        mvc.perform(get("/requests/all")
                        .header("X-Sharer-User-Id", 13L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].description").value("test"))
                .andExpect(jsonPath("$[0].requesterId").value(3))
                .andExpect(jsonPath("$[0].created").value(now.toString()))
                .andExpect(jsonPath("$[0].items[0].id").value(1))
                .andExpect(jsonPath("$[0].items[0].name").value("item.1"))
                .andExpect(jsonPath("$[0].items[0].ownerId").value(2))
                .andExpect(jsonPath("$[0].items[0].available").value(true));

        verify(restTemplate, times(1)).exchange(
                eq(url + "/requests/all"),
                eq(HttpMethod.GET),
                ArgumentMatchers.<HttpEntity<Void>>any(),
                ArgumentMatchers.<ParameterizedTypeReference<List<ItemRequestDto>>>any());
    }

    @Test
    void testSaveNegativeHeader_shouldReturnBadRequest() throws Exception {
        String content = """
                {
                    "description": "item.request",
                    "requesterId": 3,
                    "created": "2025-06-18T23:07:15"
                }
                """;

        mvc.perform(post("/requests")
                        .header("X-Sharer-User-Id", -1L)
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
    void testSaveHeaderNull_shouldReturnBadRequest() throws Exception {
        String content = """
                {
                    "description": "item.request",
                    "requesterId": 3,
                    "created": "2025-06-18T23:07:15"
                }
                """;

        mvc.perform(post("/requests")
                        .header("X-Sharer-User-Id", "null")
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
    void testSaveInvalidDescription_shouldReturnBadRequest() throws Exception {
        String content = """
                {
                    "description": "",
                    "requesterId": 3,
                    "created": "2025-06-18T23:07:15"
                }
                """;

        mvc.perform(post("/requests")
                        .header("X-Sharer-User-Id", 3L)
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
    void testSaveNegativeRequesterId_shouldReturnBadRequest() throws Exception {
        String content = """
                {
                    "description": "item.request",
                    "requesterId": -3,
                    "created": "2025-06-18T23:07:15"
                }
                """;

        mvc.perform(post("/requests")
                        .header("X-Sharer-User-Id", -1L)
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
        CreateItemRequestDto r = CreateItemRequestDto.builder()
                .id(1L)
                .description("ох уж эти тесты")
                .requesterId(3L)
                .created(now)
                .build();

        Mockito.when(restTemplate.exchange(
                        eq(url + "/requests"),
                        eq(HttpMethod.POST),
                        any(HttpEntity.class),
                        eq(CreateItemRequestDto.class)))
                .thenReturn(new ResponseEntity<>(r, HttpStatus.OK));

        mvc.perform(post("/requests")
                        .content(asJsonString(r))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.requesterId").value(3))
                .andExpect(jsonPath("$.description").value("ох уж эти тесты"))
                .andExpect(jsonPath("$.created").value(now.toString()));

        verify(restTemplate, times(1))
                .exchange(eq(url + "/requests"),
                        eq(HttpMethod.POST),
                        any(HttpEntity.class),
                        eq(CreateItemRequestDto.class));
    }


}
