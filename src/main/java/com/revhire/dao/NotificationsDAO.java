package com.revhire.dao;

import com.revhire.model.Notification;
import com.revhire.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class NotificationsDAO {

    // Insert notification
    public void insertNotification(int userId, String message) throws SQLException {
        String sql = "INSERT INTO notifications (user_id, message) VALUES (?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setString(2, message);
            ps.executeUpdate();
        }
    }

    // Fetch unread notifications
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
            ps.setQueryTimeout(5); // max 5 sec
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
        }
        return list;
    }

    // Fetch all notifications
    public List<Notification> fetchAllNotifications(int userId) throws SQLException {
        String sql = "SELECT notification_id, user_id, message, is_read, created_at " +
                "FROM notifications WHERE user_id = ? ORDER BY created_at DESC";
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
        }
        return list;
    }

    // ✅ Updated: mark all as read safely
    public int markAllRead(int userId) throws SQLException {
        String sql = "UPDATE notifications SET is_read = 'Y' WHERE user_id = ? AND is_read = 'N'";
        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false);
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, userId);
                ps.setQueryTimeout(3); // prevent long hang
                int updatedRows = ps.executeUpdate();
                conn.commit();
                return updatedRows;
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            }
        }
    }

    public int countUnreadNotifications(int userId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM notifications WHERE user_id = ? AND is_read = 'N'";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt(1);
            }
        }
        return 0;
    }
}
