package com.revhire.service;

import com.revhire.dao.impl.UserDAOImpl;
import com.revhire.model.User;
import com.revhire.service.impl.UserServiceImpl;
import com.revhire.util.HashUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserDAOImpl userDAOImpl;

    @InjectMocks
    private UserServiceImpl userServiceImpl;

    // ---------------- addUserAndReturnId ----------------

    @Test
    void addUserAndReturnId_shouldCallDAO() {
        // given
        given(userDAOImpl.insertUserAndReturnId(
                anyString(),
                anyString(),
                anyString(),
                anyString(),
                anyString()
        )).willReturn(100);

        // when
        int userId = userServiceImpl.addUserAndReturnId(
                "test@example.com",
                "passHash",
                "USER",
                "Q?",
                "AHash"
        );

        // then
        assertEquals(100, userId);
        then(userDAOImpl).should().insertUserAndReturnId(
                "test@example.com",
                "passHash",
                "USER",
                "Q?",
                "AHash"
        );
    }

    // ---------------- login ----------------

    @Test
    void login_shouldReturnUser_whenCredentialsValid() throws SQLException {
        // given
        ResultSet rs = mock(ResultSet.class);

        given(userDAOImpl.fetchLoginData("test@example.com"))
                .willReturn(rs);
        given(rs.next()).willReturn(true);
        given(rs.getString("password_hash"))
                .willReturn(HashUtil.hash("password"));
        given(rs.getInt("user_id")).willReturn(1);
        given(rs.getString("role")).willReturn("USER");

        // when
        User user = userServiceImpl.login("test@example.com", "password");

        // then
        assertNotNull(user);
        assertEquals(1, user.getUserId());
        assertEquals("USER", user.getRole());
    }

    @Test
    void login_shouldReturnNull_whenUserNotFound() throws SQLException {
        // given
        ResultSet rs = mock(ResultSet.class);

        given(userDAOImpl.fetchLoginData("test@example.com"))
                .willReturn(rs);
        given(rs.next()).willReturn(false);

        // when
        User user = userServiceImpl.login("test@example.com", "password");

        // then
        assertNull(user);
    }

    // ---------------- verifySecurityAnswer ----------------

    @Test
    void verifySecurityAnswer_shouldReturnTrue_whenAnswerMatches() throws SQLException {
        // given
        given(userDAOImpl.fetchSecurityAnswerHash("test@example.com"))
                .willReturn(HashUtil.hash("answer"));

        // when
        boolean result =
                userServiceImpl.verifySecurityAnswer("test@example.com", "answer");

        // then
        assertTrue(result);
    }

    @Test
    void verifySecurityAnswer_shouldReturnFalse_whenAnswerIncorrect() throws SQLException {
        // given
        given(userDAOImpl.fetchSecurityAnswerHash("test@example.com"))
                .willReturn(HashUtil.hash("correct"));

        // when
        boolean result =
                userServiceImpl.verifySecurityAnswer("test@example.com", "wrong");

        // then
        assertFalse(result);
    }
}
