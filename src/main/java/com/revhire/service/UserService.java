package com.revhire.service;

import com.revhire.model.User;

public interface UserService {

    // ---------- REGISTER ----------
    int addUserAndReturnId(
            String email,
            String passwordHash,
            String role,
            String securityQuestion,
            String securityAnswerHash
    );

    // ---------- LOGIN ----------
    User login(String email, String password);

    // ---------- CHANGE PASSWORD ----------
    boolean changePassword(int userId, String currentPwd, String newPwd);

    // ---------- FORGOT PASSWORD ----------
    String getSecurityQuestionByEmail(String email);

    // ---------- RESET PASSWORD ----------
    void resetPassword(String email, String answer, String newPassword);

    // ---------- VERIFY SECURITY ANSWERS  ----------
    boolean verifySecurityAnswer(String email, String answer);

}
