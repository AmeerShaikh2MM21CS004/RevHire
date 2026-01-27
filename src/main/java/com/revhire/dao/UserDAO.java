package com.revhire.dao;

import com.revhire.dao.impl.UserDAOimpl;
import com.revhire.util.DBConnection;

import java.sql.*;

public class UserDAO implements UserDAOimpl {

    // ---------- FETCH USER FOR LOGIN ----------
    public ResultSet fetchLoginData(String email) throws SQLException {

        String sql = "SELECT user_id, password_hash, role FROM users WHERE email = ?";

        Connection conn = DBConnection.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, email);

        return ps.executeQuery();
    }

    // ---------- UPDATE PASSWORD ----------
    public boolean updatePasswordByUserId(int userId, String newHash) throws SQLException {

        String sql = "UPDATE users SET password_hash = ? WHERE user_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, newHash);
            ps.setInt(2, userId);
            return ps.executeUpdate() > 0;
        }
    }

    public boolean updatePasswordByEmail(String email, String newHash) throws SQLException {

        String sql = "UPDATE users SET password_hash = ? WHERE email = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, newHash);
            ps.setString(2, email);
            return ps.executeUpdate() > 0;
        }
    }

    // ---------- SECURITY ----------
    public String fetchSecurityQuestion(String email) throws SQLException {

        String sql = "SELECT security_question FROM users WHERE email = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            return rs.next() ? rs.getString(1) : null;
        }
    }

    public String fetchSecurityAnswerHash(String email) throws SQLException {

        String sql = "SELECT security_answer_hash FROM users WHERE email = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            return rs.next() ? rs.getString(1) : null;
        }
    }

    public int insertUserAndReturnId(
            String email,
            String passwordHash,
            String role,
            String securityQuestion,
            String securityAnswerHash) {

        String sql = """
        INSERT INTO users (email, password_hash, role, security_question, security_answer_hash)
        VALUES (?, ?, ?, ?, ?)
    """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(
                     sql, new String[]{"user_id"})) {

            ps.setString(1, email);
            ps.setString(2, passwordHash);
            ps.setString(3, role);
            ps.setString(4, securityQuestion);
            ps.setString(5, securityAnswerHash);

            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1);
            }

            throw new RuntimeException("User ID not generated");

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public ResultSet fetchLoginDataById(int userId) throws SQLException {
        String sql = "SELECT user_id, email, password_hash, role FROM users WHERE user_id=?";
        Connection conn = DBConnection.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, userId);
        return ps.executeQuery();
    }


}
