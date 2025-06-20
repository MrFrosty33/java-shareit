package ru.practicum.server.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    @Query("""
            SELECT u.email FROM User u WHERE u.id != :userId
            """)
    List<String> getEmails(@Param("userId") Long userId);
}
