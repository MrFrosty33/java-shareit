package ru.practicum.server.json;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.models.item.CommentDto;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class CommentDtoJsonTest {
    @Autowired
    private JacksonTester<CommentDto> json;

    @Test
    void testSerialize() throws Exception {
        LocalDateTime time = LocalDateTime.of(2025, 6, 18, 14, 14, 0);
        CommentDto dto = CommentDto.builder()
                .id(1L)
                .text("comment.1")
                .authorName("user.1")
                .created(time)
                .build();

        JsonContent<CommentDto> result = json.write(dto);

        assertThat(result)
                .hasJsonPathNumberValue("$.id")
                .hasJsonPathStringValue("$.text")
                .hasJsonPathStringValue("$.authorName")
                .hasJsonPathStringValue("$.created");

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.text").isEqualTo("comment.1");
        assertThat(result).extractingJsonPathStringValue("$.authorName").isEqualTo("user.1");
        assertThat(result).extractingJsonPathStringValue("$.created")
                .isEqualTo("2025-06-18T14:14:00");
    }

    @Test
    void testDeserialize() throws Exception {
        String content = """
                {
                    "id": 1,
                    "text": "comment.1",
                    "authorName": "user.1",
                    "created": "2025-06-18T14:14:00"
                }
                """;

        CommentDto parsedDto = json.parseObject(content);

        assertThat(parsedDto.getId()).isEqualTo(1L);
        assertThat(parsedDto.getText()).isEqualTo("comment.1");
        assertThat(parsedDto.getAuthorName()).isEqualTo("user.1");
        assertThat(parsedDto.getCreated())
                .isEqualTo(LocalDateTime.of(2025, 6, 18, 14, 14, 0));
    }
}