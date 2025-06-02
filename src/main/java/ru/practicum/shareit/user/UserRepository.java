package ru.practicum.shareit.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {

    @Modifying
    @Query("""
            UPDATE User u SET
            u.name = COALESCE(:name, u.name),
            u.email = COALESCE(:email, u.email)
            WHERE u.id = :id
            """)
    void updateUser(@Param("name") String name, @Param("email") String email, @Param("id") Long id);

    @Query("""
            SELECT u.email FROM User u WHERE u.id != :userId
            """)
    List<String> getEmails(@Param("userId") Long userId);
}
