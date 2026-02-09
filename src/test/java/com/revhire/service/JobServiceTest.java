package com.revhire.service;

import com.revhire.dao.impl.JobsDAOImpl;
import com.revhire.model.Job;
import com.revhire.service.impl.JobServiceImpl;
import com.revhire.service.NotificationsService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Date;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JobServiceTest {

    @Mock
    private JobsDAOImpl jobsDAOImpl;

    @Mock
    private NotificationsService notificationsService;

    @InjectMocks
    private JobServiceImpl jobService;

    // ---------------- addJob ----------------

    @Test
    void addJob_shouldCallDAOAndNotify() {
        Date deadline = Date.valueOf("2026-02-28");

        jobService.addJob(
                1, "Developer", "Desc", "Java",
                3, "B.Tech", "Mumbai",
                "10L", "Full-Time", deadline
        );

        verify(jobsDAOImpl).addJob(
                1, "Developer", "Desc", "Java",
                3, "B.Tech", "Mumbai",
                "10L", "Full-Time", deadline
        );

        verify(notificationsService).notifyMatchingJobSeekers(
                "Developer", "Java", 3, "Mumbai"
        );
    }

    // ---------------- getAllJobsOfEmployer ----------------

    @Test
    void getAllJobsOfEmployer_shouldReturnEmployerJobs() {
        when(jobsDAOImpl.getJobsByEmployer(5))
                .thenReturn(List.of("JobA"));

        List<String> jobs = jobService.getAllJobsOfEmployer(5);

        assertEquals(1, jobs.size());
        assertEquals("JobA", jobs.get(0));
    }

    // ---------------- searchJobs ----------------

    @Test
    void searchJobs_shouldReturnFilteredJobs_WhenMatchesExist() {
        Job mockJob = new Job();
        mockJob.setJobId(101);
        mockJob.setTitle("Java Developer");
        mockJob.setCompanyName("ABC Corp");
        mockJob.setLocation("Mumbai");
        mockJob.setSalary("10L");
        mockJob.setJobType("Full-Time");

        when(jobsDAOImpl.searchJobs("Dev", "Mumbai", 3, "ABC", "10L", "Full-Time"))
                .thenReturn(List.of(mockJob));

        // Act
        List<Job> results = jobService.searchJobs("Dev", "Mumbai", 3, "ABC", "10L", "Full-Time");

        // Assert
        assertEquals(1, results.size(), "Should return exactly one job");
        assertEquals("Java Developer", results.get(0).getTitle());
        assertEquals("ABC Corp", results.get(0).getCompanyName());

        verify(jobsDAOImpl, times(1)).searchJobs("Dev", "Mumbai", 3, "ABC", "10L", "Full-Time");
    }

    @Test
    void searchJobs_shouldReturnEmptyList_WhenNoMatchesFound() {
        // Arrange
        when(jobsDAOImpl.searchJobs(anyString(), anyString(), anyInt(), anyString(), anyString(), anyString()))
                .thenReturn(Collections.emptyList());

        // Act
        List<Job> results = jobService.searchJobs("NonExistent", "Mars", 10, "NASA", "0", "Contract");

        // Assert
        assertTrue(results.isEmpty(), "Result list should be empty for non-matching criteria");
    }

    @Test
    void searchJobs_shouldThrowException_WhenDatabaseFails() {
        when(jobsDAOImpl.searchJobs(any(), any(), any(), any(), any(), any()))
                .thenThrow(new RuntimeException("DB Connection Down"));

        assertThrows(RuntimeException.class, () -> {
            jobService.searchJobs(null, null, null, null, null, null);
        });
    }

    // ---------------- updateJob ----------------
    @Test
    void updateJob_shouldLogSuccess_whenRowsUpdated() {
        when(jobsDAOImpl.updateJob(
                anyInt(), anyInt(), any(), any(), any(),
                any(), any(), any(), any(), any(), any()
        )).thenReturn(1);

        assertDoesNotThrow(() ->
                jobService.updateJob(
                        10, 1, "Title", "Desc", "Skills",
                        3, "B.Tech", "Delhi", "12L",
                        "Full-Time", Date.valueOf("2026-03-01")
                )
        );
    }

    @Test
    void updateJob_shouldHandleZeroRowsUpdated() {
        when(jobsDAOImpl.updateJob(
                anyInt(), anyInt(), any(), any(), any(),
                any(), any(), any(), any(), any(), any()
        )).thenReturn(0);

        assertDoesNotThrow(() ->
                jobService.updateJob(
                        10, 1, "Title", "Desc", "Skills",
                        3, "B.Tech", "Delhi", "12L",
                        "Full-Time", Date.valueOf("2026-03-01")
                )
        );
    }

    // ---------------- deleteJob ----------------

    @Test
    void deleteJob_shouldCallDAO() {
        jobService.deleteJob(10, 1);

        verify(jobsDAOImpl).deleteJob(10, 1);
    }

    // ---------------- getEmployerUserIdByJob ----------------

    @Test
    void getEmployerUserIdByJob_shouldReturnUserId() {
        when(jobsDAOImpl.fetchEmployerUserIdByJob(5))
                .thenReturn(100);

        int userId = jobService.getEmployerUserIdByJob(5);

        assertEquals(100, userId);
    }

    @Test
    void getEmployerUserIdByJob_shouldReturnMinusOneOnException() {
        when(jobsDAOImpl.fetchEmployerUserIdByJob(anyInt()))
                .thenThrow(RuntimeException.class);

        int userId = jobService.getEmployerUserIdByJob(5);

        assertEquals(-1, userId);
    }

    // ---------------- updateJobStatus ----------------

    @Test
    void updateJobStatus_shouldUpdateWhenOpen() {
        when(jobsDAOImpl.updateJobStatus(10, 1, "OPEN"))
                .thenReturn(1);

        jobService.updateJobStatus(10, 1, "OPEN");

        verify(jobsDAOImpl).updateJobStatus(10, 1, "OPEN");
    }

    @Test
    void updateJobStatus_shouldUpdateWhenClosed() {
        when(jobsDAOImpl.updateJobStatus(10, 1, "CLOSED"))
                .thenReturn(1);

        jobService.updateJobStatus(10, 1, "CLOSED");

        verify(jobsDAOImpl).updateJobStatus(10, 1, "CLOSED");
    }

    @Test
    void updateJobStatus_shouldHandleZeroRowsUpdated() {
        when(jobsDAOImpl.updateJobStatus(10, 1, "OPEN"))
                .thenReturn(0);

        jobService.updateJobStatus(10, 1, "OPEN");

        verify(jobsDAOImpl).updateJobStatus(10, 1, "OPEN");
    }

    @Test
    void updateJobStatus_shouldNotCallDAO_whenInvalidStatus() {
        jobService.updateJobStatus(10, 1, "INVALID");

        verify(jobsDAOImpl, never())
                .updateJobStatus(anyInt(), anyInt(), any());
    }
}
