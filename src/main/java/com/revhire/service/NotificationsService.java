package com.revhire.service;

import com.revhire.dao.NotificationsDAO;
import com.revhire.model.Notification;
import com.revhire.service.impl.NotificationsServiceimpl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class NotificationsService implements NotificationsServiceimpl {

    private final NotificationsDAO notificationsDAO = new NotificationsDAO();

    public void addNotification(int userId, String message) {
        try {
            notificationsDAO.insertNotification(userId, message);
        } catch (SQLException e) {
            System.out.println("❌ Failed to send notification: " + e.getMessage());
        }
    }

    public List<Notification> fetchUnreadNotifications(int userId) {
        try {
            return notificationsDAO.fetchUnreadNotifications(userId);
        } catch (SQLException e) {
            System.out.println("❌ Unable to fetch unread notifications: " + e.getMessage());
            return List.of();
        }
    }

    public List<String> showUnreadNotifications(int userId) {
        List<Notification> notes = fetchUnreadNotifications(userId);
        List<String> messages = new ArrayList<>();
        for (Notification n : notes) {
            messages.add("🔔 " + n.getMessage() + " (" + n.getCreatedAt() + ")");
        }
        return messages;
    }

    // ✅ Updated mark all as read safely
    public void markAllAsRead(int userId) {
        try {
            int rows = notificationsDAO.markAllRead(userId);
            if (rows > 0) System.out.println("✅ All notifications marked as read.");
            else System.out.println("⚠️ No notifications to update.");
        } catch (SQLException e) {
            System.out.println("❌ Failed to update notifications: " + e.getMessage());
        }
    }

    public int getUnreadNotificationCount(int userId) {
        try {
            return notificationsDAO.countUnreadNotifications(userId);
        } catch (SQLException e) {
            return 0;
        }
    }
}
