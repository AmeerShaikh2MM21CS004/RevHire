package com.revhire.service.impl;

import com.revhire.dao.impl.JobsDAOImpl;
import com.revhire.model.Job;
import com.revhire.service.NotificationsService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Date;
import java.util.List;

public class JobServiceImpl extends JobSeekerServiceImpl {

    private static final Logger logger = LogManager.getLogger(JobServiceImpl.class);

    private final JobsDAOImpl jobsDAOImpl;

    private final NotificationsService notificationsService;

    // Default constructor
    public JobServiceImpl() {
        this.jobsDAOImpl = new JobsDAOImpl();
        this.notificationsService = new NotificationsServiceImpl();
    }

    // Constructor for unit testing
    public JobServiceImpl(JobsDAOImpl jobsDAOImpl,
                          NotificationsService notificationsService) {
        this.jobsDAOImpl = jobsDAOImpl;
        this.notificationsService = notificationsService;
    }


    // ---------------- ADD JOB ----------------
    public void addJob(
            int employerId,
            String title,
            String description,
            String skills,
            int experience,
            String education,
            String location,
            String salary,
            String type,
            Date deadline) {

        logger.info("Adding job | employerId={}, title={}", employerId, title);

        jobsDAOImpl.addJob(
                employerId, title, description, skills,
                experience, education, location,
                salary, type, deadline
        );

        //  Proper service call (NO ERROR now)
        notificationsService.notifyMatchingJobSeekers(
                title, skills, experience, location
        );

        logger.info("Job posted & notifications sent | title={}", title);
    }

    // ---------------- GET ALL JOBS ----------------
    public List<Job> getAllJobs() {
        logger.info("Fetching all open jobs from service");
        return jobsDAOImpl.getAllOpenJobs();
    }

    // ---------------- GET EMPLOYER JOBS ----------------
    public List<Job> getAllJobsOfEmployer(int employerId) {
        logger.info("Fetching jobs for employerId={}", employerId);
        return jobsDAOImpl.getJobsByEmployer(employerId);
    }


    // ---------------- SEARCH JOBS ----------------
    public List<Job> searchJobs(
            String title,
            String location,
            Integer maxExp,
            String company,
            String salary,
            String type) {

        logger.info("Searching jobs | title={}, location={}, maxExp={}, company={}, type={}",
                title, location, maxExp, company, type);

        return jobsDAOImpl.searchJobs(title, location, maxExp, company, salary, type);
    }

    // ---------------- UPDATE JOB ----------------
    public void updateJob(
            int jobId,
            int employerId,
            String title,
            String description,
            String skills,
            Integer experience,
            String education,
            String location,
            String salary,
            String type,
            Date deadline) {

        logger.info("Updating job | jobId={}, employerId={}", jobId, employerId);

        int rows = jobsDAOImpl.updateJob(
                jobId, employerId, title, description,
                skills, experience, education,
                location, salary, type, deadline
        );

        if (rows == 0) {
            logger.warn("No update performed or unauthorized | jobId={}, employerId={}",
                    jobId, employerId);
        } else {
            logger.info("Job updated successfully | jobId={}", jobId);
        }
    }

    // ---------------- DELETE JOB ----------------
    public void deleteJob(int jobId, int employerId) {

        logger.info("Deleting job | jobId={}, employerId={}", jobId, employerId);

        jobsDAOImpl.deleteJob(jobId, employerId);

        logger.info("Job deleted successfully | jobId={}", jobId);
    }

    // ---------------- FETCH EMPLOYER USER ID ----------------
    public int getEmployerUserIdByJob(int jobId) {

        logger.debug("Fetching employer userId for jobId={}", jobId);

        try {
            return jobsDAOImpl.fetchEmployerUserIdByJob(jobId);
        } catch (Exception e) {
            logger.error("Error fetching employer userId | jobId={}", jobId, e);
            return -1;
        }
    }

    // ---------------- UPDATE JOB STATUS ----------------
    public void updateJobStatus(int jobId, int employerId, String status) {

        logger.info("Updating job status | jobId={}, employerId={}, status={}",
                jobId, employerId, status);

        if (!status.equals("OPEN") && !status.equals("CLOSED")) {
            logger.warn("Invalid job status provided | status={}", status);
            return;
        }

        int rowsUpdated = jobsDAOImpl.updateJobStatus(jobId, employerId, status);

        if (rowsUpdated == 0) {
            logger.warn("Job not found or unauthorized | jobId={}, employerId={}",
                    jobId, employerId);
        } else {
            logger.info("Job status updated successfully | jobId={}, status={}",
                    jobId, status);
        }
    }
}
