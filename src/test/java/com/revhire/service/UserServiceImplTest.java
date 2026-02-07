package com.revhire.service;

import com.revhire.dao.impl.UserDAOImpl;
import com.revhire.model.User;
import com.revhire.service.impl.UserServiceImpl;
import com.revhire.util.HashUtil;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceImplTest {

    private UserDAOImpl userDAOImpl;
    private UserServiceImpl userServiceImpl;

    @BeforeEach
    void setup() {
        userDAOImpl = mock(UserDAOImpl.class);
        userServiceImpl = new UserServiceImpl(userDAOImpl);
    }

    // ---------------- addUserAndReturnId ----------------
    @Test
    void addUserAndReturnId_shouldCallDAO() {
        when(userDAOImpl.insertUserAndReturnId(anyString(), anyString(), anyString(), anyString(), anyString()))
                .thenReturn(100);

        int userId = userServiceImpl.addUserAndReturnId("test@example.com", "passHash", "USER", "Q?", "AHash");
        assertEquals(100, userId);

        verify(userDAOImpl).insertUserAndReturnId("test@example.com", "passHash", "USER", "Q?", "AHash");
    }

    // ---------------- login ----------------
    @Test
    void login_shouldReturnUser_whenCredentialsValid() throws SQLException {
        ResultSet rs = mock(ResultSet.class);
        when(userDAOImpl.fetchLoginData("test@example.com")).thenReturn(rs);
        when(rs.next()).thenReturn(true);
        when(rs.getString("password_hash")).thenReturn(HashUtil.hash("password"));
        when(rs.getInt("user_id")).thenReturn(1);
        when(rs.getString("role")).thenReturn("USER");

        User user = userServiceImpl.login("test@example.com", "password");
        assertNotNull(user);
        assertEquals(1, user.getUserId());
    }

    @Test
    void login_shouldReturnNull_whenUserNotFound() throws SQLException {
        ResultSet rs = mock(ResultSet.class);
        when(userDAOImpl.fetchLoginData("test@example.com")).thenReturn(rs);
        when(rs.next()).thenReturn(false);

        assertNull(userServiceImpl.login("test@example.com", "password"));
    }

    // ---------------- verifySecurityAnswer ----------------
    @Test
    void verifySecurityAnswer_shouldReturnTrue_whenAnswerMatches() throws SQLException {
        when(userDAOImpl.fetchSecurityAnswerHash("test@example.com"))
                .thenReturn(HashUtil.hash("answer"));

        boolean result = userServiceImpl.verifySecurityAnswer("test@example.com", "answer");
        assertTrue(result);
    }

    @Test
    void verifySecurityAnswer_shouldReturnFalse_whenAnswerIncorrect() throws SQLException {
        when(userDAOImpl.fetchSecurityAnswerHash("test@example.com"))
                .thenReturn(HashUtil.hash("correct"));

        boolean result = userServiceImpl.verifySecurityAnswer("test@example.com", "wrong");
        assertFalse(result);
    }
}
