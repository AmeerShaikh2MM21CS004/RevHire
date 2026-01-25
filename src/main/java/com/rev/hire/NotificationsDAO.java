package com.rev.hire;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class NotificationsDAO {

    public void addNotification(int userId, String message, char isRead) {
        String sql = "INSERT INTO notifications (user_id, message, is_read) VALUES (?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, userId);
            pstmt.setString(2, message);
            pstmt.setString(3, String.valueOf(isRead));

            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<String> getAllNotifications() {
        List<String> notifications = new ArrayList<>();
        String sql = "SELECT notification_id, user_id, is_read, created_at FROM notifications";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                notifications.add(rs.getInt("notification_id") + " | " + rs.getInt("user_id") + " | " +
                        rs.getString("is_read") + " | " + rs.getTimestamp("created_at"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return notifications;
    }

    public String getNotificationById(int notificationId) {
        String sql = "SELECT * FROM notifications WHERE notification_id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, notificationId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("notification_id") + " | " + rs.getInt("user_id") + " | " +
                            rs.getString("message") + " | " + rs.getString("is_read") + " | " +
                            rs.getTimestamp("created_at");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}

