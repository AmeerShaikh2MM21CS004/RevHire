package com.revhire.service;

import com.revhire.dao.JobsDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.sql.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class JobServiceTest {

    private JobsDAO jobsDAO;
    private JobService jobService;

    @BeforeEach
    void setup() {
        jobsDAO = mock(JobsDAO.class);
        jobService = new JobService(jobsDAO);
    }

    // ---------------- addJob ----------------
    @Test
    void addJob_shouldCallDAO() {
        Date deadline = Date.valueOf("2026-02-28");

        jobService.addJob(1, "Developer", "Job Desc", "Java", 3, "B.Tech",
                "Mumbai", "10L", "Full-Time", deadline);

        verify(jobsDAO).addJob(1, "Developer", "Job Desc", "Java", 3,
                "B.Tech", "Mumbai", "10L", "Full-Time", deadline);
    }

    // ---------------- getAllJobs ----------------
    @Test
    void getAllJobs_shouldReturnList() {
        when(jobsDAO.getAllOpenJobs()).thenReturn(List.of("Job1", "Job2"));

        List<String> jobs = jobService.getAllJobs();

        assertEquals(2, jobs.size());
        assertTrue(jobs.contains("Job1"));
        assertTrue(jobs.contains("Job2"));
    }

    // ---------------- getAllJobsOfEmployer ----------------
    @Test
    void getAllJobsOfEmployer_shouldReturnList() {
        when(jobsDAO.getJobsByEmployer(5)).thenReturn(List.of("JobA"));

        List<String> jobs = jobService.getAllJobsOfEmployer(5);

        assertEquals(1, jobs.size());
        assertEquals("JobA", jobs.get(0));
    }

    // ---------------- searchJobs ----------------
    @Test
    void searchJobs_shouldReturnFilteredList() {
        when(jobsDAO.searchJobs("Dev", "Mumbai", 3, "ABC", "10L", "Full-Time"))
                .thenReturn(List.of("JobX"));

        List<String> jobs = jobService.searchJobs("Dev", "Mumbai", 3, "ABC", "10L", "Full-Time");

        assertEquals(1, jobs.size());
        assertEquals("JobX", jobs.get(0));
    }

    // ---------------- updateJob ----------------
    @Test
    void updateJob_shouldCallDAO() {
        when(jobsDAO.updateJob(anyInt(), anyInt(), any(), any(), any(), any(), any(), any(), any(), any(), any()))
                .thenReturn(1);

        jobService.updateJob(10, 1, "Title", "Desc", "Skills", 3,
                "B.Tech", "Delhi", "12L", "Full-Time", Date.valueOf("2026-03-01"));

        verify(jobsDAO).updateJob(10, 1, "Title", "Desc", "Skills", 3,
                "B.Tech", "Delhi", "12L", "Full-Time", Date.valueOf("2026-03-01"));
    }

    // ---------------- deleteJob ----------------
    @Test
    void deleteJob_shouldCallDAO() {
        jobService.deleteJob(10, 1);

        verify(jobsDAO).deleteJob(10, 1);
    }

    // ---------------- getEmployerUserIdByJob ----------------
    @Test
    void getEmployerUserIdByJob_shouldReturnUserId() throws Exception {
        when(jobsDAO.fetchEmployerUserIdByJob(5)).thenReturn(100);

        int userId = jobService.getEmployerUserIdByJob(5);

        assertEquals(100, userId);
    }

    @Test
    void getEmployerUserIdByJob_shouldReturnMinusOneOnException() throws Exception {
        when(jobsDAO.fetchEmployerUserIdByJob(anyInt())).thenThrow(new RuntimeException());

        int userId = jobService.getEmployerUserIdByJob(5);

        assertEquals(-1, userId);
    }

    // ---------------- updateJobStatus ----------------
    @Test
    void updateJobStatus_shouldCallDAO_whenValidStatus() {
        when(jobsDAO.updateJobStatus(10, 1, "OPEN")).thenReturn(1);

        jobService.updateJobStatus(10, 1, "OPEN");

        verify(jobsDAO).updateJobStatus(10, 1, "OPEN");
    }

    @Test
    void updateJobStatus_shouldNotCallDAO_whenInvalidStatus() {
        jobService.updateJobStatus(10, 1, "INVALID");

        verify(jobsDAO, never()).updateJobStatus(anyInt(), anyInt(), any());
    }
}
