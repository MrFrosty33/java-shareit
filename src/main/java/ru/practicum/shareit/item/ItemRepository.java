package ru.practicum.shareit.item;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {
    List<Item> findByOwnerId(Long ownerId);

    @Query("""
            SELECT i.available FROM Item i
            WHERE i.id = :itemId
            """)
    Boolean isItemAvailable(@Param("itemId") Long itemId);
}
