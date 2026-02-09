package com.revhire.service;

import com.revhire.dao.impl.UserDAOImpl;
import com.revhire.model.User;
import com.revhire.service.impl.UserServiceImpl;
import com.revhire.util.HashUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.mockStatic;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserDAOImpl userDAOImpl;

    @InjectMocks
    private UserServiceImpl userServiceImpl;

    // ---------- REGISTER ----------

    @Test
    void addUserAndReturnId_shouldReturnUserId() {

        given(userDAOImpl.insertUserAndReturnId(
                anyString(), anyString(), anyString(), anyString(), anyString()
        )).willReturn(10);

        int id = userServiceImpl.addUserAndReturnId(
                "a@test.com",
                "hash",
                "USER",
                "Q",
                "A"
        );

        assertEquals(10, id);
    }

    // ---------- LOGIN ----------

    @Test
    void login_shouldReturnUser_whenValidCredentials() throws SQLException {

        ResultSet rs = mock(ResultSet.class);

        given(userDAOImpl.fetchLoginData("a@test.com")).willReturn(rs);
        given(rs.next()).willReturn(true);
        given(rs.getString("password_hash"))
                .willReturn(HashUtil.hash("pass"));
        given(rs.getInt("user_id")).willReturn(1);
        given(rs.getString("role")).willReturn("USER");

        User user = userServiceImpl.login("a@test.com", "pass");

        assertNotNull(user);
        assertEquals(1, user.getUserId());
        assertEquals("USER", user.getRole());
    }

    @Test
    void login_shouldReturnNull_whenPasswordWrong() throws SQLException {

        ResultSet rs = mock(ResultSet.class);

        given(userDAOImpl.fetchLoginData("a@test.com")).willReturn(rs);
        given(rs.next()).willReturn(true);
        given(rs.getString("password_hash"))
                .willReturn(HashUtil.hash("correct"));

        assertNull(userServiceImpl.login("a@test.com", "wrong"));
    }

    @Test
    void login_shouldReturnNull_whenUserNotFound() throws SQLException {

        ResultSet rs = mock(ResultSet.class);

        given(userDAOImpl.fetchLoginData("a@test.com")).willReturn(rs);
        given(rs.next()).willReturn(false);

        assertNull(userServiceImpl.login("a@test.com", "pass"));
    }

    // ---------- CHANGE PASSWORD ----------

    @Test
    void changePassword_shouldReturnTrue_whenSuccess() throws SQLException {

        ResultSet rs = mock(ResultSet.class);

        given(userDAOImpl.fetchLoginDataById(1)).willReturn(rs);
        given(rs.next()).willReturn(true);
        given(rs.getString("password_hash"))
                .willReturn(HashUtil.hash("old"));

        given(userDAOImpl.updatePasswordByUserId(eq(1), anyString()))
                .willReturn(true);

        assertTrue(userServiceImpl.changePassword(1, "old", "new"));
    }

    @Test
    void changePassword_shouldReturnFalse_whenWrongCurrentPassword()
            throws SQLException {

        ResultSet rs = mock(ResultSet.class);

        given(userDAOImpl.fetchLoginDataById(1)).willReturn(rs);
        given(rs.next()).willReturn(true);
        given(rs.getString("password_hash"))
                .willReturn(HashUtil.hash("correct"));

        assertFalse(userServiceImpl.changePassword(1, "wrong", "new"));
    }

    // ---------- FORGOT PASSWORD ----------

    @Test
    void getSecurityQuestionByEmail_shouldReturnQuestion()
            throws SQLException {

        given(userDAOImpl.fetchSecurityQuestion("a@test.com"))
                .willReturn("Your pet name?");

        String question =
                userServiceImpl.getSecurityQuestionByEmail("a@test.com");

        assertEquals("Your pet name?", question);
    }

    @Test
    void getSecurityQuestionByEmail_shouldReturnNull_onSQLException()
            throws SQLException {

        given(userDAOImpl.fetchSecurityQuestion("a@test.com"))
                .willThrow(SQLException.class);

        assertNull(userServiceImpl.getSecurityQuestionByEmail("a@test.com"));
    }

    // ---------- RESET PASSWORD ----------

    @Test
    void resetPassword_shouldUpdate_whenAnswerCorrect()
            throws SQLException {

        given(userDAOImpl.fetchSecurityAnswerHash("a@test.com"))
                .willReturn(HashUtil.hash("ans"));

        given(userDAOImpl.updatePasswordByEmail(anyString(), anyString()))
                .willReturn(true);

        userServiceImpl.resetPassword("a@test.com", "ans", "newPass");

        then(userDAOImpl).should()
                .updatePasswordByEmail(eq("a@test.com"), anyString());
    }

    @Test
    void resetPassword_shouldThrowException_onSQLException()
            throws SQLException {

        given(userDAOImpl.fetchSecurityAnswerHash("a@test.com"))
                .willThrow(SQLException.class);

        assertThrows(
                RuntimeException.class,
                () -> userServiceImpl.resetPassword(
                        "a@test.com", "ans", "new"
                )
        );
    }

    // ---------- VERIFY SECURITY ANSWER ----------

    @Test
    void verifySecurityAnswer_shouldReturnTrue_whenMatch()
            throws SQLException {

        given(userDAOImpl.fetchSecurityAnswerHash("a@test.com"))
                .willReturn(HashUtil.hash("yes"));

        assertTrue(
                userServiceImpl.verifySecurityAnswer("a@test.com", "yes")
        );
    }

    @Test
    void verifySecurityAnswer_shouldReturnFalse_whenMismatch()
            throws SQLException {

        given(userDAOImpl.fetchSecurityAnswerHash("a@test.com"))
                .willReturn(HashUtil.hash("correct"));

        assertFalse(
                userServiceImpl.verifySecurityAnswer("a@test.com", "wrong")
        );
    }

    // ---------- getUserIdBySeekerId (STATIC DAO METHOD) ----------

    @Test
    void getUserIdBySeekerId_shouldReturnUserId() {

        try (MockedStatic<UserDAOImpl> mocked =
                     mockStatic(UserDAOImpl.class)) {

            mocked.when(() -> UserDAOImpl.getUserIdBySeekerId(5))
                    .thenReturn(101);

            int userId = userServiceImpl.getUserIdBySeekerId(5);

            assertEquals(101, userId);
        }
    }

    @Test
    void getUserIdBySeekerId_shouldReturnMinusOne_whenNotFound() {

        try (MockedStatic<UserDAOImpl> mocked =
                     mockStatic(UserDAOImpl.class)) {

            mocked.when(() -> UserDAOImpl.getUserIdBySeekerId(99))
                    .thenReturn(-1);

            int userId = userServiceImpl.getUserIdBySeekerId(99);

            assertEquals(-1, userId);
        }
    }
}
