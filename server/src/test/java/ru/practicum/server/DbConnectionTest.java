package ru.practicum.server;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class DbConnectionTest {
    @Autowired
    private DataSource dataSource;

    @Test
    void testDbConnection() throws SQLException {
        try (Connection conn = dataSource.getConnection()) {
            DatabaseMetaData meta = conn.getMetaData();
            assertEquals("H2", meta.getDatabaseProductName());
            assertEquals("jdbc:h2:file:./db/share-it", meta.getURL());
        }
    }
}
