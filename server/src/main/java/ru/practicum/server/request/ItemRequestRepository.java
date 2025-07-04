package ru.practicum.server.request;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemRequestRepository extends JpaRepository<ItemRequest, Long> {
    List<ItemRequest> findByRequesterIdOrderByCreatedAsc(Long userId);

    List<ItemRequest> findByRequesterIdNotOrderByCreatedAsc(Long userId);
}
