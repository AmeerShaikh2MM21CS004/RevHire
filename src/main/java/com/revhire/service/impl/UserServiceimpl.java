package com.revhire.service.impl;

import com.revhire.model.User;

public interface  UserServiceimpl {

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

    void resetPassword(String email, String answer, String newPassword);

    boolean verifySecurityAnswer(String email, String answer);

}
