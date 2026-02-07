package com.revhire.service.impl;

import com.revhire.dao.impl.NotificationsDAOImpl;
import com.revhire.model.Notification;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class NotificationsServiceImpl implements com.revhire.service.NotificationsService {

    private static final Logger logger =
            LogManager.getLogger(NotificationsServiceImpl.class);

    private final NotificationsDAOImpl notificationsDAOImpl;

    // Default constructor
    public NotificationsServiceImpl() {
        this.notificationsDAOImpl = new NotificationsDAOImpl();
    }

    // Constructor for unit testing
    public NotificationsServiceImpl(NotificationsDAOImpl notificationsDAOImpl) {
        this.notificationsDAOImpl = notificationsDAOImpl;
    }

    // ---------------- ADD NOTIFICATION ----------------
    @Override
    public void addNotification(int userId, String message) {

        logger.info("Adding notification | userId={}, message={}", userId, message);

        try {
            notificationsDAOImpl.insertNotification(userId, message);
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
            return notificationsDAOImpl.fetchUnreadNotifications(userId);

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
            int rows = notificationsDAOImpl.markAllRead(userId);

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
            return notificationsDAOImpl.countUnreadNotifications(userId);
        } catch (SQLException e) {
            logger.error("Failed to count unread notifications | userId={}", userId, e);
            return 0;
        }
    }
}
