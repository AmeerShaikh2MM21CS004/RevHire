package com.revhire.dao.impl;

import com.revhire.dao.NotificationsDAO;
import com.revhire.model.Notification;
import com.revhire.util.DBConnection;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class NotificationsDAOImpl implements NotificationsDAO {

    private static final Logger logger =  LogManager.getLogger(NotificationsDAOImpl.class);

    // Insert notification
    @Override
    public void insertNotification(int userId, String message) throws SQLException {
        String sql = "INSERT INTO notifications (user_id, message) VALUES (?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ps.setString(2, message);
            ps.executeUpdate();

            logger.info("Notification inserted for userId={}", userId);

        } catch (SQLException e) {
            logger.error("Error inserting notification for userId={}", userId, e);
            throw e;
        }
    }

    // Fetch unread notifications
    @Override
    public List<Notification> fetchUnreadNotifications(int userId) throws SQLException {
        String sql = """
            SELECT notification_id, user_id, message, is_read, created_at
            FROM notifications
            WHERE user_id = ? AND is_read = 'N'
            ORDER BY created_at DESC
        """;

        List<Notification> list = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ps.setQueryTimeout(5);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(new Notification(
                            rs.getInt("notification_id"),
                            rs.getInt("user_id"),
                            rs.getString("message"),
                            rs.getString("is_read").charAt(0),
                            rs.getTimestamp("created_at")
                    ));
                }
            }

            logger.info("Fetched {} unread notifications for userId={}",
                    list.size(), userId);

        } catch (SQLException e) {
            logger.error("Error fetching unread notifications for userId={}", userId, e);
            throw e;
        }

        return list;
    }

    // Fetch all notifications
    @Override
    public List<Notification> fetchAllNotifications(int userId) throws SQLException {
        String sql = """
            SELECT notification_id, user_id, message, is_read, created_at
            FROM notifications
            WHERE user_id = ?
            ORDER BY created_at DESC
        """;

        List<Notification> list = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(new Notification(
                            rs.getInt("notification_id"),
                            rs.getInt("user_id"),
                            rs.getString("message"),
                            rs.getString("is_read").charAt(0),
                            rs.getTimestamp("created_at")
                    ));
                }
            }

            logger.info("Fetched {} total notifications for userId={}",
                    list.size(), userId);

        } catch (SQLException e) {
            logger.error("Error fetching notifications for userId={}", userId, e);
            throw e;
        }

        return list;
    }

    // Mark all as read (transaction-safe)
    @Override
    public int markAllRead(int userId) throws SQLException {
        String sql =
                "UPDATE notifications SET is_read = 'Y' WHERE user_id = ? AND is_read = 'N'";

        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false);

            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, userId);
                ps.setQueryTimeout(3);

                int updatedRows = ps.executeUpdate();
                conn.commit();

                logger.info("Marked {} notifications as read for userId={}",
                        updatedRows, userId);

                return updatedRows;

            } catch (SQLException e) {
                conn.rollback();
                logger.error("Rollback: failed to mark notifications as read for userId={}",
                        userId, e);
                throw e;
            }
        }
    }

    // Count unread notifications
    @Override
    public int countUnreadNotifications(int userId) throws SQLException {
        String sql =
                "SELECT COUNT(*) FROM notifications WHERE user_id = ? AND is_read = 'N'";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    int count = rs.getInt(1);
                    logger.debug("Unread notification count for userId={} is {}",
                            userId, count);
                    return count;
                }
            }

        } catch (SQLException e) {
            logger.error("Error counting unread notifications for userId={}", userId, e);
            throw e;
        }

        return 0;
    }
}
