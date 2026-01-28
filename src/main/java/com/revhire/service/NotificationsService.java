package com.revhire.service;

import com.revhire.dao.NotificationsDAO;
import com.revhire.model.Notification;
import com.revhire.service.impl.NotificationsServiceimpl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class NotificationsService implements NotificationsServiceimpl {

    private static final Logger logger =
            LogManager.getLogger(NotificationsService.class);

    private final NotificationsDAO notificationsDAO = new NotificationsDAO();

    // ---------------- ADD NOTIFICATION ----------------
    @Override
    public void addNotification(int userId, String message) {

        logger.info("Adding notification | userId={}, message={}", userId, message);

        try {
            notificationsDAO.insertNotification(userId, message);
            logger.info("Notification sent successfully | userId={}", userId);

        } catch (SQLException e) {
            logger.error("Failed to send notification | userId={}", userId, e);
            throw new RuntimeException("Failed to send notification", e);
        }
    }

    // ---------------- FETCH UNREAD NOTIFICATIONS ----------------
    @Override
    public List<Notification> fetchUnreadNotifications(int userId) {

        logger.info("Fetching unread notifications | userId={}", userId);

        try {
            return notificationsDAO.fetchUnreadNotifications(userId);

        } catch (SQLException e) {
            logger.error("Unable to fetch unread notifications | userId={}", userId, e);
            return List.of(); // safe fallback
        }
    }

    // ---------------- SHOW UNREAD NOTIFICATIONS ----------------
    @Override
    public List<String> showUnreadNotifications(int userId) {

        logger.info("Preparing unread notification messages | userId={}", userId);

        List<Notification> notes = fetchUnreadNotifications(userId);
        List<String> messages = new ArrayList<>();

        for (Notification n : notes) {
            messages.add("🔔 " + n.getMessage() + " (" + n.getCreatedAt() + ")");
        }

        logger.info("Unread notifications prepared | count={}", messages.size());
        return messages;
    }

    // ---------------- MARK ALL AS READ ----------------
    @Override
    public void markAllAsRead(int userId) {

        logger.info("Marking all notifications as read | userId={}", userId);

        try {
            int rows = notificationsDAO.markAllRead(userId);

            if (rows > 0) {
                logger.info("All notifications marked as read | userId={}", userId);
            } else {
                logger.warn("No unread notifications found | userId={}", userId);
            }

        } catch (SQLException e) {
            logger.error("Failed to mark notifications as read | userId={}", userId, e);
            throw new RuntimeException("Failed to update notifications", e);
        }
    }

    // ---------------- COUNT UNREAD NOTIFICATIONS ----------------
    @Override
    public int getUnreadNotificationCount(int userId) {

        logger.debug("Counting unread notifications | userId={}", userId);

        try {
            return notificationsDAO.countUnreadNotifications(userId);
        } catch (SQLException e) {
            logger.error("Failed to count unread notifications | userId={}", userId, e);
            return 0;
        }
    }
}
