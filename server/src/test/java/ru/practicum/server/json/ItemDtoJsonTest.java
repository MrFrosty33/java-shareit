package ru.practicum.server.json;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.models.item.CommentDto;
import ru.practicum.models.item.ItemDto;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@SuppressWarnings("checkstyle:RegexpSinglelineJava")
@JsonTest
public class ItemDtoJsonTest {
    @Autowired
    private JacksonTester<ItemDto> json;

    @Test
    void testSerialize() throws Exception {
        LocalDateTime time = LocalDateTime.of(2025, 6, 18, 14, 14, 0);
        List<CommentDto> comments = List.of(
                CommentDto.builder()
                        .id(1L)
                        .text("comment.1")
                        .authorName("user.1")
                        .created(time)
                        .build(),
                CommentDto.builder()
                        .id(2L)
                        .text("comment.2")
                        .authorName("user.1")
                        .created(time)
                        .build()
        );

        ItemDto dto = ItemDto.builder()
                .id(1L)
                .name("item")
                .description("test")
                .ownerId(13L)
                .requestId(3L)
                .lastBooking(6L)
                .nextBooking(7L)
                .available(true)
                .comments(comments)
                .build();

        JsonContent<ItemDto> result = json.write(dto);

        assertThat(result)
                .hasJsonPathNumberValue("$.id")
                .hasJsonPathStringValue("$.name")
                .hasJsonPathStringValue("$.description")
                .hasJsonPathNumberValue("$.ownerId")
                .hasJsonPathNumberValue("$.requestId")
                .hasJsonPathNumberValue("$.lastBooking")
                .hasJsonPathNumberValue("$.nextBooking")
                .hasJsonPathBooleanValue("$.available")
                .hasJsonPathArrayValue("$.comments");

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.name").isEqualTo("item");
        assertThat(result).extractingJsonPathStringValue("$.description").isEqualTo("test");
        assertThat(result).extractingJsonPathNumberValue("$.ownerId").isEqualTo(13);
        assertThat(result).extractingJsonPathNumberValue("$.requestId").isEqualTo(3);
        assertThat(result).extractingJsonPathNumberValue("$.lastBooking").isEqualTo(6);
        assertThat(result).extractingJsonPathNumberValue("$.nextBooking").isEqualTo(7);
        assertThat(result).extractingJsonPathBooleanValue("$.available").isEqualTo(true);
        assertThat(result).extractingJsonPathArrayValue("$.comments").hasSize(2);
    }

    @Test
    void testDeserialize() throws Exception {
        String content = """
                {
                    "id": 1,
                    "name": "item",
                    "description": "test",
                    "ownerId": 13,
                    "requestId": 4,
                    "lastBooking": 6,
                    "nextBooking": 7,
                    "available": true,
                    "comments": [
                        {
                            "id": 1,
                            "text": "comment.1",
                            "authorName": "user.1",
                            "created": "2025-06-18T14:14:00"
                        },
                        {
                            "id": 2,
                            "text": "comment.2",
                            "authorName": "user.1",
                            "created": "2025-06-18T14:14:00"
                        }
                    ]
                }
                """;

        ItemDto parsedDto = json.parseObject(content);

        assertThat(parsedDto.getId()).isEqualTo(1L);
        assertThat(parsedDto.getName()).isEqualTo("item");
        assertThat(parsedDto.getDescription()).isEqualTo("test");
        assertThat(parsedDto.getOwnerId()).isEqualTo(13L);
        assertThat(parsedDto.getRequestId()).isEqualTo(4L);
        assertThat(parsedDto.getLastBooking()).isEqualTo(6L);
        assertThat(parsedDto.getNextBooking()).isEqualTo(7L);
        assertThat(parsedDto.getAvailable()).isTrue();
        assertThat(parsedDto.getComments()).hasSize(2);
        assertThat(parsedDto.getComments().get(0).getText()).isEqualTo("comment.1");
        assertThat(parsedDto.getComments().get(1).getText()).isEqualTo("comment.2");
    }


}
