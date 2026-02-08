package com.revhire.service.impl;

import com.revhire.dao.impl.JobSeekersDAOImpl;
import com.revhire.dao.impl.NotificationsDAOImpl;
import com.revhire.model.Notification;
import com.revhire.service.NotificationsService;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class NotificationsServiceImpl implements NotificationsService {

    private static final Logger logger =
            LogManager.getLogger(NotificationsServiceImpl.class);

    private final NotificationsDAOImpl notificationsDAOImpl;
    private final JobSeekersDAOImpl jobSeekersDAO;

    // Default constructor
    public NotificationsServiceImpl() {
        this.notificationsDAOImpl = new NotificationsDAOImpl();
        this.jobSeekersDAO = new JobSeekersDAOImpl();
    }

    // Constructor for unit testing
    public NotificationsServiceImpl(
            NotificationsDAOImpl notificationsDAOImpl,
            JobSeekersDAOImpl jobSeekersDAO) {

        this.notificationsDAOImpl = notificationsDAOImpl;
        this.jobSeekersDAO = jobSeekersDAO;
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
            return List.of();
        }
    }

    // ---------------- SHOW UNREAD NOTIFICATIONS ----------------
    @Override
    public List<String> showUnreadNotifications(int userId) {

        List<Notification> notes = fetchUnreadNotifications(userId);
        List<String> messages = new ArrayList<>();

        for (Notification n : notes) {
            messages.add("🔔 " + n.getMessage() + " (" + n.getCreatedAt() + ")");
        }

        return messages;
    }

    // ---------------- MARK ALL AS READ ----------------
    @Override
    public void markAllAsRead(int userId) {

        try {
            notificationsDAOImpl.markAllRead(userId);
            logger.info("All notifications marked as read | userId={}", userId);

        } catch (SQLException e) {
            logger.error("Failed to mark notifications as read | userId={}", userId, e);
            throw new RuntimeException(e);
        }
    }

    // ---------------- COUNT UNREAD ----------------
    @Override
    public int getUnreadNotificationCount(int userId) {

        try {
            return notificationsDAOImpl.countUnreadNotifications(userId);
        } catch (SQLException e) {
            logger.error("Failed to count unread notifications | userId={}", userId, e);
            return 0;
        }
    }

    // 🔔 JOB MATCH NOTIFICATIONS (FIXED)
    @Override
    public void notifyMatchingJobSeekers(
            String jobTitle,
            String skills,
            int experience,
            String location) {

        try {
            List<Integer> userIds =
                    jobSeekersDAO.findMatchingSeekerUserIds(skills, experience, location);

            for (int userId : userIds) {
                String message =
                        "New job match: " + jobTitle + " in " + location;
                addNotification(userId, message);
            }

            logger.info("Job match notifications sent | count={}", userIds.size());

        } catch (Exception e) {
            logger.error("Failed to notify matching job seekers", e);
        }
    }
}
