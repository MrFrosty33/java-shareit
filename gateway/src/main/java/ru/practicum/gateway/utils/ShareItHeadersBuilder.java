package ru.practicum.gateway.utils;

import org.springframework.http.HttpHeaders;

public interface ShareItHeadersBuilder {
    HttpHeaders getUserIdHeader(Long userId);
}
