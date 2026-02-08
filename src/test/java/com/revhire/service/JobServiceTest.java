package com.revhire.service;

import com.revhire.dao.impl.JobsDAOImpl;
import com.revhire.service.impl.JobServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class JobServiceTest {

    @Mock
    private JobsDAOImpl jobsDAOImpl;

    @InjectMocks
    private JobServiceImpl jobService;

    // ---------------- addJob ----------------

    @Test
    void addJob_shouldCallDAO() {
        // given
        Date deadline = Date.valueOf("2026-02-28");

        // when
        jobService.addJob(
                1,
                "Developer",
                "Job Desc",
                "Java",
                3,
                "B.Tech",
                "Mumbai",
                "10L",
                "Full-Time",
                deadline
        );

        // then
        then(jobsDAOImpl).should().addJob(
                1,
                "Developer",
                "Job Desc",
                "Java",
                3,
                "B.Tech",
                "Mumbai",
                "10L",
                "Full-Time",
                deadline
        );
    }

    // ---------------- getAllJobs ----------------

    @Test
    void getAllJobs_shouldReturnList() {
        // given
        given(jobsDAOImpl.getAllOpenJobs())
                .willReturn(List.of("Job1", "Job2"));

        // when
        List<String> jobs = jobService.getAllJobs();

        // then
        assertEquals(2, jobs.size());
        assertTrue(jobs.contains("Job1"));
        assertTrue(jobs.contains("Job2"));
    }

    // ---------------- getAllJobsOfEmployer ----------------

    @Test
    void getAllJobsOfEmployer_shouldReturnList() {
        // given
        given(jobsDAOImpl.getJobsByEmployer(5))
                .willReturn(List.of("JobA"));

        // when
        List<String> jobs = jobService.getAllJobsOfEmployer(5);

        // then
        assertEquals(1, jobs.size());
        assertEquals("JobA", jobs.get(0));
    }

    // ---------------- searchJobs ----------------

    @Test
    void searchJobs_shouldReturnFilteredList() {
        // given
        given(jobsDAOImpl.searchJobs(
                "Dev",
                "Mumbai",
                3,
                "ABC",
                "10L",
                "Full-Time"
        )).willReturn(List.of("JobX"));

        // when
        List<String> jobs = jobService.searchJobs(
                "Dev",
                "Mumbai",
                3,
                "ABC",
                "10L",
                "Full-Time"
        );

        // then
        assertEquals(1, jobs.size());
        assertEquals("JobX", jobs.get(0));
    }

    // ---------------- updateJob ----------------

    @Test
    void updateJob_shouldCallDAO() {
        // given
        Date deadline = Date.valueOf("2026-03-01");
        given(jobsDAOImpl.updateJob(
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
        )).willReturn(1);

        // when
        jobService.updateJob(
                10,
                1,
                "Title",
                "Desc",
                "Skills",
                3,
                "B.Tech",
                "Delhi",
                "12L",
                "Full-Time",
                deadline
        );

        // then
        then(jobsDAOImpl).should().updateJob(
                10,
                1,
                "Title",
                "Desc",
                "Skills",
                3,
                "B.Tech",
                "Delhi",
                "12L",
                "Full-Time",
                deadline
        );
    }

    // ---------------- deleteJob ----------------

    @Test
    void deleteJob_shouldCallDAO() {
        // when
        jobService.deleteJob(10, 1);

        // then
        then(jobsDAOImpl).should().deleteJob(10, 1);
    }

    // ---------------- getEmployerUserIdByJob ----------------

    @Test
    void getEmployerUserIdByJob_shouldReturnUserId() {
        // given
        given(jobsDAOImpl.fetchEmployerUserIdByJob(5))
                .willReturn(100);

        // when
        int userId = jobService.getEmployerUserIdByJob(5);

        // then
        assertEquals(100, userId);
    }

    @Test
    void getEmployerUserIdByJob_shouldReturnMinusOneOnException() {
        // given
        willThrow(RuntimeException.class)
                .given(jobsDAOImpl)
                .fetchEmployerUserIdByJob(anyInt());

        // when
        int userId = jobService.getEmployerUserIdByJob(5);

        // then
        assertEquals(-1, userId);
    }

    // ---------------- updateJobStatus ----------------

    @Test
    void updateJobStatus_shouldCallDAO_whenValidStatus() {
        // given
        given(jobsDAOImpl.updateJobStatus(10, 1, "OPEN"))
                .willReturn(1);

        // when
        jobService.updateJobStatus(10, 1, "OPEN");

        // then
        then(jobsDAOImpl).should()
                .updateJobStatus(10, 1, "OPEN");
    }

    @Test
    void updateJobStatus_shouldNotCallDAO_whenInvalidStatus() {
        // when
        jobService.updateJobStatus(10, 1, "INVALID");

        // then
        then(jobsDAOImpl).should(never())
                .updateJobStatus(anyInt(), anyInt(), any());
    }
}
