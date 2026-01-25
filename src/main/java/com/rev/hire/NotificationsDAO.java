package com.rev.hire;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class NotificationsDAO {

    // 🔔 Create notification
    public void addNotification(int userId, String message) {

        String sql = """
            INSERT INTO notifications (user_id, message)
            VALUES (?, ?)
        """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ps.setString(2, message);
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 📥 View notifications
    public static List<String> getUserNotifications(int userId) {

        List<String> notes = new ArrayList<>();

        String sql = """
            SELECT notification_id, message, is_read, created_at
            FROM notifications
            WHERE user_id = ?
            ORDER BY created_at DESC
        """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                notes.add(
                        rs.getInt("notification_id") + " | " +
                                rs.getString("message") + " | " +
                                (rs.getString("is_read").equals("Y") ? "READ" : "UNREAD") +
                                " | " + rs.getTimestamp("created_at")
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return notes;
    }

    // ✅ Mark notification as read
    public void markAsRead(int notificationId) {

        String sql = "UPDATE notifications SET is_read='Y' WHERE notification_id=?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, notificationId);
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
