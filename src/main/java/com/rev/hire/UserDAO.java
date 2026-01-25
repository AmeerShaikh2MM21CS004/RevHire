package com.rev.hire;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {

    // Add a new user (ID auto-generated)
    public void addUser(String email, String passwordHash, String role) {
        String sql = "INSERT INTO users (email, password_hash, role) VALUES (?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, email);
            pstmt.setString(2, passwordHash);
            pstmt.setString(3, role);

            pstmt.executeUpdate();
            System.out.println("✅ User inserted successfully.");
        } catch (SQLException e) {
            System.out.println("⚠️ Error inserting user: " + e.getMessage());
        }
    }

    // Get all users
    public List<String> getAllUsers() {
        List<String> users = new ArrayList<>();
        String sql = "SELECT user_id, email, role, created_at FROM users";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                users.add(rs.getInt("user_id") + " | " +
                        rs.getString("email") + " | " +
                        rs.getString("role") + " | " +
                        rs.getTimestamp("created_at"));
            }
        } catch (SQLException e) {
            System.out.println("⚠️ Error fetching users: " + e.getMessage());
        }
        return users;
    }

    // Get user by ID
    public String getUserById(int userId) {
        String sql = "SELECT user_id, email, role, created_at FROM users WHERE user_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, userId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("user_id") + " | " +
                            rs.getString("email") + " | " +
                            rs.getString("role") + " | " +
                            rs.getTimestamp("created_at");
                }
            }
        } catch (SQLException e) {
            System.out.println("⚠️ Error fetching user by ID: " + e.getMessage());
        }
        return null;
    }
}
