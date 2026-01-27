package com.revhire.service;

import com.revhire.dao.UserDAO;
import com.revhire.model.User;
import com.revhire.service.impl.UserServiceimpl;
import com.revhire.util.HashUtil;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserService implements UserServiceimpl {

    private final UserDAO userDAO = new UserDAO();

    // ---------- REGISTER ----------
    public int addUserAndReturnId(
            String email,
            String passwordHash,
            String role,
            String securityQuestion,
            String securityAnswerHash) {

        return userDAO.insertUserAndReturnId(
                email,
                passwordHash,
                role,
                securityQuestion,
                securityAnswerHash
        );
    }

    // ---------- LOGIN ----------
    public User login(String email, String password) {

        try (ResultSet rs = userDAO.fetchLoginData(email)) {

            if (!rs.next()) return null;

            String dbHash = rs.getString("password_hash");
            if (!dbHash.equals(HashUtil.hash(password))) return null;

            return new User(
                    rs.getInt("user_id"),
                    email,
                    rs.getString("role")
            );

        } catch (SQLException e) {
            return null;
        }
    }

    // ---------- CHANGE PASSWORD ----------
    public boolean changePassword(int userId, String currentPwd, String newPwd) {

        try (ResultSet rs = userDAO.fetchLoginDataById(userId)) {

            if (!rs.next()) return false;

            if (!rs.getString("password_hash")
                    .equals(HashUtil.hash(currentPwd)))
                return false;

            return userDAO.updatePasswordByUserId(
                    userId, HashUtil.hash(newPwd)
            );

        } catch (SQLException e) {
            return false;
        }
    }

    // ---------- FORGOT PASSWORD ----------
    public String getSecurityQuestionByEmail(String email) {

        try {
            return userDAO.fetchSecurityQuestion(email);
        } catch (SQLException e) {
            return null;
        }
    }

    public void resetPassword(
            String email,
            String answer,
            String newPassword
    ) {

        try {
            String dbHash = userDAO.fetchSecurityAnswerHash(email);
            if (dbHash == null) return;

            if (!dbHash.equals(HashUtil.hash(answer.trim().toLowerCase())))
                return;

            userDAO.updatePasswordByEmail(
                    email, HashUtil.hash(newPassword)
            );

        } catch (SQLException ignored) {
        }
    }

    // In UserService.java
    public boolean verifySecurityAnswer(String email, String answer) {
        try {
            String dbHash = userDAO.fetchSecurityAnswerHash(email);
            if (dbHash == null) return false;
            return dbHash.equals(HashUtil.hash(answer.trim().toLowerCase()));
        } catch (SQLException e) {
            return false;
        }
    }

}
