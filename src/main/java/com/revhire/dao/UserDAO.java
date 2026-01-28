package com.revhire.dao;

import com.revhire.dao.impl.UserDAOimpl;
import com.revhire.util.DBConnection;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;

public class UserDAO implements UserDAOimpl {

    private static final Logger logger =
            LogManager.getLogger(UserDAO.class);

    // ---------- FETCH USER FOR LOGIN ----------
    @Override
    public ResultSet fetchLoginData(String email) throws SQLException {

        String sql = """
            SELECT user_id, email, password_hash, role
            FROM users
            WHERE email = ?
        """;

        logger.info("Fetching login data for email={}", email);

        Connection conn = DBConnection.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, email);

        return ps.executeQuery(); // closed by caller (service layer)
    }

    // ---------- UPDATE PASSWORD ----------
    @Override
    public boolean updatePasswordByUserId(int userId, String newHash) throws SQLException {

        String sql = "UPDATE users SET password_hash = ? WHERE user_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, newHash);
            ps.setInt(2, userId);

            int rows = ps.executeUpdate();
            logger.info("Password updated by userId={}, rowsAffected={}", userId, rows);
            System.out.println();
            System.out.println("Password updated");

            return rows > 0;

        } catch (SQLException e) {
            logger.error("Error updating password for userId={}", userId, e);
            System.out.println();
            System.out.println("Error updating password");
            throw e;
        }
    }

    @Override
    public boolean updatePasswordByEmail(String email, String newHash) throws SQLException {

        String sql = "UPDATE users SET password_hash = ? WHERE email = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, newHash);
            ps.setString(2, email);

            int rows = ps.executeUpdate();
            logger.info("Password updated for email={}, rowsAffected={}", email, rows);

            return rows > 0;

        } catch (SQLException e) {
            logger.error("Error updating password for email={}", email, e);
            throw e;
        }
    }

    // ---------- SECURITY ----------
    @Override
    public String fetchSecurityQuestion(String email) throws SQLException {

        String sql = "SELECT security_question FROM users WHERE email = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                logger.info("Security question fetched for email={}", email);
                return rs.getString(1);
            }

            logger.warn("No security question found for email={}", email);
            return null;

        } catch (SQLException e) {
            logger.error("Error fetching security question for email={}", email, e);
            System.out.println();
            System.out.println("Error fetching security question");
            throw e;
        }
    }

    @Override
    public String fetchSecurityAnswerHash(String email) throws SQLException {

        String sql = "SELECT security_answer_hash FROM users WHERE email = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                logger.info("Security answer hash fetched for email={}", email);
                return rs.getString(1);
            }

            logger.warn("No security answer hash found for email={}", email);
            return null;

        } catch (SQLException e) {
            logger.error("Error fetching security answer hash for email={}", email, e);
            throw e;
        }
    }

    @Override
    public int insertUserAndReturnId(
            String email,
            String passwordHash,
            String role,
            String securityQuestion,
            String securityAnswerHash) {

        String sql = """
            INSERT INTO users
            (email, password_hash, role, security_question, security_answer_hash)
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
                int userId = rs.getInt(1);
                logger.info("User created successfully, userId={}, email={}", userId, email);
                System.out.println();
                System.out.println("User created successfully");
                return userId;
            }

            throw new RuntimeException("User ID not generated");

        } catch (SQLException e) {
            logger.error("Error creating user with email={}", email, e);
            System.out.println();
            System.out.println("Error creating user");
            throw new RuntimeException(e);
        }
    }

    @Override
    public ResultSet fetchLoginDataById(int userId) throws SQLException {

        String sql = """
            SELECT user_id, email, password_hash, role
            FROM users
            WHERE user_id = ?
        """;

        logger.info("Fetching login data for userId={}", userId);

        Connection conn = DBConnection.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, userId);

        return ps.executeQuery();
    }
}
