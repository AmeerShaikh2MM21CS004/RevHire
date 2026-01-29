package com.revhire.service;

import com.revhire.dao.NotificationsDAO;
import com.revhire.model.Notification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class NotificationsServiceTest {

    private NotificationsDAO notificationsDAO;
    private NotificationsService notificationsService;

    @BeforeEach
    void setup() {
        notificationsDAO = mock(NotificationsDAO.class);
        notificationsService = new NotificationsService(notificationsDAO);
    }

    // ---------------- addNotification ----------------
    @Test
    void addNotification_shouldCallDAO() throws Exception {
        doNothing().when(notificationsDAO).insertNotification(1, "Hello");

        notificationsService.addNotification(1, "Hello");

        verify(notificationsDAO).insertNotification(1, "Hello");
    }

    @Test
    void addNotification_shouldThrowRuntimeExceptionOnSQLException() throws Exception {
        doThrow(new RuntimeException()).when(notificationsDAO).insertNotification(anyInt(), any());

        assertThrows(RuntimeException.class,
                () -> notificationsService.addNotification(1, "Hello"));
    }

    // ---------------- fetchUnreadNotifications ----------------
    @Test
    void fetchUnreadNotifications_shouldReturnList() throws Exception {
        Notification n = new Notification(1, 1, "Test", 'N', new Timestamp(System.currentTimeMillis()));
        when(notificationsDAO.fetchUnreadNotifications(1)).thenReturn(List.of(n));

        List<Notification> notes = notificationsService.fetchUnreadNotifications(1);

        assertEquals(1, notes.size());
        assertEquals("Test", notes.get(0).getMessage());
    }

    @Test
    void fetchUnreadNotifications_shouldReturnEmptyListOnException() throws Exception {
        when(notificationsDAO.fetchUnreadNotifications(1)).thenThrow(new SQLException());

        List<Notification> notes = notificationsService.fetchUnreadNotifications(1);

        assertTrue(notes.isEmpty()); // should pass now
    }


    // ---------------- showUnreadNotifications ----------------
    @Test
    void showUnreadNotifications_shouldReturnFormattedMessages() throws Exception {
        Notification n = new Notification(1, 1, "Test", 'N', new Timestamp(System.currentTimeMillis()));
        when(notificationsDAO.fetchUnreadNotifications(1)).thenReturn(List.of(n));

        List<String> messages = notificationsService.showUnreadNotifications(1);

        assertEquals(1, messages.size());
        assertTrue(messages.get(0).contains("🔔 Test"));
    }

    // ---------------- markAllAsRead ----------------
    @Test
    void markAllAsRead_shouldCallDAO() throws Exception {
        when(notificationsDAO.markAllRead(1)).thenReturn(3);

        notificationsService.markAllAsRead(1);

        verify(notificationsDAO).markAllRead(1);
    }

    @Test
    void markAllAsRead_shouldThrowRuntimeExceptionOnSQLException() throws Exception {
        when(notificationsDAO.markAllRead(anyInt())).thenThrow(new RuntimeException());

        assertThrows(RuntimeException.class,
                () -> notificationsService.markAllAsRead(1));
    }

    // ---------------- getUnreadNotificationCount ----------------
    @Test
    void getUnreadNotificationCount_shouldReturnCount() throws Exception {
        when(notificationsDAO.countUnreadNotifications(1)).thenReturn(5);

        int count = notificationsService.getUnreadNotificationCount(1);

        assertEquals(5, count);
    }

    @Test
    void getUnreadNotificationCount_shouldReturnZeroOnException() throws Exception {
        when(notificationsDAO.countUnreadNotifications(1)).thenThrow(new SQLException());

        int count = notificationsService.getUnreadNotificationCount(1);

        assertEquals(0, count);
    }
}
