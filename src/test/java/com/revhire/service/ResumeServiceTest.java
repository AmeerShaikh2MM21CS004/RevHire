package com.revhire.service;

import com.revhire.dao.ResumesDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class ResumeServiceTest {

    private ResumesDAO resumesDAO;
    private ResumeService resumeService;

    @BeforeEach
    void setup() {
        resumesDAO = mock(ResumesDAO.class);
        resumeService = new ResumeService(resumesDAO);
    }

    // ---------------- saveOrUpdateResume ----------------
    @Test
    void saveOrUpdateResume_shouldCallDAO() throws SQLException {
        // Arrange: nothing special, DAO mocked
        doNothing().when(resumesDAO).upsertResume(anyInt(), any(), any(), any(), any(), any());

        // Act
        resumeService.saveOrUpdateResume(1, "Obj", "Edu", "Exp", "Skills", "Projects");

        // Assert: verify DAO method was called
        verify(resumesDAO).upsertResume(1, "Obj", "Edu", "Exp", "Skills", "Projects");
    }

    @Test
    void saveOrUpdateResume_shouldThrowRuntimeExceptionOnSQLException() throws SQLException {
        // Arrange: throw exception from DAO
        doThrow(new SQLException()).when(resumesDAO).upsertResume(anyInt(), any(), any(), any(), any(), any());

        // Act & Assert: ensure RuntimeException is thrown
        assertThrows(RuntimeException.class, () ->
                resumeService.saveOrUpdateResume(1, "Obj", "Edu", "Exp", "Skills", "Projects")
        );
    }
}
