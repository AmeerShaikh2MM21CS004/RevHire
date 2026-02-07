package com.revhire.service;

import com.revhire.dao.impl.ApplicationsDAOImpl;
import com.revhire.model.Application;
import com.revhire.service.impl.ApplicationServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ApplicationServiceImplTest {

    private ApplicationsDAOImpl applicationsDAOImpl;
    private ApplicationServiceImpl applicationServiceImpl;

    @BeforeEach
    void setup() {
        applicationsDAOImpl = Mockito.mock(ApplicationsDAOImpl.class);
        applicationServiceImpl = new ApplicationServiceImpl(applicationsDAOImpl);
    }

    // ---------------- applyForJob ----------------

    @Test
    void applyForJob_shouldApplyWhenNotAlreadyApplied() {
        // Arrange
        int jobId = 101;
        int seekerId = 201;

        when(applicationsDAOImpl.hasAlreadyApplied(jobId, seekerId))
                .thenReturn(false);

        // Act
        applicationServiceImpl.applyForJob(jobId, seekerId);

        // Assert
        verify(applicationsDAOImpl).applyJob(jobId, seekerId);
    }

    @Test
    void applyForJob_shouldNotApplyWhenAlreadyApplied() {
        // Arrange
        int jobId = 101;
        int seekerId = 201;

        when(applicationsDAOImpl.hasAlreadyApplied(jobId, seekerId))
                .thenReturn(true);

        // Act
        applicationServiceImpl.applyForJob(jobId, seekerId);

        // Assert
        verify(applicationsDAOImpl, never()).applyJob(anyInt(), anyInt());
    }

    // ---------------- withdrawApplication ----------------

    @Test
    void withdrawApplication_shouldUpdateStatus() {
        // Act
        applicationServiceImpl.withdrawApplication(1, "WITHDRAWN", "Not interested");

        // Assert
        verify(applicationsDAOImpl)
                .updateStatus(1, "WITHDRAWN", "Not interested");
    }

    // ---------------- viewMyApplications ----------------

    @Test
    void viewMyApplications_shouldReturnApplications() {
        // Arrange
        int seekerId = 201;
        List<String> applications = List.of("Job A", "Job B");

        when(applicationsDAOImpl.getApplicationsBySeeker(seekerId))
                .thenReturn(applications);

        // Act
        List<String> result = applicationServiceImpl.viewMyApplications(seekerId);

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

        when(applicationsDAOImpl.fetchApplicationsByJobId(jobId))
                .thenReturn(list);

        // Act
        List<Application> result = applicationServiceImpl.getApplicantsForJob(jobId);

        // Assert
        assertEquals(1, result.size());
    }

    @Test
    void getApplicantsForJob_shouldReturnEmptyListOnException() throws Exception {
        // Arrange
        when(applicationsDAOImpl.fetchApplicationsByJobId(anyInt()))
                .thenThrow(new RuntimeException("DB error"));

        // Act
        List<Application> result = applicationServiceImpl.getApplicantsForJob(101);

        // Assert
        assertTrue(result.isEmpty());
    }

    // ---------------- updateApplicationStatus ----------------

    @Test
    void updateApplicationStatus_shouldUpdateStatus() throws SQLException {
        // Act
        applicationServiceImpl.updateApplicationStatus(1, "APPROVED");

        // Assert
        verify(applicationsDAOImpl)
                .updateStatusByApplicationId(1, "APPROVED");
    }

    @Test
    void updateApplicationStatus_shouldHandleExceptionGracefully() throws SQLException {
        // Arrange
        doThrow(new RuntimeException("DB error"))
                .when(applicationsDAOImpl)
                .updateStatusByApplicationId(anyInt(), anyString());

        // Act (should NOT throw)
        assertDoesNotThrow(() ->
                applicationServiceImpl.updateApplicationStatus(1, "REJECTED"));
    }

    // ---------------- getSeekerUserIdByApplicationId ----------------

    @Test
    void getSeekerUserIdByApplicationId_shouldReturnUserId() throws SQLException {
        when(applicationsDAOImpl.fetchSeekerUserIdByApplicationId(1))
                .thenReturn(5001);

        int result =
                applicationServiceImpl.getSeekerUserIdByApplicationId(1);

        assertEquals(5001, result);
    }

    @Test
    void getSeekerUserIdByApplicationId_shouldReturnMinusOneOnException() throws SQLException {
        when(applicationsDAOImpl.fetchSeekerUserIdByApplicationId(anyInt()))
                .thenThrow(new RuntimeException());

        int result =
                applicationServiceImpl.getSeekerUserIdByApplicationId(1);

        assertEquals(-1, result);
    }

    // ---------------- getJobIdByApplicationId ----------------

    @Test
    void getJobIdByApplicationId_shouldReturnJobId() throws SQLException {
        when(applicationsDAOImpl.fetchJobIdByApplicationId(1))
                .thenReturn(3001);

        int result =
                applicationServiceImpl.getJobIdByApplicationId(1);

        assertEquals(3001, result);
    }

    @Test
    void getJobIdByApplicationId_shouldReturnMinusOneOnException() throws SQLException {
        when(applicationsDAOImpl.fetchJobIdByApplicationId(anyInt()))
                .thenThrow(new RuntimeException());

        int result =
                applicationServiceImpl.getJobIdByApplicationId(1);

        assertEquals(-1, result);
    }
}
