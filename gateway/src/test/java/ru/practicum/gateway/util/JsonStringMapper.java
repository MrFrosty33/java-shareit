package ru.practicum.gateway.util;

import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonStringMapper {
    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
