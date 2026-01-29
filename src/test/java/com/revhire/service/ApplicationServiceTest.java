package com.revhire.service;

import com.revhire.dao.ApplicationsDAO;
import com.revhire.model.Application;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ApplicationServiceTest {

    private ApplicationsDAO applicationsDAO;
    private ApplicationService applicationService;

    @BeforeEach
    void setup() {
        applicationsDAO = Mockito.mock(ApplicationsDAO.class);
        applicationService = new ApplicationService(applicationsDAO);
    }

    // ---------------- applyForJob ----------------

    @Test
    void applyForJob_shouldApplyWhenNotAlreadyApplied() {
        // Arrange
        int jobId = 101;
        int seekerId = 201;

        when(applicationsDAO.hasAlreadyApplied(jobId, seekerId))
                .thenReturn(false);

        // Act
        applicationService.applyForJob(jobId, seekerId);

        // Assert
        verify(applicationsDAO).applyJob(jobId, seekerId);
    }

    @Test
    void applyForJob_shouldNotApplyWhenAlreadyApplied() {
        // Arrange
        int jobId = 101;
        int seekerId = 201;

        when(applicationsDAO.hasAlreadyApplied(jobId, seekerId))
                .thenReturn(true);

        // Act
        applicationService.applyForJob(jobId, seekerId);

        // Assert
        verify(applicationsDAO, never()).applyJob(anyInt(), anyInt());
    }

    // ---------------- withdrawApplication ----------------

    @Test
    void withdrawApplication_shouldUpdateStatus() {
        // Act
        applicationService.withdrawApplication(1, "WITHDRAWN", "Not interested");

        // Assert
        verify(applicationsDAO)
                .updateStatus(1, "WITHDRAWN", "Not interested");
    }

    // ---------------- viewMyApplications ----------------

    @Test
    void viewMyApplications_shouldReturnApplications() {
        // Arrange
        int seekerId = 201;
        List<String> applications = List.of("Job A", "Job B");

        when(applicationsDAO.getApplicationsBySeeker(seekerId))
                .thenReturn(applications);

        // Act
        List<String> result = applicationService.viewMyApplications(seekerId);

        // Assert
        assertEquals(2, result.size());
        assertEquals(applications, result);
    }

    // ---------------- getApplicantsForJob ----------------

    @Test
    void getApplicantsForJob_shouldReturnApplicants() throws Exception {
        // Arrange
        int jobId = 101;
        List<Application> list = List.of(mock(Application.class));

        when(applicationsDAO.fetchApplicationsByJobId(jobId))
                .thenReturn(list);

        // Act
        List<Application> result = applicationService.getApplicantsForJob(jobId);

        // Assert
        assertEquals(1, result.size());
    }

    @Test
    void getApplicantsForJob_shouldReturnEmptyListOnException() throws Exception {
        // Arrange
        when(applicationsDAO.fetchApplicationsByJobId(anyInt()))
                .thenThrow(new RuntimeException("DB error"));

        // Act
        List<Application> result = applicationService.getApplicantsForJob(101);

        // Assert
        assertTrue(result.isEmpty());
    }

    // ---------------- updateApplicationStatus ----------------

    @Test
    void updateApplicationStatus_shouldUpdateStatus() throws SQLException {
        // Act
        applicationService.updateApplicationStatus(1, "APPROVED");

        // Assert
        verify(applicationsDAO)
                .updateStatusByApplicationId(1, "APPROVED");
    }

    @Test
    void updateApplicationStatus_shouldHandleExceptionGracefully() throws SQLException {
        // Arrange
        doThrow(new RuntimeException("DB error"))
                .when(applicationsDAO)
                .updateStatusByApplicationId(anyInt(), anyString());

        // Act (should NOT throw)
        assertDoesNotThrow(() ->
                applicationService.updateApplicationStatus(1, "REJECTED"));
    }

    // ---------------- getSeekerUserIdByApplicationId ----------------

    @Test
    void getSeekerUserIdByApplicationId_shouldReturnUserId() throws SQLException {
        when(applicationsDAO.fetchSeekerUserIdByApplicationId(1))
                .thenReturn(5001);

        int result =
                applicationService.getSeekerUserIdByApplicationId(1);

        assertEquals(5001, result);
    }

    @Test
    void getSeekerUserIdByApplicationId_shouldReturnMinusOneOnException() throws SQLException {
        when(applicationsDAO.fetchSeekerUserIdByApplicationId(anyInt()))
                .thenThrow(new RuntimeException());

        int result =
                applicationService.getSeekerUserIdByApplicationId(1);

        assertEquals(-1, result);
    }

    // ---------------- getJobIdByApplicationId ----------------

    @Test
    void getJobIdByApplicationId_shouldReturnJobId() throws SQLException {
        when(applicationsDAO.fetchJobIdByApplicationId(1))
                .thenReturn(3001);

        int result =
                applicationService.getJobIdByApplicationId(1);

        assertEquals(3001, result);
    }

    @Test
    void getJobIdByApplicationId_shouldReturnMinusOneOnException() throws SQLException {
        when(applicationsDAO.fetchJobIdByApplicationId(anyInt()))
                .thenThrow(new RuntimeException());

        int result =
                applicationService.getJobIdByApplicationId(1);

        assertEquals(-1, result);
    }
}
