package com.revhire.service;

import com.revhire.dao.JobSeekersDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class JobSeekerServiceTest {

    private JobSeekersDAO jobSeekersDAO;
    private JobSeekerService jobSeekerService;

    @BeforeEach
    void setup() {
        jobSeekersDAO = mock(JobSeekersDAO.class);
        jobSeekerService = new JobSeekerService(jobSeekersDAO);
    }

    // ---------------- createProfile ----------------

    @Test
    void createProfile_shouldCallInsert() throws SQLException {
        doNothing().when(jobSeekersDAO)
                .insertJobSeeker(1, "John Doe", "1234567890", "Mumbai", 3, 'Y');

        jobSeekerService.createProfile(1, "John Doe", "1234567890", "Mumbai", 3);

        verify(jobSeekersDAO).insertJobSeeker(1, "John Doe", "1234567890", "Mumbai", 3, 'Y');
    }

    @Test
    void createProfile_shouldThrowRuntimeExceptionOnSQLException() throws SQLException {
        doThrow(new SQLException("DB Error")).when(jobSeekersDAO)
                .insertJobSeeker(anyInt(), any(), any(), any(), anyInt(), anyChar());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> jobSeekerService.createProfile(1, "John", "123", "Mumbai", 2));

        assertEquals("Failed to create job seeker profile", ex.getMessage());
    }

    // ---------------- updateJobSeekerProfile ----------------

    @Test
    void updateJobSeekerProfile_shouldCallUpdate() throws SQLException {
        when(jobSeekersDAO.updateJobSeeker(anyInt(), any(), any(), any(), any()))
                .thenReturn(1);

        jobSeekerService.updateJobSeekerProfile(10, "John Doe", "12345", "Delhi", 4);

        verify(jobSeekersDAO).updateJobSeeker(10, "John Doe", "12345", "Delhi", 4);
    }

    @Test
    void updateJobSeekerProfile_shouldHandleZeroRows() throws SQLException {
        when(jobSeekersDAO.updateJobSeeker(anyInt(), any(), any(), any(), any()))
                .thenReturn(0);

        assertDoesNotThrow(() ->
                jobSeekerService.updateJobSeekerProfile(10, "John", "123", "Delhi", 3));
    }

    @Test
    void updateJobSeekerProfile_shouldThrowRuntimeExceptionOnSQLException() throws SQLException {
        when(jobSeekersDAO.updateJobSeeker(anyInt(), any(), any(), any(), any()))
                .thenThrow(new SQLException());

        assertThrows(RuntimeException.class,
                () -> jobSeekerService.updateJobSeekerProfile(10, "John", "123", "Delhi", 3));
    }

    // ---------------- getSeekerIdByUserId ----------------

    @Test
    void getSeekerIdByUserId_shouldReturnSeekerId() throws SQLException {
        when(jobSeekersDAO.findSeekerIdByUserId(5)).thenReturn(1001);

        int result = jobSeekerService.getSeekerIdByUserId(5);

        assertEquals(1001, result);
    }

    @Test
    void getSeekerIdByUserId_shouldThrowWhenNotFound() throws SQLException {
        when(jobSeekersDAO.findSeekerIdByUserId(anyInt())).thenReturn(-1);

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> jobSeekerService.getSeekerIdByUserId(5));

        assertEquals("Job seeker profile not found", ex.getMessage());
    }

    @Test
    void getSeekerIdByUserId_shouldThrowOnSQLException() throws SQLException {
        when(jobSeekersDAO.findSeekerIdByUserId(anyInt())).thenThrow(new SQLException());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> jobSeekerService.getSeekerIdByUserId(5));

        assertEquals("Error fetching seeker ID", ex.getMessage());
    }

    // ---------------- createJobSeeker ----------------

    @Test
    void createJobSeeker_shouldCallCreateProfileWithDefaults() throws SQLException {
        doNothing().when(jobSeekersDAO).insertJobSeeker(anyInt(), isNull(), isNull(), isNull(), eq(0), eq('Y'));

        jobSeekerService.createJobSeeker(5);

        verify(jobSeekersDAO).insertJobSeeker(5, null, null, null, 0, 'Y');
    }
}
