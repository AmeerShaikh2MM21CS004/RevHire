package com.revhire.service;

import com.revhire.dao.impl.JobSeekersDAOImpl;
import com.revhire.service.impl.JobSeekerServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class JobSeekerServiceImplTest {

    @Mock
    private JobSeekersDAOImpl jobSeekersDAOImpl;

    @InjectMocks
    private JobSeekerServiceImpl jobSeekerServiceImpl;

    // ---------------- createProfile ----------------

    @Test
    void createProfile_shouldCallInsert() throws SQLException {
        // given
        willDoNothing().given(jobSeekersDAOImpl)
                .insertJobSeeker(1, "John Doe", "1234567890", "Mumbai", 3, 'Y');

        // when
        jobSeekerServiceImpl.createProfile(1, "John Doe", "1234567890", "Mumbai", 3);

        // then
        then(jobSeekersDAOImpl).should()
                .insertJobSeeker(1, "John Doe", "1234567890", "Mumbai", 3, 'Y');
    }

    @Test
    void createProfile_shouldThrowRuntimeExceptionOnSQLException() throws SQLException {
        // given
        willThrow(new SQLException("DB Error"))
                .given(jobSeekersDAOImpl)
                .insertJobSeeker(anyInt(), any(), any(), any(), anyInt(), anyChar());

        // when + then
        RuntimeException ex = assertThrows(
                RuntimeException.class,
                () -> jobSeekerServiceImpl.createProfile(1, "John", "123", "Mumbai", 2)
        );

        assertEquals("Failed to create job seeker profile", ex.getMessage());
    }

    // ---------------- updateJobSeekerProfile ----------------

    @Test
    void updateJobSeekerProfile_shouldCallUpdate() throws SQLException {
        // given
        given(jobSeekersDAOImpl.updateJobSeeker(anyInt(), any(), any(), any(), any()))
                .willReturn(1);

        // when
        jobSeekerServiceImpl.updateJobSeekerProfile(10, "John Doe", "12345", "Delhi", 4);

        // then
        then(jobSeekersDAOImpl).should()
                .updateJobSeeker(10, "John Doe", "12345", "Delhi", 4);
    }

    @Test
    void updateJobSeekerProfile_shouldHandleZeroRows() throws SQLException {
        // given
        given(jobSeekersDAOImpl.updateJobSeeker(anyInt(), any(), any(), any(), any()))
                .willReturn(0);

        // then (should not throw)
        assertDoesNotThrow(() ->
                jobSeekerServiceImpl.updateJobSeekerProfile(10, "John", "123", "Delhi", 3)
        );
    }

    @Test
    void updateJobSeekerProfile_shouldThrowRuntimeExceptionOnSQLException() throws SQLException {
        // given
        willThrow(SQLException.class)
                .given(jobSeekersDAOImpl)
                .updateJobSeeker(anyInt(), any(), any(), any(), any());

        // then
        assertThrows(RuntimeException.class, () ->
                jobSeekerServiceImpl.updateJobSeekerProfile(10, "John", "123", "Delhi", 3)
        );
    }

    // ---------------- getSeekerIdByUserId ----------------

    @Test
    void getSeekerIdByUserId_shouldReturnSeekerId() throws SQLException {
        // given
        given(jobSeekersDAOImpl.findSeekerIdByUserId(5)).willReturn(1001);

        // when
        int result = jobSeekerServiceImpl.getSeekerIdByUserId(5);

        // then
        assertEquals(1001, result);
    }

    @Test
    void getSeekerIdByUserId_shouldThrowWhenNotFound() throws SQLException {
        // given
        given(jobSeekersDAOImpl.findSeekerIdByUserId(anyInt())).willReturn(-1);

        // when + then
        RuntimeException ex = assertThrows(
                RuntimeException.class,
                () -> jobSeekerServiceImpl.getSeekerIdByUserId(5)
        );

        assertEquals("Job seeker profile not found", ex.getMessage());
    }

    @Test
    void getSeekerIdByUserId_shouldThrowOnSQLException() throws SQLException {
        // given
        willThrow(SQLException.class)
                .given(jobSeekersDAOImpl)
                .findSeekerIdByUserId(anyInt());

        // when + then
        RuntimeException ex = assertThrows(
                RuntimeException.class,
                () -> jobSeekerServiceImpl.getSeekerIdByUserId(5)
        );

        assertEquals("Error fetching seeker ID", ex.getMessage());
    }

    // ---------------- createJobSeeker ----------------

    @Test
    void createJobSeeker_shouldCallCreateProfileWithDefaults() throws SQLException {
        // given
        willDoNothing().given(jobSeekersDAOImpl)
                .insertJobSeeker(anyInt(), isNull(), isNull(), isNull(), eq(0), eq('Y'));

        // when
        jobSeekerServiceImpl.createJobSeeker(5);

        // then
        then(jobSeekersDAOImpl).should()
                .insertJobSeeker(5, null, null, null, 0, 'Y');
    }
}
