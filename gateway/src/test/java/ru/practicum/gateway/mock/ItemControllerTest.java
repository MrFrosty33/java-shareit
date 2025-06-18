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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.client.RestTemplate;
import ru.practicum.gateway.controller.ItemController;
import ru.practicum.gateway.utils.ShareItHeadersBuilder;
import ru.practicum.models.item.CommentDto;
import ru.practicum.models.item.ItemDto;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.practicum.gateway.util.JsonStringMapper.asJsonString;

@WebMvcTest(ItemController.class)
@AutoConfigureMockMvc
class ItemControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private RestTemplate restTemplate;

    @MockBean
    ShareItHeadersBuilder headersBuilder;

    @Value("${shareit.server-url}")
    private String url;

    private LocalDateTime now = LocalDateTime.of(2025, 6, 19, 0, 46, 16);


    @Test
    void testGetNegativeId_shouldReturnBadRequest() throws Exception {
        mvc.perform(get("/items/-1")
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
    void testGetNegativeHeader_shouldReturnBadRequest() throws Exception {
        mvc.perform(get("/items/1")
                        .header("X-Sharer-User-Id", -1L))
                .andExpect(status().isBadRequest());

        verify(restTemplate, never()).exchange(
                anyString(),
                any(HttpMethod.class),
                any(),
                any(Class.class));
    }

    @Test
    void testGet_shouldReturnOk() throws Exception {
        ItemDto i = ItemDto.builder()
                .id(1L)
                .name("item.1")
                .description("test.1")
                .ownerId(13L)
                .build();

        Mockito.when(restTemplate.exchange(
                        eq(url + "/items/1"),
                        eq(HttpMethod.GET),
                        ArgumentMatchers.any(HttpEntity.class),
                        eq(ItemDto.class)))
                .thenReturn(new ResponseEntity<>(i, HttpStatus.OK));

        mvc.perform(get("/items/1")
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("item.1"))
                .andExpect(jsonPath("$.description").value("test.1"))
                .andExpect(jsonPath("$.ownerId").value(13L));

        verify(restTemplate, times(1))
                .exchange(
                        eq(url + "/items/1"),
                        eq(HttpMethod.GET),
                        ArgumentMatchers.any(HttpEntity.class),
                        eq(ItemDto.class)
                );
    }

    @Test
    void testGeAllNegativeHeader_shouldReturnBadRequest() throws Exception {
        mvc.perform(get("/items")
                        .header("X-Sharer-User-Id", -1L))
                .andExpect(status().isBadRequest());

        verify(restTemplate, never()).exchange(
                anyString(),
                any(HttpMethod.class),
                any(),
                any(Class.class));
    }

    @Test
    void testGetAllItemsByUserId_shouldReturnOk() throws Exception {
        List<ItemDto> expectedItems = List.of(
                ItemDto.builder()
                        .id(1L)
                        .name("item.1")
                        .description("test.1")
                        .ownerId(13L)
                        .build(),
                ItemDto.builder()
                        .id(2L)
                        .name("item.2")
                        .description("test.2")
                        .ownerId(13L)
                        .build()
        );

        Mockito.when(restTemplate.exchange(
                        eq(url + "/items"),
                        eq(HttpMethod.GET),
                        ArgumentMatchers.any(HttpEntity.class),
                        ArgumentMatchers.<ParameterizedTypeReference<List<ItemDto>>>any()))
                .thenReturn(new ResponseEntity<>(expectedItems, HttpStatus.OK));

        mvc.perform(get("/items")
                        .header("X-Sharer-User-Id", 13L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].name").value("item.1"))
                .andExpect(jsonPath("$[0].description").value("test.1"))
                .andExpect(jsonPath("$[0].ownerId").value(13L))
                .andExpect(jsonPath("$[1].id").value(2L))
                .andExpect(jsonPath("$[1].name").value("item.2"))
                .andExpect(jsonPath("$[1].description").value("test.2"))
                .andExpect(jsonPath("$[1].ownerId").value(13L));

        verify(restTemplate, times(1))
                .exchange(
                        eq(url + "/items"),
                        eq(HttpMethod.GET),
                        ArgumentMatchers.any(HttpEntity.class),
                        ArgumentMatchers.<ParameterizedTypeReference<List<ItemDto>>>any()
                );
    }

    @Test
    void testSearch_shouldReturnOk() throws Exception {
        String text = "search";
        List<ItemDto> expectedItems = List.of(
                ItemDto.builder()
                        .id(1L)
                        .name("item.1")
                        .description("test.1.search")
                        .available(true)
                        .ownerId(3L)
                        .build(),
                ItemDto.builder()
                        .id(2L)
                        .name("item.2")
                        .description("test.2")
                        .available(true)
                        .ownerId(3L)
                        .build()
        );

        Mockito.when(restTemplate.exchange(
                        eq(url + "/items/search?text=" + text),
                        eq(HttpMethod.GET),
                        ArgumentMatchers.any(HttpEntity.class),
                        ArgumentMatchers.<ParameterizedTypeReference<List<ItemDto>>>any()))
                .thenReturn(new ResponseEntity<>(expectedItems, HttpStatus.OK));

        mvc.perform(get("/items/search")
                        .param("text", text)
                        .header("X-Sharer-User-Id", 3L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("item.1"))
                .andExpect(jsonPath("$[0].description").value("test.1.search"))
                .andExpect(jsonPath("$[0].available").value(true))
                .andExpect(jsonPath("$[0].ownerId").value(3L));

        verify(restTemplate, times(1))
                .exchange(
                        eq(url + "/items/search?text=" + text),
                        eq(HttpMethod.GET),
                        ArgumentMatchers.any(HttpEntity.class),
                        ArgumentMatchers.<ParameterizedTypeReference<List<ItemDto>>>any()
                );
    }

    @Test
    void testSaveInvalidName_shouldReturnBadRequest() throws Exception {
        String content = """
                {
                   "name": "",
                   "description": "test.1",
                   "available": true,
                   "ownerId": 3
                }
                """;

        mvc.perform(post("/items")
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
    void testSaveInvalidDescription_shouldReturnBadRequest() throws Exception {
        String content = """
                {
                   "name": "item.1",
                   "description": "",
                   "available": true,
                   "ownerId": 3
                }
                """;

        mvc.perform(post("/items")
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
    void testSaveNegativeOwnerId_shouldReturnBadRequest() throws Exception {
        String content = """
                {
                   "name": "item.1",
                   "description": "test.1",
                   "available": true,
                   "ownerId": -3
                }
                """;

        mvc.perform(post("/items")
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
    void testSave_shouldReturnOk() throws Exception {
        ItemDto i = ItemDto.builder()
                .id(1L)
                .name("item.1")
                .description("test.1")
                .ownerId(13L)
                .available(true)
                .build();

        Mockito.when(restTemplate.exchange(
                        eq(url + "/items"),
                        eq(HttpMethod.POST),
                        any(HttpEntity.class),
                        eq(ItemDto.class)
                ))
                .thenReturn(new ResponseEntity<>(i, HttpStatus.OK));

        mvc.perform(post("/items")
                        .content(asJsonString(i))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 13L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("item.1"))
                .andExpect(jsonPath("$.description").value("test.1"))
                .andExpect(jsonPath("$.available").value(true))
                .andExpect(jsonPath("$.ownerId").value(13L));

        verify(restTemplate, times(1))
                .exchange(
                        eq(url + "/items"),
                        eq(HttpMethod.POST),
                        any(HttpEntity.class),
                        eq(ItemDto.class)
                );
    }

    @Test
    void testAddComment_shouldReturnOk() throws Exception {
        CommentDto c = CommentDto.builder()
                .id(1L)
                .text("comment.1")
                .created(now)
                .build();

        Mockito.when(restTemplate.exchange(
                        eq(url + "/items/1/comment"),
                        eq(HttpMethod.POST),
                        any(HttpEntity.class),
                        eq(CommentDto.class)
                ))
                .thenReturn(new ResponseEntity<>(c, HttpStatus.OK));

        mvc.perform(post("/items/1/comment")
                        .content(asJsonString(c))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 13L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.text").value("comment.1"))
                .andExpect(jsonPath("$.created").value(now.toString()));


        verify(restTemplate, times(1))
                .exchange(
                        eq(url + "/items/1/comment"),
                        eq(HttpMethod.POST),
                        any(HttpEntity.class),
                        eq(CommentDto.class)
                );
    }

    @Test
    void testUpdate_shouldReturnOk() throws Exception {
        ItemDto i = ItemDto.builder()
                .id(1L)
                .name("item.1")
                .description("test.1")
                .ownerId(13L)
                .available(true)
                .build();

        Mockito.when(restTemplate.exchange(
                        eq(url + "/items/1"),
                        eq(HttpMethod.PATCH),
                        eq(new HttpEntity<>(i)),
                        eq(ItemDto.class)))
                .thenReturn(new ResponseEntity<>(i, HttpStatus.OK));

        mvc.perform(MockMvcRequestBuilders.patch("/items/1")
                        .content(asJsonString(i))
                        .header("X-Sharer-User-Id", 13L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("item.1"))
                .andExpect(jsonPath("$.description").value("test.1"))
                .andExpect(jsonPath("$.ownerId").value(13L))
                .andExpect(jsonPath("$.available").value(true));

        verify(restTemplate, times(1))
                .exchange(eq(url + "/items/1"),
                        eq(HttpMethod.PATCH),
                        eq(new HttpEntity<>(i)),
                        eq(ItemDto.class));
    }

    @Test
    void testDeleteNegativeId_shouldReturnBadRequest() throws Exception {
        mvc.perform(delete("/items/-1")
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isBadRequest());

        verify(restTemplate, never()).exchange(
                anyString(),
                any(HttpMethod.class),
                any(),
                any(Class.class));
    }

    @Test
    void testDeleteAll_shouldReturnOk() throws Exception {
        Mockito.when(restTemplate.exchange(
                        url + "/items",
                        HttpMethod.DELETE,
                        null,
                        Void.class
                ))
                .thenReturn(new ResponseEntity<>(HttpStatus.OK));

        mvc.perform(delete("/items")
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk());

        verify(restTemplate, times(1))
                .exchange(eq(url + "/items"),
                        eq(HttpMethod.DELETE),
                        eq(null),
                        eq(Void.class));
    }


}