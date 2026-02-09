package com.revhire.service;

import com.revhire.dao.impl.JobsDAOImpl;
import com.revhire.model.Job;
import com.revhire.service.impl.JobServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Date;
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
    void addJob_shouldAddJobAndNotifySeekers() {

        Date deadline = Date.valueOf("2026-12-31");

        jobService.addJob(
                10,
                "Java Developer",
                "Backend role",
                "Java, Spring",
                3,
                "B.Tech",
                "Bangalore",
                "10 LPA",
                "FULL_TIME",
                deadline
        );

        verify(jobsDAOImpl).addJob(
                eq(10),
                eq("Java Developer"),
                eq("Backend role"),
                eq("Java, Spring"),
                eq(3),
                eq("B.Tech"),
                eq("Bangalore"),
                eq("10 LPA"),
                eq("FULL_TIME"),
                eq(deadline)
        );

        verify(notificationsService).notifyMatchingJobSeekers(
                "Java Developer",
                "Java, Spring",
                3,
                "Bangalore"
        );
    }

    // ---------------- getAllJobs ----------------

    @Test
    void getAllJobs_shouldReturnJobs() {

        when(jobsDAOImpl.getAllOpenJobs())
                .thenReturn(List.of(new Job()));

        List<Job> result = jobService.getAllJobs();

        assertEquals(1, result.size());
        verify(jobsDAOImpl).getAllOpenJobs();
    }

    // ---------------- getAllJobsOfEmployer ----------------

    @Test
    void getAllJobsOfEmployer_shouldReturnJobs() {

        when(jobsDAOImpl.getJobsByEmployer(5))
                .thenReturn(List.of(new Job(), new Job()));

        List<Job> result = jobService.getAllJobsOfEmployer(5);

        assertEquals(2, result.size());
        verify(jobsDAOImpl).getJobsByEmployer(5);
    }

    // ---------------- searchJobs ----------------

    @Test
    void searchJobs_shouldReturnFilteredJobs() {

        when(jobsDAOImpl.searchJobs(
                anyString(),
                anyString(),
                any(),
                anyString(),
                anyString(),
                anyString()
        )).thenReturn(List.of(new Job()));

        List<Job> result = jobService.searchJobs(
                "Java",
                "Pune",
                5,
                "TCS",
                "10",
                "FULL_TIME"
        );

        assertEquals(1, result.size());
    }

    // ---------------- updateJob ----------------

    @Test
    void updateJob_shouldLogWarningWhenNoRowsUpdated() {

        when(jobsDAOImpl.updateJob(
                anyInt(),
                anyInt(),
                any(),
                any(),
                any(),
                any(),
                any(),
                any(),
                any(),
                any(),
                any()   // Date deadline
        )).thenReturn(0);

        jobService.updateJob(
                1,
                2,
                "Title",
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null
        );

        verify(jobsDAOImpl).updateJob(
                eq(1),
                eq(2),
                any(),
                any(),
                any(),
                any(),
                any(),
                any(),
                any(),
                any(),
                any()
        );
    }

    @Test
    void updateJob_shouldSucceedWhenRowsUpdated() {

        when(jobsDAOImpl.updateJob(
                anyInt(),
                anyInt(),
                any(),
                any(),
                any(),
                any(),
                any(),
                any(),
                any(),
                any(),
                any()
        )).thenReturn(1);

        jobService.updateJob(
                1,
                2,
                "Title",
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null
        );

        verify(jobsDAOImpl, times(1)).updateJob(
                anyInt(),
                anyInt(),
                any(),
                any(),
                any(),
                any(),
                any(),
                any(),
                any(),
                any(),
                any()
        );
    }

    // ---------------- deleteJob ----------------

    @Test
    void deleteJob_shouldCallDAO() {

        doNothing().when(jobsDAOImpl)
                .deleteJob(10, 20);

        jobService.deleteJob(10, 20);

        verify(jobsDAOImpl).deleteJob(10, 20);
    }

    // ---------------- getEmployerUserIdByJob ----------------

    @Test
    void getEmployerUserIdByJob_shouldReturnUserId() {

        when(jobsDAOImpl.fetchEmployerUserIdByJob(99))
                .thenReturn(5001);

        int result = jobService.getEmployerUserIdByJob(99);

        assertEquals(5001, result);
    }

    @Test
    void getEmployerUserIdByJob_shouldReturnMinusOneOnException() {

        when(jobsDAOImpl.fetchEmployerUserIdByJob(anyInt()))
                .thenThrow(new RuntimeException("DB error"));

        int result = jobService.getEmployerUserIdByJob(99);

        assertEquals(-1, result);
    }

    // ---------------- updateJobStatus ----------------

    @Test
    void updateJobStatus_shouldUpdateWhenValidStatus() {

        when(jobsDAOImpl.updateJobStatus(1, 2, "OPEN"))
                .thenReturn(1);

        jobService.updateJobStatus(1, 2, "OPEN");

        verify(jobsDAOImpl)
                .updateJobStatus(1, 2, "OPEN");
    }

    @Test
    void updateJobStatus_shouldNotUpdateWhenInvalidStatus() {

        jobService.updateJobStatus(1, 2, "INVALID");

        verify(jobsDAOImpl, never())
                .updateJobStatus(anyInt(), anyInt(), anyString());
    }

    @Test
    void updateJobStatus_shouldWarnWhenNoRowsUpdated() {

        when(jobsDAOImpl.updateJobStatus(1, 2, "CLOSED"))
                .thenReturn(0);

        jobService.updateJobStatus(1, 2, "CLOSED");

        verify(jobsDAOImpl)
                .updateJobStatus(1, 2, "CLOSED");
    }
}
