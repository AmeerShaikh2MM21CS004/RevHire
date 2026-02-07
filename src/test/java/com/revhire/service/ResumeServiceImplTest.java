package com.revhire.service;

import com.revhire.dao.impl.ResumesDAOImpl;
import com.revhire.service.impl.ResumeServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class ResumeServiceImplTest {

    private ResumesDAOImpl resumesDAOImpl;
    private ResumeServiceImpl resumeServiceImpl;

    @BeforeEach
    void setup() {
        resumesDAOImpl = mock(ResumesDAOImpl.class);
        resumeServiceImpl = new ResumeServiceImpl(resumesDAOImpl);
    }

    // ---------------- saveOrUpdateResume ----------------
    @Test
    void saveOrUpdateResume_shouldCallDAO() throws SQLException {
        // Arrange: nothing special, DAO mocked
        doNothing().when(resumesDAOImpl).upsertResume(anyInt(), any(), any(), any(), any(), any());

        // Act
        resumeServiceImpl.saveOrUpdateResume(1, "Obj", "Edu", "Exp", "Skills", "Projects");

        // Assert: verify DAO method was called
        verify(resumesDAOImpl).upsertResume(1, "Obj", "Edu", "Exp", "Skills", "Projects");
    }

    @Test
    void saveOrUpdateResume_shouldThrowRuntimeExceptionOnSQLException() throws SQLException {
        // Arrange: throw exception from DAO
        doThrow(new SQLException()).when(resumesDAOImpl).upsertResume(anyInt(), any(), any(), any(), any(), any());

        // Act & Assert: ensure RuntimeException is thrown
        assertThrows(RuntimeException.class, () ->
                resumeServiceImpl.saveOrUpdateResume(1, "Obj", "Edu", "Exp", "Skills", "Projects")
        );
    }
}
