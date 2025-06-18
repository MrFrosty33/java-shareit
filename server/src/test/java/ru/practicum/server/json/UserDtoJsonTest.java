package ru.practicum.server.json;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.models.user.UserDto;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class UserDtoJsonTest {

    @Autowired
    private JacksonTester<UserDto> json;

    @Test
    void testSerialize() throws Exception {
        UserDto dto = UserDto.builder()
                .id(1L)
                .name("U")
                .email("t.1@example.com")
                .build();

        JsonContent<UserDto> result = json.write(dto);

        assertThat(result)
                .hasJsonPathNumberValue("$.id")
                .hasJsonPathStringValue("$.name")
                .hasJsonPathStringValue("$.email");

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.name").isEqualTo("U");
        assertThat(result).extractingJsonPathStringValue("$.email").isEqualTo("t.1@example.com");
    }

    @Test
    void testDeserialize() throws Exception {
        String content = """
                {
                  "id": 1,
                  "name": "U",
                  "email": "t.1@example.com"
                }
                """;

        UserDto dto = json.parseObject(content);

        assertThat(dto.getId()).isEqualTo(1L);
        assertThat(dto.getName()).isEqualTo("U");
        assertThat(dto.getEmail()).isEqualTo("t.1@example.com");
    }
}
