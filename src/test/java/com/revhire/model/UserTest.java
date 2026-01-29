package com.revhire.model;

import org.junit.jupiter.api.Test;

import java.sql.Timestamp;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Test
    void testFullConstructorAndGetters() {
        // Arrange
        Timestamp createdAt = Timestamp.valueOf("2025-01-10 08:30:00");

        // Act
        User user = new User(
                1,
                "user@revhire.com",
                "JOB_SEEKER",
                createdAt
        );

        // Assert
        assertEquals(1, user.getUserId());
        assertEquals("user@revhire.com", user.getEmail());
        assertEquals("JOB_SEEKER", user.getRole());
        assertEquals(createdAt, user.getCreatedAt());
    }

    @Test
    void testLoginConstructorWithoutCreatedAt() {
        // Act
        User user = new User(
                2,
                "employer@revhire.com",
                "EMPLOYER"
        );

        // Assert
        assertEquals(2, user.getUserId());
        assertEquals("employer@revhire.com", user.getEmail());
        assertEquals("EMPLOYER", user.getRole());
        assertNull(user.getCreatedAt());
    }

    @Test
    void testSettersAndGetters() {
        // Arrange
        User user = new User(0, null, null, null);
        Timestamp newTime = Timestamp.valueOf("2025-02-05 12:00:00");

        // Act
        user.setUserId(3);
        user.setEmail("admin@revhire.com");
        user.setRole("ADMIN");
        user.setCreatedAt(newTime);

        // Assert
        assertEquals(3, user.getUserId());
        assertEquals("admin@revhire.com", user.getEmail());
        assertEquals("ADMIN", user.getRole());
        assertEquals(newTime, user.getCreatedAt());
    }
}
