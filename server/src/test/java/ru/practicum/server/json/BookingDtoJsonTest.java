package ru.practicum.server.json;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.models.booking.BookingDto;
import ru.practicum.models.booking.Status;
import ru.practicum.models.item.ItemDto;
import ru.practicum.models.user.UserDto;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class BookingDtoJsonTest {
    @Autowired
    private JacksonTester<BookingDto> json;

    @Test
    void testSerialize() throws Exception {
        LocalDateTime start = LocalDateTime.of(2025, 6, 18, 14, 14, 0);
        LocalDateTime end = LocalDateTime.of(2025, 6, 25, 14, 14, 0);

        ItemDto item = ItemDto.builder()
                .id(1L)
                .name("item.1")
                .description("test")
                .build();

        UserDto booker = UserDto.builder()
                .id(1L)
                .name("user.1")
                .email("t.1@example.com")
                .build();

        BookingDto dto = BookingDto.builder()
                .id(1L)
                .item(item)
                .start(start)
                .end(end)
                .booker(booker)
                .status(Status.APPROVED)
                .build();

        JsonContent<BookingDto> result = json.write(dto);

        assertThat(result)
                .hasJsonPathNumberValue("$.id")
                .hasJsonPathMapValue("$.item")
                .hasJsonPathStringValue("$.start")
                .hasJsonPathStringValue("$.end")
                .hasJsonPathMapValue("$.booker")
                .hasJsonPathStringValue("$.status");

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.start").isEqualTo("2025-06-18T14:14:00");
        assertThat(result).extractingJsonPathStringValue("$.end").isEqualTo("2025-06-25T14:14:00");
        assertThat(result).extractingJsonPathStringValue("$.status").isEqualTo("APPROVED");
        assertThat(result).extractingJsonPathNumberValue("$.item.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.item.name").isEqualTo("item.1");
        assertThat(result).extractingJsonPathStringValue("$.item.description").isEqualTo("test");
        assertThat(result).extractingJsonPathNumberValue("$.booker.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.booker.name").isEqualTo("user.1");
        assertThat(result).extractingJsonPathStringValue("$.booker.email").isEqualTo("t.1@example.com");
    }

    @Test
    void testDeserialize() throws Exception {
        String content = """
                {
                    "id": 1,
                    "item": {
                        "id": 1,
                        "name": "item.1",
                        "description": "test"
                    },
                    "start": "2025-06-18T14:14:00",
                    "end": "2025-06-25T14:14:00",
                    "booker": {
                        "id": 1,
                        "name": "user.1",
                        "email": "t.1@example.com"
                    },
                    "status": "APPROVED"
                }
                """;

        BookingDto parsedDto = json.parseObject(content);

        assertThat(parsedDto.getId()).isEqualTo(1L);
        assertThat(parsedDto.getStart()).isEqualTo(LocalDateTime.of(2025, 6, 18, 14, 14, 0));
        assertThat(parsedDto.getEnd()).isEqualTo(LocalDateTime.of(2025, 6, 25, 14, 14, 0));
        assertThat(parsedDto.getStatus()).isEqualTo(Status.APPROVED);
        assertThat(parsedDto.getItem().getId()).isEqualTo(1L);
        assertThat(parsedDto.getItem().getName()).isEqualTo("item.1");
        assertThat(parsedDto.getItem().getDescription()).isEqualTo("test");
        assertThat(parsedDto.getBooker().getId()).isEqualTo(1L);
        assertThat(parsedDto.getBooker().getName()).isEqualTo("user.1");
        assertThat(parsedDto.getBooker().getEmail()).isEqualTo("t.1@example.com");
    }
}