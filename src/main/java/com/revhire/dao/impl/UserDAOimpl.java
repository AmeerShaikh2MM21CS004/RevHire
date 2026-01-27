package com.revhire.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface  UserDAOimpl {

    ResultSet fetchLoginData(String email) throws SQLException;

    ResultSet fetchLoginDataById(int userId) throws SQLException;

    boolean updatePasswordByUserId(int userId, String newHash) throws SQLException;

    boolean updatePasswordByEmail(String email, String newHash) throws SQLException;

    String fetchSecurityQuestion(String email) throws SQLException;

    String fetchSecurityAnswerHash(String email) throws SQLException;

    int insertUserAndReturnId(
            String email,
            String passwordHash,
            String role,
            String securityQuestion,
            String securityAnswerHash
    );

}
