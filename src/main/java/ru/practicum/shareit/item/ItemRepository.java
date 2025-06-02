package ru.practicum.shareit.item;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {
    List<Item> findByOwnerId(Long ownerId);

    // можно ли обновлять владельца и запрос?
    @Modifying
    @Query("""
            UPDATE Item i SET
            i.name = COALESCE(:name, i.name),
            i.description = COALESCE(:description, i.description),
            i.available = COALESCE(:available, i.available)
            WHERE i.id = :id
            """)
    void updateItem(@Param("name") String name, @Param("description") String description,
                    @Param("available") Boolean available, @Param("id") Long id);
}
