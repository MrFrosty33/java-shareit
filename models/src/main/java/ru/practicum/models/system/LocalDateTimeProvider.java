package ru.practicum.models.system;

import java.time.LocalDateTime;
import java.time.ZoneId;

public class LocalDateTimeProvider {

    public static LocalDateTime getLocalDateTime() {
        // этот момент мне не нравится.
        // Вероятно, возникнет проблема, если запускать приложение в другой временной зоне :\
        return LocalDateTime.now(ZoneId.of("Europe/Vienna"));
    }
}
