package com.revhire.service;

import com.revhire.dao.impl.ApplicationsDAOImpl;
import com.revhire.model.Application;
import com.revhire.service.impl.ApplicationServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
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

        applicationService.applyForJob(101, 201);

        verify(applicationsDAOImpl)
                .applyJob(101, 201);
    }

    @Test
    void applyForJob_shouldNotApplyWhenAlreadyApplied() {
        when(applicationsDAOImpl.hasAlreadyApplied(101, 201))
                .thenReturn(true);

        applicationService.applyForJob(101, 201);

        verify(applicationsDAOImpl, never())
                .applyJob(anyInt(), anyInt());
    }

    // ---------------- withdrawApplication ----------------

    @Test
    void withdrawApplication_shouldUpdateStatus() {
        applicationService.withdrawApplication(
                1, "WITHDRAWN", "Not interested");

        verify(applicationsDAOImpl)
                .updateStatus(1, "WITHDRAWN", "Not interested");
    }

    // ---------------- viewMyApplications ----------------

    @Test
    void viewMyApplications_shouldReturnApplications() {
        when(applicationsDAOImpl.getApplicationsBySeeker(201))
                .thenReturn(List.of("App1", "App2"));

        List<String> result =
                applicationService.viewMyApplications(201);

        assertEquals(2, result.size());
    }

    // ---------------- getApplicantsForJob ----------------

    @Test
    void getApplicantsForJob_shouldReturnApplicants() throws Exception {
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

        assertTrue(result.isEmpty());
    }

    // ---------------- updateApplicationStatus ----------------

    @Test
    void updateApplicationStatus_shouldUpdateStatus() throws SQLException {
        assertDoesNotThrow(() ->
                applicationService.updateApplicationStatus(1, "APPROVED"));

        verify(applicationsDAOImpl)
                .updateStatusByApplicationId(1, "APPROVED");
    }

    @Test
    void updateApplicationStatus_shouldHandleExceptionGracefully() throws SQLException {
        doThrow(new RuntimeException("DB error"))
                .when(applicationsDAOImpl)
                .updateStatusByApplicationId(anyInt(), anyString());

        assertDoesNotThrow(() ->
                applicationService.updateApplicationStatus(1, "REJECTED"));
    }

    // ---------------- getSeekerUserIdByApplicationId ----------------

    @Test
    void getSeekerUserIdByApplicationId_shouldReturnUserId() throws Exception {
        when(applicationsDAOImpl.fetchSeekerUserIdByApplicationId(1))
                .thenReturn(5001);

        int result =
                applicationService.getSeekerUserIdByApplicationId(1);

        assertEquals(5001, result);
    }

    @Test
    void getSeekerUserIdByApplicationId_shouldReturnMinusOneOnException() throws Exception {
        when(applicationsDAOImpl.fetchSeekerUserIdByApplicationId(anyInt()))
                .thenThrow(new RuntimeException());

        int result =
                applicationService.getSeekerUserIdByApplicationId(1);

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
