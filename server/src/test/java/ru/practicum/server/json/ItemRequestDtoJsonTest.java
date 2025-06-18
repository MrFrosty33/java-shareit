package ru.practicum.server.json;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.models.request.ItemRequestAnswer;
import ru.practicum.models.request.ItemRequestDto;

import java.time.LocalDateTime;
import java.util.Set;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@JsonTest
public class ItemRequestDtoJsonTest {
    @Autowired
    private JacksonTester<ItemRequestDto> json;

    @Test
    void shouldSerializeAllFields() throws Exception {
        LocalDateTime time = LocalDateTime.of(2025, 6, 18, 14, 14, 0);
        Set<ItemRequestAnswer> items = Set.of(
                ItemRequestAnswer.builder()
                        .id(1L)
                        .name("item.1")
                        .ownerId(13L)
                        .available(true)
                        .build(),
                ItemRequestAnswer.builder()
                        .id(2L)
                        .name("item.2")
                        .ownerId(13L)
                        .available(false)
                        .build()
        );

        ItemRequestDto dto = ItemRequestDto.builder()
                .id(1L)
                .description("test")
                .requesterId(3L)
                .created(time)
                .items(items)
                .build();

        JsonContent<ItemRequestDto> result = json.write(dto);

        Assertions.assertThat(result)
                .hasJsonPathNumberValue("$.id")
                .hasJsonPathNumberValue("$.requesterId")
                .hasJsonPathStringValue("$.description")
                .hasJsonPathArrayValue("$.items")
                .hasJsonPathStringValue("$.created");

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathNumberValue("$.requesterId").isEqualTo(3);
        assertThat(result).extractingJsonPathStringValue("$.description").isEqualTo("test");
        assertThat(result).extractingJsonPathArrayValue("$.items").hasSize(2);
        assertThat(result).extractingJsonPathStringValue("$.created")
                .isEqualTo("2025-06-18T14:14:00");
    }

    @Test
    void shouldDeserializeFromJson() throws Exception {
        String content = """
                {
                    "id": 1,
                    "description": "test",
                    "requesterId": 3,
                    "created": "2025-06-18T14:14:00",
                    "items": [
                        {
                            "id": 1,
                            "name": "item.1",
                            "ownerId": 13,
                            "available": true
                        },
                        {
                            "id": 2,
                            "name": "item.2",
                            "ownerId": 13,
                            "available": false
                        }
                    ]
                }
                """;

        ItemRequestDto parsedDto = json.parseObject(content);

        assertThat(parsedDto.getId()).isEqualTo(1L);
        assertThat(parsedDto.getDescription()).isEqualTo("test");
        assertThat(parsedDto.getRequesterId()).isEqualTo(3L);
        assertThat(parsedDto.getCreated()).
                isEqualTo(LocalDateTime.of(2025, 6, 18, 14, 14, 0));
        assertThat(parsedDto.getItems()).hasSize(2);
    }
}
