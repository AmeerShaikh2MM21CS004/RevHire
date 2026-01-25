package com.rev.hire;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {

    /// Register user and return generated user_id
    public int addUserAndReturnId(String email, String passwordHash, String role) {
        String sql = "INSERT INTO users (email, password_hash, role) VALUES (?, ?, ?)";
        int userId = -1;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt =
                     conn.prepareStatement(sql, new String[]{"user_id"})) {

            pstmt.setString(1, email);
            pstmt.setString(2, passwordHash);
            pstmt.setString(3, role);

            pstmt.executeUpdate();

            ResultSet rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                userId = rs.getInt(1);
            }

        } catch (SQLException e) {
            System.out.println("⚠️ Error inserting user: " + e.getMessage());
        }
        return userId;
    }

    // LOGIN
    public User login(String email, String passwordHash) {
        String sql = "SELECT user_id, role FROM users WHERE email = ? AND password_hash = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, email);
            pstmt.setString(2, passwordHash);

            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new User(
                        rs.getInt("user_id"),
                        email,
                        rs.getString("role")
                );
            }
        } catch (SQLException e) {
            System.out.println("⚠️ Login error: " + e.getMessage());
        }
        return null;
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
