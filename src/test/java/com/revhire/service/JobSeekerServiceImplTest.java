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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JobSeekerServiceImplTest {

    @Mock
    private JobSeekersDAOImpl jobSeekersDAOImpl;

    @InjectMocks
    private JobSeekerServiceImpl jobSeekerServiceImpl;

    // ---------------- createProfile ----------------

    @Test
    void createProfile_shouldCreateProfileSuccessfully() throws SQLException {

        doNothing().when(jobSeekersDAOImpl)
                .insertJobSeeker(
                        1,
                        "John Doe",
                        "1234567890",
                        "Mumbai",
                        3,
                        'Y'
                );

        assertDoesNotThrow(() ->
                jobSeekerServiceImpl.createProfile(
                        1,
                        "John Doe",
                        "1234567890",
                        "Mumbai",
                        3
                )
        );

        verify(jobSeekersDAOImpl).insertJobSeeker(
                1,
                "John Doe",
                "1234567890",
                "Mumbai",
                3,
                'Y'
        );
    }

    @Test
    void createProfile_shouldThrowRuntimeExceptionOnSQLException() throws SQLException {

        doThrow(SQLException.class).when(jobSeekersDAOImpl)
                .insertJobSeeker(
                        anyInt(),
                        anyString(),
                        anyString(),
                        anyString(),
                        anyInt(),
                        anyChar()
                );

        RuntimeException ex = assertThrows(
                RuntimeException.class,
                () -> jobSeekerServiceImpl.createProfile(
                        1,
                        "John",
                        "123",
                        "Mumbai",
                        2
                )
        );

        assertEquals("Failed to create job seeker profile", ex.getMessage());
    }

    // ---------------- updateJobSeekerProfile ----------------

    @Test
    void updateJobSeekerProfile_shouldLogSuccessWhenRowsUpdated() throws SQLException {

        when(jobSeekersDAOImpl.updateJobSeeker(
                anyInt(),
                anyString(),
                anyString(),
                anyString(),
                any()
        )).thenReturn(1);

        assertDoesNotThrow(() ->
                jobSeekerServiceImpl.updateJobSeekerProfile(
                        10,
                        "John Doe",
                        "12345",
                        "Delhi",
                        4
                )
        );

        verify(jobSeekersDAOImpl).updateJobSeeker(
                10,
                "John Doe",
                "12345",
                "Delhi",
                4
        );
    }

    @Test
    void updateJobSeekerProfile_shouldHandleZeroRowsUpdated() throws SQLException {

        when(jobSeekersDAOImpl.updateJobSeeker(
                anyInt(),
                anyString(),
                anyString(),
                anyString(),
                any()
        )).thenReturn(0);

        assertDoesNotThrow(() ->
                jobSeekerServiceImpl.updateJobSeekerProfile(
                        10,
                        "John",
                        "123",
                        "Delhi",
                        3
                )
        );
    }

    @Test
    void updateJobSeekerProfile_shouldThrowRuntimeExceptionOnSQLException() throws SQLException {

        doThrow(SQLException.class).when(jobSeekersDAOImpl)
                .updateJobSeeker(
                        anyInt(),
                        anyString(),
                        anyString(),
                        anyString(),
                        any()
                );

        RuntimeException ex = assertThrows(
                RuntimeException.class,
                () -> jobSeekerServiceImpl.updateJobSeekerProfile(
                        10,
                        "John",
                        "123",
                        "Delhi",
                        3
                )
        );

        assertEquals("Failed to update job seeker profile", ex.getMessage());
    }

    // ---------------- getSeekerIdByUserId ----------------

    @Test
    void getSeekerIdByUserId_shouldReturnSeekerId() throws SQLException {

        when(jobSeekersDAOImpl.findSeekerIdByUserId(5))
                .thenReturn(1001);

        int result = jobSeekerServiceImpl.getSeekerIdByUserId(5);

        assertEquals(1001, result);
    }

    @Test
    void getSeekerIdByUserId_shouldThrowWhenProfileNotFound() throws SQLException {

        when(jobSeekersDAOImpl.findSeekerIdByUserId(5))
                .thenReturn(-1);

        RuntimeException ex = assertThrows(
                RuntimeException.class,
                () -> jobSeekerServiceImpl.getSeekerIdByUserId(5)
        );

        assertEquals("Job seeker profile not found", ex.getMessage());
    }

    @Test
    void getSeekerIdByUserId_shouldThrowOnSQLException() throws SQLException {

        doThrow(SQLException.class).when(jobSeekersDAOImpl)
                .findSeekerIdByUserId(anyInt());

        RuntimeException ex = assertThrows(
                RuntimeException.class,
                () -> jobSeekerServiceImpl.getSeekerIdByUserId(5)
        );

        assertEquals("Error fetching seeker ID", ex.getMessage());
    }

    // ---------------- createJobSeeker ----------------

    @Test
    void createJobSeeker_shouldCreateDefaultProfile() throws SQLException {

        doNothing().when(jobSeekersDAOImpl)
                .insertJobSeeker(
                        anyInt(),
                        isNull(),
                        isNull(),
                        isNull(),
                        eq(0),
                        eq('Y')
                );

        jobSeekerServiceImpl.createJobSeeker(7);

        verify(jobSeekersDAOImpl).insertJobSeeker(
                7,
                null,
                null,
                null,
                0,
                'Y'
        );
    }
}
