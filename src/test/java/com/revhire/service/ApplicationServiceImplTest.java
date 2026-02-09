package com.revhire.service;

import com.revhire.dao.impl.ApplicationsDAOImpl;
import com.revhire.model.Application;
import com.revhire.service.impl.ApplicationServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ApplicationServiceImplTest {

    @Mock
    private ApplicationsDAOImpl applicationsDAOImpl;

    @InjectMocks
    private ApplicationServiceImpl applicationService;

    // ---------------- applyForJob ----------------

    @Test
    void applyForJob_shouldApplyWhenNotAlreadyApplied() {
        when(applicationsDAOImpl.hasAlreadyApplied(101, 201))
                .thenReturn(false);

        boolean result = applicationService.applyForJob(101, 201);

        assertTrue(result);
        verify(applicationsDAOImpl).applyJob(101, 201);
    }

    @Test
    void applyForJob_shouldNotApplyWhenAlreadyApplied() {
        when(applicationsDAOImpl.hasAlreadyApplied(101, 201))
                .thenReturn(true);

        boolean result = applicationService.applyForJob(101, 201);

        assertFalse(result);
        verify(applicationsDAOImpl, never())
                .applyJob(anyInt(), anyInt());
    }

    // ---------------- withdrawApplication ----------------

    @Test
    void withdrawApplication_shouldReturnTrueWhenUpdated() {
        when(applicationsDAOImpl.updateStatus(eq(1), eq("WITHDRAWN"), any()))
                .thenReturn(true);

        boolean result =
                applicationService.withdrawApplication(1, "WITHDRAWN", "Reason");

        assertTrue(result);
        verify(applicationsDAOImpl)
                .updateStatus(eq(1), eq("WITHDRAWN"), any());
    }

    @Test
    void withdrawApplication_shouldReturnFalseWhenNotUpdated() {
        when(applicationsDAOImpl.updateStatus(eq(99), eq("WITHDRAWN"), isNull()))
                .thenReturn(false);

        boolean result =
                applicationService.withdrawApplication(99, "WITHDRAWN", null);

        assertFalse(result);
    }

    // ---------------- viewMyApplications ----------------

    @Test
    void viewMyApplications_shouldReturnApplications() {
        Application app = mock(Application.class);

        when(applicationsDAOImpl.getApplicationsBySeeker(201))
                .thenReturn(List.of(app));

        List<Application> result =
                applicationService.viewMyApplications(201);

        assertEquals(1, result.size());
        verify(applicationsDAOImpl)
                .getApplicationsBySeeker(201);
    }

    // ---------------- getApplicantsForJob ----------------

    @Test
    void getApplicantsForJob_shouldReturnList() throws Exception {
        when(applicationsDAOImpl.fetchApplicationsByJobId(101))
                .thenReturn(List.of(mock(Application.class)));

        List<Application> result =
                applicationService.getApplicantsForJob(101);

        assertEquals(1, result.size());
    }

    @Test
    void getApplicantsForJob_shouldReturnEmptyListOnException() throws Exception {
        when(applicationsDAOImpl.fetchApplicationsByJobId(anyInt()))
                .thenThrow(new RuntimeException("DB error"));

        List<Application> result =
                applicationService.getApplicantsForJob(101);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    // ---------------- updateApplicationStatus ----------------

    @Test
    void updateApplicationStatus_shouldCallDAO() throws Exception {
        when(applicationsDAOImpl.updateStatusByApplicationId(1, "APPROVED"))
                .thenReturn(true);

        assertDoesNotThrow(() ->
                applicationService.updateApplicationStatus(1, "APPROVED"));

        verify(applicationsDAOImpl)
                .updateStatusByApplicationId(1, "APPROVED");
    }

    @Test
    void updateApplicationStatus_shouldHandleException() throws Exception {
        when(applicationsDAOImpl.updateStatusByApplicationId(anyInt(), anyString()))
                .thenThrow(new RuntimeException("DB error"));

        assertDoesNotThrow(() ->
                applicationService.updateApplicationStatus(1, "REJECTED"));
    }

    // ---------------- getSeekerIdByApplicationId ----------------

    @Test
    void getSeekerIdByApplicationId_shouldReturnSeekerId() throws Exception {
        when(applicationsDAOImpl.fetchSeekerIdByApplicationId(1))
                .thenReturn(501);

        int result =
                applicationService.getSeekerIdByApplicationId(1);

        assertEquals(501, result);
    }

    @Test
    void getSeekerIdByApplicationId_shouldReturnMinusOneOnException() throws Exception {
        when(applicationsDAOImpl.fetchSeekerIdByApplicationId(anyInt()))
                .thenThrow(new RuntimeException());

        int result =
                applicationService.getSeekerIdByApplicationId(1);

        assertEquals(-1, result);
    }

    // ---------------- getJobIdByApplicationId ----------------

    @Test
    void getJobIdByApplicationId_shouldReturnJobId() throws Exception {
        when(applicationsDAOImpl.fetchJobIdByApplicationId(1))
                .thenReturn(3001);

        int result =
                applicationService.getJobIdByApplicationId(1);

        assertEquals(3001, result);
    }

    @Test
    void getJobIdByApplicationId_shouldReturnMinusOneOnException() throws Exception {
        when(applicationsDAOImpl.fetchJobIdByApplicationId(anyInt()))
                .thenThrow(new RuntimeException());

        int result =
                applicationService.getJobIdByApplicationId(1);

        assertEquals(-1, result);
    }
}
