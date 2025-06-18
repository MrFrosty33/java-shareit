package ru.practicum.server.json;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.models.booking.BookingCreate;
import ru.practicum.models.booking.Status;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class BookingCreateJsonTest {
    @Autowired
    private JacksonTester<BookingCreate> json;

    @Test
    void testSerialize() throws Exception {
        LocalDateTime start = LocalDateTime.of(2025, 6, 18, 14, 14, 0);
        LocalDateTime end = LocalDateTime.of(2025, 6, 25, 14, 14, 0);

        BookingCreate dto = BookingCreate.builder()
                .id(1L)
                .itemId(13L)
                .start(start)
                .end(end)
                .bookerId(3L)
                .status(Status.WAITING)
                .build();

        JsonContent<BookingCreate> result = json.write(dto);

        assertThat(result)
                .hasJsonPathNumberValue("$.id")
                .hasJsonPathNumberValue("$.itemId")
                .hasJsonPathStringValue("$.start")
                .hasJsonPathStringValue("$.end")
                .hasJsonPathNumberValue("$.bookerId")
                .hasJsonPathStringValue("$.status");

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathNumberValue("$.itemId").isEqualTo(13);
        assertThat(result).extractingJsonPathStringValue("$.start").isEqualTo("2025-06-18T14:14:00");
        assertThat(result).extractingJsonPathStringValue("$.end").isEqualTo("2025-06-25T14:14:00");
        assertThat(result).extractingJsonPathNumberValue("$.bookerId").isEqualTo(3);
        assertThat(result).extractingJsonPathStringValue("$.status").isEqualTo("WAITING");
    }

    @Test
    void testDeserialize() throws Exception {
        String content = """
                {
                    "id": 1,
                    "itemId": 13,
                    "start": "2025-06-18T14:14:00",
                    "end": "2025-06-25T14:14:00",
                    "bookerId": 3,
                    "status": "WAITING"
                }
                """;

        BookingCreate parsedDto = json.parseObject(content);

        assertThat(parsedDto.getId()).isEqualTo(1L);
        assertThat(parsedDto.getItemId()).isEqualTo(13L);
        assertThat(parsedDto.getStart()).isEqualTo(LocalDateTime.of(2025, 6, 18, 14, 14, 0));
        assertThat(parsedDto.getEnd()).isEqualTo(LocalDateTime.of(2025, 6, 25, 14, 14, 0));
        assertThat(parsedDto.getBookerId()).isEqualTo(3L);
        assertThat(parsedDto.getStatus()).isEqualTo(Status.WAITING);
    }

    @Test
    void testSerializeWithoutFewFields() throws Exception {
        String content = """
                {
                    "id": 1,
                    "itemId": 13,
                    "start": "2025-06-18T14:14:00",
                    "end": "2025-06-25T14:14:00"
                }
                """;

        BookingCreate parsedDto = json.parseObject(content);

        assertThat(parsedDto.getId()).isEqualTo(1L);
        assertThat(parsedDto.getItemId()).isEqualTo(13L);
        assertThat(parsedDto.getStart()).isEqualTo(LocalDateTime.of(2025, 6, 18, 14, 14, 0));
        assertThat(parsedDto.getEnd()).isEqualTo(LocalDateTime.of(2025, 6, 25, 14, 14, 0));
        assertThat(parsedDto.getBookerId()).isNull();
        assertThat(parsedDto.getStatus()).isNull();
    }
}