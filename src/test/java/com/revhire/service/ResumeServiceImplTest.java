package com.revhire.service;

import com.revhire.dao.impl.ResumesDAOImpl;
import com.revhire.service.impl.ResumeServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class ResumeServiceImplTest {

    @Mock
    private ResumesDAOImpl resumesDAOImpl;

    @InjectMocks
    private ResumeServiceImpl resumeServiceImpl;

    // ---------------- saveOrUpdateResume ----------------

    @Test
    void saveOrUpdateResume_shouldCallDAO() throws SQLException {
        // given
        willDoNothing()
                .given(resumesDAOImpl)
                .upsertResume(anyInt(), any(), any(), any(), any(), any());

        // when
        resumeServiceImpl.saveOrUpdateResume(
                1,
                "Obj",
                "Edu",
                "Exp",
                "Skills",
                "Projects"
        );

        // then
        then(resumesDAOImpl).should().upsertResume(
                1,
                "Obj",
                "Edu",
                "Exp",
                "Skills",
                "Projects"
        );
    }

    @Test
    void saveOrUpdateResume_shouldThrowRuntimeExceptionOnSQLException() throws SQLException {
        // given
        willThrow(SQLException.class)
                .given(resumesDAOImpl)
                .upsertResume(anyInt(), any(), any(), any(), any(), any());

        // when + then
        assertThrows(
                RuntimeException.class,
                () -> resumeServiceImpl.saveOrUpdateResume(
                        1,
                        "Obj",
                        "Edu",
                        "Exp",
                        "Skills",
                        "Projects"
                )
        );
    }
}
