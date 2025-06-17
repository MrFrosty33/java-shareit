package ru.practicum.gateway.utils;

import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

@Component
public class HeadersBuilderImpl implements ShareItHeadersBuilder {
    @Override
    public HttpHeaders getUserIdHeader(Long userId) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Sharer-User-Id", String.valueOf(userId));
        return headers;
    }
}
