package ru.practicum.server.json;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.models.request.CreateItemRequestDto;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class CreateItemRequestJsonTest {
    @Autowired
    private JacksonTester<CreateItemRequestDto> json;

    @Test
    void testSerialize() throws Exception {
        LocalDateTime now = LocalDateTime.of(2025, 6, 18, 14, 14, 0);
        CreateItemRequestDto dto = CreateItemRequestDto.builder()
                .id(1L)
                .requesterId(13L)
                .description("test")
                .created(now)
                .build();

        JsonContent<CreateItemRequestDto> result = json.write(dto);

        assertThat(result)
                .hasJsonPathNumberValue("$.id")
                .hasJsonPathNumberValue("$.requesterId")
                .hasJsonPathStringValue("$.created");

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathNumberValue("$.requesterId").isEqualTo(13);
        assertThat(result).extractingJsonPathStringValue("$.description").isEqualTo("test");

        assertThat(result).extractingJsonPathStringValue("$.created")
                .isEqualTo("2025-06-18T14:14:00");
    }

    @Test
    void testDeserialize() throws Exception {
        String content = """
                {
                  "id": 1,
                  "requesterId": 13,
                  "description": "test",
                  "created": "2025-06-18T14:14:00"
                }
                """;

        CreateItemRequestDto dto = json.parseObject(content);

        assertThat(dto.getId()).isEqualTo(1L);
        assertThat(dto.getRequesterId()).isEqualTo(13L);
        assertThat(dto.getDescription()).isEqualTo("test");
        assertThat(dto.getCreated())
                .isEqualTo(LocalDateTime.of(2025, 6, 18, 14, 14, 0));
    }

}
