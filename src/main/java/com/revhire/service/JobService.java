package com.revhire.service;

import com.revhire.dao.JobsDAO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Date;
import java.util.List;

public class JobService extends JobSeekerService {

    private static final Logger logger =
            LogManager.getLogger(JobService.class);

    private final JobsDAO jobsDAO = new JobsDAO();

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

        jobsDAO.addJob(
                employerId, title, description, skills,
                experience, education, location,
                salary, type, deadline
        );

        logger.info("Job posted successfully | employerId={}, title={}", employerId, title);
    }

    // ---------------- GET ALL JOBS ----------------
    public List<String> getAllJobs() {

        logger.info("Fetching all open jobs");
        return jobsDAO.getAllOpenJobs();
    }

    // ---------------- GET EMPLOYER JOBS ----------------
    public List<String> getAllJobsOfEmployer(int employerId) {

        logger.info("Fetching jobs for employerId={}", employerId);
        return jobsDAO.getJobsByEmployer(employerId);
    }

    // ---------------- SEARCH JOBS ----------------
    public List<String> searchJobs(
            String title,
            String location,
            Integer maxExp,
            String company,
            String salary,
            String type) {

        logger.info("Searching jobs | title={}, location={}, maxExp={}, company={}, type={}",
                title, location, maxExp, company, type);

        return jobsDAO.searchJobs(title, location, maxExp, company, salary, type);
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

        int rows = jobsDAO.updateJob(
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

        jobsDAO.deleteJob(jobId, employerId);

        logger.info("Job deleted successfully | jobId={}", jobId);
    }

    // ---------------- FETCH EMPLOYER USER ID ----------------
    public int getEmployerUserIdByJob(int jobId) {

        logger.debug("Fetching employer userId for jobId={}", jobId);

        try {
            return jobsDAO.fetchEmployerUserIdByJob(jobId);
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

        int rowsUpdated = jobsDAO.updateJobStatus(jobId, employerId, status);

        if (rowsUpdated == 0) {
            logger.warn("Job not found or unauthorized | jobId={}, employerId={}",
                    jobId, employerId);
        } else {
            logger.info("Job status updated successfully | jobId={}, status={}",
                    jobId, status);
        }
    }
}
