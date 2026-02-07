package com.revhire.service;

import com.revhire.dao.impl.NotificationsDAOImpl;
import com.revhire.model.Notification;
import com.revhire.service.impl.NotificationsServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class NotificationsServiceImplTest {

    private NotificationsDAOImpl notificationsDAOImpl;
    private NotificationsServiceImpl notificationsServiceImpl;

    @BeforeEach
    void setup() {
        notificationsDAOImpl = mock(NotificationsDAOImpl.class);
        notificationsServiceImpl = new NotificationsServiceImpl(notificationsDAOImpl);
    }

    // ---------------- addNotification ----------------
    @Test
    void addNotification_shouldCallDAO() throws Exception {
        doNothing().when(notificationsDAOImpl).insertNotification(1, "Hello");

        notificationsServiceImpl.addNotification(1, "Hello");

        verify(notificationsDAOImpl).insertNotification(1, "Hello");
    }

    @Test
    void addNotification_shouldThrowRuntimeExceptionOnSQLException() throws Exception {
        doThrow(new RuntimeException()).when(notificationsDAOImpl).insertNotification(anyInt(), any());

        assertThrows(RuntimeException.class,
                () -> notificationsServiceImpl.addNotification(1, "Hello"));
    }

    // ---------------- fetchUnreadNotifications ----------------
    @Test
    void fetchUnreadNotifications_shouldReturnList() throws Exception {
        Notification n = new Notification(1, 1, "Test", 'N', new Timestamp(System.currentTimeMillis()));
        when(notificationsDAOImpl.fetchUnreadNotifications(1)).thenReturn(List.of(n));

        List<Notification> notes = notificationsServiceImpl.fetchUnreadNotifications(1);

        assertEquals(1, notes.size());
        assertEquals("Test", notes.get(0).getMessage());
    }

    @Test
    void fetchUnreadNotifications_shouldReturnEmptyListOnException() throws Exception {
        when(notificationsDAOImpl.fetchUnreadNotifications(1)).thenThrow(new SQLException());

        List<Notification> notes = notificationsServiceImpl.fetchUnreadNotifications(1);

        assertTrue(notes.isEmpty()); // should pass now
    }


    // ---------------- showUnreadNotifications ----------------
    @Test
    void showUnreadNotifications_shouldReturnFormattedMessages() throws Exception {
        Notification n = new Notification(1, 1, "Test", 'N', new Timestamp(System.currentTimeMillis()));
        when(notificationsDAOImpl.fetchUnreadNotifications(1)).thenReturn(List.of(n));

        List<String> messages = notificationsServiceImpl.showUnreadNotifications(1);

        assertEquals(1, messages.size());
        assertTrue(messages.get(0).contains("🔔 Test"));
    }

    // ---------------- markAllAsRead ----------------
    @Test
    void markAllAsRead_shouldCallDAO() throws Exception {
        when(notificationsDAOImpl.markAllRead(1)).thenReturn(3);

        notificationsServiceImpl.markAllAsRead(1);

        verify(notificationsDAOImpl).markAllRead(1);
    }

    @Test
    void markAllAsRead_shouldThrowRuntimeExceptionOnSQLException() throws Exception {
        when(notificationsDAOImpl.markAllRead(anyInt())).thenThrow(new RuntimeException());

        assertThrows(RuntimeException.class,
                () -> notificationsServiceImpl.markAllAsRead(1));
    }

    // ---------------- getUnreadNotificationCount ----------------
    @Test
    void getUnreadNotificationCount_shouldReturnCount() throws Exception {
        when(notificationsDAOImpl.countUnreadNotifications(1)).thenReturn(5);

        int count = notificationsServiceImpl.getUnreadNotificationCount(1);

        assertEquals(5, count);
    }

    @Test
    void getUnreadNotificationCount_shouldReturnZeroOnException() throws Exception {
        when(notificationsDAOImpl.countUnreadNotifications(1)).thenThrow(new SQLException());

        int count = notificationsServiceImpl.getUnreadNotificationCount(1);

        assertEquals(0, count);
    }
}
