package com.revhire.service.impl;

import com.revhire.dao.impl.*;
import com.revhire.model.User;
import com.revhire.util.DBConnection;
import com.revhire.util.HashUtil;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserServiceImpl implements com.revhire.service.UserService {

    private static final Logger logger = LogManager.getLogger(UserServiceImpl.class);
    private final UserDAOImpl userDAOImpl;


    // Default constructor
    public UserServiceImpl() {
        this.userDAOImpl = new UserDAOImpl();
    }

    // Constructor for unit testing
    public UserServiceImpl(UserDAOImpl userDAOImpl) {
        this.userDAOImpl = userDAOImpl;
    }

    // ---------- REGISTER ----------
    @Override
    public int addUserAndReturnId(
            String email,
            String passwordHash,
            String role,
            String securityQuestion,
            String securityAnswerHash) {

        logger.info("Registering new user | email={}, role={}", email, role);

        int userId = userDAOImpl.insertUserAndReturnId(
                email, passwordHash, role, securityQuestion, securityAnswerHash
        );
        logger.info("User registered successfully | userId={}", userId);
        return userId;

    }

    // ---------- LOGIN ----------
    @Override
    public User login(String email, String password) {
        logger.info("User login attempt | email={}", email);

        try (ResultSet rs = userDAOImpl.fetchLoginData(email)) {

            if (!rs.next()) {
                logger.warn("Login failed: no user found | email={}", email);
                return null;
            }

            String dbHash = rs.getString("password_hash");
            if (!dbHash.equals(HashUtil.hash(password))) {
                logger.warn("Login failed: invalid password | email={}", email);
                return null;
            }

            User user = new User(rs.getInt("user_id"), email, rs.getString("role"));
            logger.info("Login successful | userId={}", user.getUserId());
            return user;

        } catch (SQLException e) {
            logger.error("Login failed due to SQL error | email={}", email, e);
            return null;
        }
    }

    // ---------- CHANGE PASSWORD ----------
    @Override
    public boolean changePassword(int userId, String currentPwd, String newPwd) {
        logger.info("Change password request | userId={}", userId);

        try (ResultSet rs = userDAOImpl.fetchLoginDataById(userId)) {

            if (!rs.next()) {
                logger.warn("Change password failed: user not found | userId={}", userId);
                return false;
            }

            if (!rs.getString("password_hash").equals(HashUtil.hash(currentPwd))) {
                logger.warn("Change password failed: incorrect current password | userId={}", userId);
                return false;
            }

            boolean success = userDAOImpl.updatePasswordByUserId(userId, HashUtil.hash(newPwd));
            if (success) {
                logger.info("Password changed successfully | userId={}", userId);
            } else {
                logger.error("Password change failed | userId={}", userId);
            }
            return success;

        } catch (SQLException e) {
            logger.error("Change password failed due to SQL error | userId={}", userId, e);
            return false;
        }
    }

    // ---------- FORGOT PASSWORD ----------
    @Override
    public String getSecurityQuestionByEmail(String email) {
        logger.info("Fetching security question | email={}", email);

        try {
            String question = userDAOImpl.fetchSecurityQuestion(email);
            if (question != null) {
                logger.info("Security question fetched successfully | email={}", email);
            } else {
                logger.warn("No security question found | email={}", email);
            }
            return question;

        } catch (SQLException e) {
            logger.error("Failed to fetch security question | email={}", email, e);
            return null;
        }
    }

    @Override
    public void resetPassword(String email, String answer, String newPassword) {
        logger.info("Reset password request | email={}", email);

        try {
            String dbHash = userDAOImpl.fetchSecurityAnswerHash(email);
            if (dbHash == null) {
                logger.warn("Reset password failed: no security answer found | email={}", email);
                return;
            }

            if (!dbHash.equals(HashUtil.hash(answer.trim().toLowerCase()))) {
                logger.warn("Reset password failed: incorrect security answer | email={}", email);
                return;
            }

            userDAOImpl.updatePasswordByEmail(email, HashUtil.hash(newPassword));
            logger.info("Password reset successfully | email={}", email);

        } catch (SQLException e) {
            logger.error("Failed to reset password | email={}", email, e);
            throw new RuntimeException("Password reset failed for email=" + email, e);
        }
    }

    // ---------- VERIFY SECURITY ANSWER ----------
    @Override
    public boolean verifySecurityAnswer(String email, String answer) {
        logger.info("Verifying security answer | email={}", email);

        try {
            String dbHash = userDAOImpl.fetchSecurityAnswerHash(email);
            if (dbHash == null) {
                logger.warn("Verification failed: no security answer found | email={}", email);
                return false;
            }

            boolean isCorrect = dbHash.equals(HashUtil.hash(answer.trim().toLowerCase()));
            logger.info("Security answer verification result | email={}, result={}", email, isCorrect);
            return isCorrect;

        } catch (SQLException e) {
            logger.error("Security answer verification failed | email={}", email, e);
            return false;
        }
    }

    @Override
    public int getUserIdBySeekerId(int seekerId) {

        logger.debug("Service call: getUserIdBySeekerId | seekerId={}", seekerId);

        int userId = UserDAOImpl.getUserIdBySeekerId(seekerId);

        if (userId == -1) {
            logger.warn("No userId found for seekerId={}", seekerId);
        }

        return userId;
    }
}
