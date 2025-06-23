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
import ru.practicum.gateway.controller.BookingController;
import ru.practicum.gateway.utils.ShareItHeadersBuilder;
import ru.practicum.models.booking.BookingCreate;
import ru.practicum.models.booking.BookingDto;
import ru.practicum.models.booking.Status;
import ru.practicum.models.item.ItemDto;
import ru.practicum.models.user.UserDto;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.practicum.gateway.util.JsonStringMapper.asJsonString;

@WebMvcTest(BookingController.class)
@SuppressWarnings("checkstyle:RegexpSinglelineJava")
@AutoConfigureMockMvc
public class BookingControllerTest {
    @Autowired
    private MockMvc mvc;

    @Value("${shareit.server-url}")
    private String url;

    @MockBean
    private RestTemplate restTemplate;

    @MockBean
    private ShareItHeadersBuilder headersBuilder;

    // чтобы в ближайшем будущее тест не слетел, годы указаны в далёком будущем
    // хоть это и странно, что приложение вообще позволяет бронировать вещь через десятки, сотни лет xD
    private LocalDateTime start = LocalDateTime.of(2125, 6, 18, 14, 15, 16);
    private LocalDateTime end = LocalDateTime.of(2125, 6, 25, 14, 15, 16);

    @Test
    void testGetNegativeId_shouldReturnBadRequest() throws Exception {
        mvc.perform(get("/bookings/-1")
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
    void testGetNullId_shouldReturnBadRequest() throws Exception {
        mvc.perform(get("/bookings/null")
                        .header("X-Sharer-User-Id", 1L))
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
        UserDto u = UserDto.builder()
                .id(13L)
                .name("user.1")
                .email("u.1@example.com")
                .build();
        BookingDto b = BookingDto.builder()
                .id(1L)
                .start(start)
                .end(end)
                .item(i)
                .booker(u)
                .build();

        Mockito.when(restTemplate.exchange(
                        eq(url + "/bookings/1"),
                        eq(HttpMethod.GET),
                        ArgumentMatchers.any(HttpEntity.class),
                        eq(BookingDto.class)))
                .thenReturn(new ResponseEntity<>(b, HttpStatus.OK));

        mvc.perform(get("/bookings/1")
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.start").value(start.toString()))
                .andExpect(jsonPath("$.end").value(end.toString()))
                .andExpect(jsonPath("$.item.id").value(1))
                .andExpect(jsonPath("$.item.name").value("item.1"))
                .andExpect(jsonPath("$.item.description").value("test.1"))
                .andExpect(jsonPath("$.item.ownerId").value(13L))
                .andExpect(jsonPath("$.booker.id").value(13L))
                .andExpect(jsonPath("$.booker.name").value("user.1"))
                .andExpect(jsonPath("$.booker.email").value("u.1@example.com"));

        verify(restTemplate, times(1))
                .exchange(
                        eq(url + "/bookings/1"),
                        eq(HttpMethod.GET),
                        ArgumentMatchers.any(HttpEntity.class),
                        eq(BookingDto.class)
                );
    }

    @Test
    void testGetAllByStateAndBookerId_invalidState_shouldReturnBadRequest() throws Exception {
        mvc.perform(get("/bookings?state=INVALID")
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isBadRequest());

        verify(restTemplate, never()).exchange(
                anyString(),
                any(HttpMethod.class),
                any(),
                any(Class.class));
    }

    @Test
    void testGetAllByStateAndBookerId_negativeHeader_shouldReturnBadRequest() throws Exception {
        mvc.perform(get("/bookings?state=ALL")
                        .header("X-Sharer-User-Id", -1L))
                .andExpect(status().isBadRequest());

        verify(restTemplate, never()).exchange(
                anyString(),
                any(HttpMethod.class),
                any(),
                any(Class.class));
    }

    @Test
    void testGetAllByStateAndBookerId_shouldReturnOk() throws Exception {
        ItemDto i1 = ItemDto.builder()
                .id(1L)
                .name("item.1")
                .description("test.1")
                .ownerId(13L)
                .build();
        ItemDto i2 = ItemDto.builder()
                .id(2L)
                .name("item.2")
                .description("test.2")
                .ownerId(14L)
                .build();

        UserDto u1 = UserDto.builder()
                .id(13L)
                .name("user.1")
                .email("u.1@example.com")
                .build();
        UserDto u2 = UserDto.builder()
                .id(14L)
                .name("user.2")
                .email("u.2@example.com")
                .build();

        BookingDto b1 = BookingDto.builder()
                .id(1L)
                .start(start)
                .end(end)
                .item(i1)
                .booker(u1)
                .build();
        BookingDto b2 = BookingDto.builder()
                .id(2L)
                .start(start)
                .end(end)
                .item(i2)
                .booker(u2)
                .build();

        List<BookingDto> bookings = List.of(b1, b2);

        ResponseEntity<List<BookingDto>> responseEntity = new ResponseEntity<>(bookings, HttpStatus.OK);

        Mockito.when(restTemplate.exchange(
                eq(url + "/bookings?state=ALL"),
                eq(HttpMethod.GET),
                any(HttpEntity.class),
                Mockito.<ParameterizedTypeReference<List<BookingDto>>>any()
        )).thenReturn(responseEntity);

        mvc.perform(get("/bookings?state=ALL")
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].item.id").value(1))
                .andExpect(jsonPath("$[0].item.name").value("item.1"))
                .andExpect(jsonPath("$[0].item.description").value("test.1"))
                .andExpect(jsonPath("$[0].item.ownerId").value(13))
                .andExpect(jsonPath("$[0].booker.id").value(13))
                .andExpect(jsonPath("$[0].booker.name").value("user.1"))
                .andExpect(jsonPath("$[0].booker.email").value("u.1@example.com"))

                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].item.id").value(2))
                .andExpect(jsonPath("$[1].item.name").value("item.2"))
                .andExpect(jsonPath("$[1].item.description").value("test.2"))
                .andExpect(jsonPath("$[1].item.ownerId").value(14))
                .andExpect(jsonPath("$[1].booker.id").value(14))
                .andExpect(jsonPath("$[1].booker.name").value("user.2"))
                .andExpect(jsonPath("$[1].booker.email").value("u.2@example.com"));

        verify(restTemplate).exchange(
                eq(url + "/bookings?state=ALL"),
                eq(HttpMethod.GET),
                any(HttpEntity.class),
                Mockito.<ParameterizedTypeReference<List<BookingDto>>>any()
        );
    }

    @Test
    void testGetAllByStateAndOwnerId_invalidState_shouldReturnBadRequest() throws Exception {
        mvc.perform(get("/bookings/owner?state=INVALID")
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isBadRequest());

        verify(restTemplate, never()).exchange(
                anyString(),
                any(HttpMethod.class),
                any(),
                any(Class.class));
    }

    @Test
    void testGetAllByStateAndOwnerId_negativeHeader_shouldReturnBadRequest() throws Exception {
        mvc.perform(get("/bookings/owner?state=ALL")
                        .header("X-Sharer-User-Id", -1L))
                .andExpect(status().isBadRequest());

        verify(restTemplate, never()).exchange(
                anyString(),
                any(HttpMethod.class),
                any(),
                any(Class.class));
    }

    @Test
    void testGetAllByStateAndOwnerId_shouldReturnOk() throws Exception {
        ItemDto i1 = ItemDto.builder()
                .id(1L)
                .name("item.1")
                .description("test.1")
                .ownerId(13L)
                .build();
        ItemDto i2 = ItemDto.builder()
                .id(2L)
                .name("item.2")
                .description("test.2")
                .ownerId(14L)
                .build();

        UserDto u1 = UserDto.builder()
                .id(13L)
                .name("user.1")
                .email("u.1@example.com")
                .build();
        UserDto u2 = UserDto.builder()
                .id(14L)
                .name("user.2")
                .email("u.2@example.com")
                .build();

        BookingDto b1 = BookingDto.builder()
                .id(1L)
                .start(start)
                .end(end)
                .item(i1)
                .booker(u1)
                .build();
        BookingDto b2 = BookingDto.builder()
                .id(2L)
                .start(start)
                .end(end)
                .item(i2)
                .booker(u2)
                .build();

        List<BookingDto> bookings = List.of(b1, b2);

        ResponseEntity<List<BookingDto>> responseEntity = new ResponseEntity<>(bookings, HttpStatus.OK);

        Mockito.when(restTemplate.exchange(
                eq(url + "/bookings/owner?state=ALL"),
                eq(HttpMethod.GET),
                any(HttpEntity.class),
                Mockito.<ParameterizedTypeReference<List<BookingDto>>>any()
        )).thenReturn(responseEntity);

        mvc.perform(get("/bookings/owner?state=ALL")
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].item.id").value(1))
                .andExpect(jsonPath("$[0].item.name").value("item.1"))
                .andExpect(jsonPath("$[0].item.description").value("test.1"))
                .andExpect(jsonPath("$[0].item.ownerId").value(13))
                .andExpect(jsonPath("$[0].booker.id").value(13))
                .andExpect(jsonPath("$[0].booker.name").value("user.1"))
                .andExpect(jsonPath("$[0].booker.email").value("u.1@example.com"))

                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].item.id").value(2))
                .andExpect(jsonPath("$[1].item.name").value("item.2"))
                .andExpect(jsonPath("$[1].item.description").value("test.2"))
                .andExpect(jsonPath("$[1].item.ownerId").value(14))
                .andExpect(jsonPath("$[1].booker.id").value(14))
                .andExpect(jsonPath("$[1].booker.name").value("user.2"))
                .andExpect(jsonPath("$[1].booker.email").value("u.2@example.com"));

        verify(restTemplate).exchange(
                eq(url + "/bookings/owner?state=ALL"),
                eq(HttpMethod.GET),
                any(HttpEntity.class),
                Mockito.<ParameterizedTypeReference<List<BookingDto>>>any()
        );
    }

    @Test
    void testSaveInvalidEnd_shouldReturnBadRequest() throws Exception {
        String content = """
                {
                  "itemId": 1,
                  "start": "2025-06-18T14:15:16",
                  "end": "1999-06-19T14:15:16",
                  "bookerId": 13
                }
                """;

        mvc.perform(post("/bookings")
                        .header("X-Sharer-User-Id", 1L)
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
    void testSaveInvalidStart_shouldReturnBadRequest() throws Exception {
        String content = """
                {
                  "itemId": 1,
                  "start": "1999-06-18T14:15:16",
                  "end": "2025-06-19T14:15:16",
                  "bookerId": 13
                }
                """;

        mvc.perform(post("/bookings")
                        .header("X-Sharer-User-Id", 1L)
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
    void testSaveNegativeHeader_shouldReturnBadRequest() throws Exception {
        // чтобы в ближайшем будущее тест не слетел, годы указаны в далёком будущем
        // хоть это и странно, что приложение вообще позволяет бронировать вещь через десятки, сотни лет xD
        String content = """
                {
                  "itemId": 1,
                  "start": "2125-06-18T14:15:16",
                  "end": "2125-06-19T14:15:16",
                  "bookerId": 13
                }
                """;

        mvc.perform(post("/bookings")
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
    void testSaveBooking_shouldReturnOk() throws Exception {
        BookingCreate b = BookingCreate.builder()
                .id(1L)
                .start(start)
                .end(end)
                .itemId(1L)
                .bookerId(13L)
                .build();

        BookingDto bDto = BookingDto.builder()
                .id(1L)
                .start(start)
                .end(end)
                .item(ItemDto.builder().id(1L).build())
                .booker(UserDto.builder().id(13L).build())
                .build();

        Mockito.when(restTemplate.exchange(
                        eq(url + "/bookings"),
                        eq(HttpMethod.POST),
                        any(HttpEntity.class),
                        eq(BookingDto.class)
                ))
                .thenReturn(new ResponseEntity<>(bDto, HttpStatus.OK));

        mvc.perform(post("/bookings")
                        .content(asJsonString(b))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", "13")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.start").value(start.toString()))
                .andExpect(jsonPath("$.end").value(end.toString()))
                .andExpect(jsonPath("$.item.id").value(1))
                .andExpect(jsonPath("$.booker.id").value(13));

        verify(restTemplate, times(1))
                .exchange(
                        eq(url + "/bookings"),
                        eq(HttpMethod.POST),
                        any(HttpEntity.class),
                        eq(BookingDto.class)
                );
    }

    @Test
    void testApproveBookingApprovedNull_shouldReturnBadRequest() throws Exception {
        Long bookingId = 1L;

        mvc.perform(patch("/bookings/" + bookingId + "?approved=null")
                        .header("X-Sharer-User-Id", 13L)
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
    void testApproveBookingHeaderNegative_shouldReturnBadRequest() throws Exception {
        Long bookingId = 1L;

        mvc.perform(patch("/bookings/" + bookingId + "?approved=true")
                        .header("X-Sharer-User-Id", -1L)
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
    void testApproveBookingHeaderNull_shouldReturnBadRequest() throws Exception {
        Long bookingId = 1L;

        mvc.perform(patch("/bookings/" + bookingId + "?approved=true")
                        .header("X-Sharer-User-Id", "null")
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
    void testApproveBooking_shouldReturnOk() throws Exception {
        Long bookingId = 1L;
        Boolean approved = true;

        BookingDto b = BookingDto.builder()
                .id(bookingId)
                .status(Status.APPROVED)
                .build();

        Mockito.when(restTemplate.exchange(
                eq(url + "/bookings/" + bookingId + "?approved=" + approved),
                eq(HttpMethod.PATCH),
                any(HttpEntity.class),
                eq(BookingDto.class)
        )).thenReturn(new ResponseEntity<>(b, HttpStatus.OK));

        mvc.perform(patch("/bookings/" + bookingId)
                        .header("X-Sharer-User-Id", 13L)
                        .param("approved", approved.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(bookingId))
                .andExpect(jsonPath("$.status").value("APPROVED"));

        verify(restTemplate, times(1))
                .exchange(
                        eq(url + "/bookings/" + bookingId + "?approved=" + approved),
                        eq(HttpMethod.PATCH),
                        any(HttpEntity.class),
                        eq(BookingDto.class)
                );
    }

}
