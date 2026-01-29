package com.revhire.model;

import org.junit.jupiter.api.Test;

import java.sql.Timestamp;

import static org.junit.jupiter.api.Assertions.*;

class NotificationTest {

    @Test
    void testConstructorForNewNotification() {
        // Arrange
        int userId = 101;
        String message = "Your application has been shortlisted";

        // Act
        Notification notification = new Notification(userId, message);

        // Assert
        assertEquals(userId, notification.getUserId());
        assertEquals(message, notification.getMessage());
        assertEquals('N', notification.getIsRead());
        assertNotNull(notification.getCreatedAt());
    }

    @Test
    void testFullConstructorFromDatabase() {
        // Arrange
        Timestamp createdAt = Timestamp.valueOf("2025-01-18 11:20:00");

        // Act
        Notification notification = new Notification(
                1,
                101,
                "Interview scheduled",
                'Y',
                createdAt
        );

        // Assert
        assertEquals(1, notification.getNotificationId());
        assertEquals(101, notification.getUserId());
        assertEquals("Interview scheduled", notification.getMessage());
        assertEquals('Y', notification.getIsRead());
        assertEquals(createdAt, notification.getCreatedAt());
    }

    @Test
    void testSettersAndGetters() {
        // Arrange
        Notification notification = new Notification(101, "Initial Message");
        Timestamp updatedTime = Timestamp.valueOf("2025-02-01 09:00:00");

        // Act
        notification.setNotificationId(10);
        notification.setUserId(202);
        notification.setMessage("Updated Message");
        notification.setIsRead('Y');
        notification.setCreatedAt(updatedTime);

        // Assert
        assertEquals(10, notification.getNotificationId());
        assertEquals(202, notification.getUserId());
        assertEquals("Updated Message", notification.getMessage());
        assertEquals('Y', notification.getIsRead());
        assertEquals(updatedTime, notification.getCreatedAt());
    }
}
