package com.revhire.service;

import com.revhire.dao.impl.JobSeekersDAOImpl;
import com.revhire.service.impl.JobSeekerServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class JobSeekerServiceImplTest {

    private JobSeekersDAOImpl jobSeekersDAOImpl;
    private JobSeekerServiceImpl jobSeekerServiceImpl;

    @BeforeEach
    void setup() {
        jobSeekersDAOImpl = mock(JobSeekersDAOImpl.class);
        jobSeekerServiceImpl = new JobSeekerServiceImpl(jobSeekersDAOImpl);
    }

    // ---------------- createProfile ----------------

    @Test
    void createProfile_shouldCallInsert() throws SQLException {
        doNothing().when(jobSeekersDAOImpl)
                .insertJobSeeker(1, "John Doe", "1234567890", "Mumbai", 3, 'Y');

        jobSeekerServiceImpl.createProfile(1, "John Doe", "1234567890", "Mumbai", 3);

        verify(jobSeekersDAOImpl).insertJobSeeker(1, "John Doe", "1234567890", "Mumbai", 3, 'Y');
    }

    @Test
    void createProfile_shouldThrowRuntimeExceptionOnSQLException() throws SQLException {
        doThrow(new SQLException("DB Error")).when(jobSeekersDAOImpl)
                .insertJobSeeker(anyInt(), any(), any(), any(), anyInt(), anyChar());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> jobSeekerServiceImpl.createProfile(1, "John", "123", "Mumbai", 2));

        assertEquals("Failed to create job seeker profile", ex.getMessage());
    }

    // ---------------- updateJobSeekerProfile ----------------

    @Test
    void updateJobSeekerProfile_shouldCallUpdate() throws SQLException {
        when(jobSeekersDAOImpl.updateJobSeeker(anyInt(), any(), any(), any(), any()))
                .thenReturn(1);

        jobSeekerServiceImpl.updateJobSeekerProfile(10, "John Doe", "12345", "Delhi", 4);

        verify(jobSeekersDAOImpl).updateJobSeeker(10, "John Doe", "12345", "Delhi", 4);
    }

    @Test
    void updateJobSeekerProfile_shouldHandleZeroRows() throws SQLException {
        when(jobSeekersDAOImpl.updateJobSeeker(anyInt(), any(), any(), any(), any()))
                .thenReturn(0);

        assertDoesNotThrow(() ->
                jobSeekerServiceImpl.updateJobSeekerProfile(10, "John", "123", "Delhi", 3));
    }

    @Test
    void updateJobSeekerProfile_shouldThrowRuntimeExceptionOnSQLException() throws SQLException {
        when(jobSeekersDAOImpl.updateJobSeeker(anyInt(), any(), any(), any(), any()))
                .thenThrow(new SQLException());

        assertThrows(RuntimeException.class,
                () -> jobSeekerServiceImpl.updateJobSeekerProfile(10, "John", "123", "Delhi", 3));
    }

    // ---------------- getSeekerIdByUserId ----------------

    @Test
    void getSeekerIdByUserId_shouldReturnSeekerId() throws SQLException {
        when(jobSeekersDAOImpl.findSeekerIdByUserId(5)).thenReturn(1001);

        int result = jobSeekerServiceImpl.getSeekerIdByUserId(5);

        assertEquals(1001, result);
    }

    @Test
    void getSeekerIdByUserId_shouldThrowWhenNotFound() throws SQLException {
        when(jobSeekersDAOImpl.findSeekerIdByUserId(anyInt())).thenReturn(-1);

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> jobSeekerServiceImpl.getSeekerIdByUserId(5));

        assertEquals("Job seeker profile not found", ex.getMessage());
    }

    @Test
    void getSeekerIdByUserId_shouldThrowOnSQLException() throws SQLException {
        when(jobSeekersDAOImpl.findSeekerIdByUserId(anyInt())).thenThrow(new SQLException());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> jobSeekerServiceImpl.getSeekerIdByUserId(5));

        assertEquals("Error fetching seeker ID", ex.getMessage());
    }

    // ---------------- createJobSeeker ----------------

    @Test
    void createJobSeeker_shouldCallCreateProfileWithDefaults() throws SQLException {
        doNothing().when(jobSeekersDAOImpl).insertJobSeeker(anyInt(), isNull(), isNull(), isNull(), eq(0), eq('Y'));

        jobSeekerServiceImpl.createJobSeeker(5);

        verify(jobSeekersDAOImpl).insertJobSeeker(5, null, null, null, 0, 'Y');
    }
}
