package com.revhire.service;

import com.revhire.dao.ApplicationsDAO;
import com.revhire.model.Application;
import com.revhire.service.impl.ApplicationServiceimpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class ApplicationService implements ApplicationServiceimpl {

    private static final Logger logger =
            LogManager.getLogger(ApplicationService.class);

    private final ApplicationsDAO applicationsDAO;

    // Default constructor
    public ApplicationService() {
        this.applicationsDAO = new ApplicationsDAO();
    }

    // Constructor for testing (dependency injection)
    public ApplicationService(ApplicationsDAO applicationsDAO) {
        this.applicationsDAO = applicationsDAO;
    }

    @Override
    public void applyForJob(int jobId, int seekerId) {

        logger.info("Applying for jobId={} by seekerId={}", jobId, seekerId);

        if (applicationsDAO.hasAlreadyApplied(jobId, seekerId)) {
            logger.warn("Duplicate application attempt: jobId={}, seekerId={}", jobId, seekerId);
            return;
        }

        applicationsDAO.applyJob(jobId, seekerId);
        logger.info("Job application submitted successfully: jobId={}, seekerId={}", jobId, seekerId);
    }

    // Withdraw application with optional reason
    @Override
    public void withdrawApplication(int applicationId, String status, String reason) {

        logger.info("Withdrawing applicationId={}, status={}, reason={}",
                applicationId, status, reason);

        applicationsDAO.updateStatus(applicationId, status, reason);

        logger.info("Application withdrawn successfully: applicationId={}", applicationId);
    }

    @Override
    public List<String> viewMyApplications(int seekerId) {

        logger.info("Fetching applications for seekerId={}", seekerId);
        return applicationsDAO.getApplicationsBySeeker(seekerId);
    }

    @Override
    public List<Application> getApplicantsForJob(int jobId) {

        logger.info("Fetching applicants for jobId={}", jobId);

        try {
            return applicationsDAO.fetchApplicationsByJobId(jobId);
        } catch (Exception e) {
            logger.error("Error fetching applicants for jobId={}", jobId, e);
            return List.of(); // safe fallback
        }
    }

    @Override
    public void updateApplicationStatus(int appId, String status) {

        logger.info("Updating application status: applicationId={}, status={}", appId, status);

        try {
            applicationsDAO.updateStatusByApplicationId(appId, status);
            logger.info("Application status updated successfully: applicationId={}", appId);
        } catch (Exception e) {
            logger.error("Error updating application status: applicationId={}", appId, e);
        }
    }

    // Get the jobseeker (user) ID for a given application
    @Override
    public int getSeekerUserIdByApplicationId(int appId) {

        logger.info("Fetching seeker userId for applicationId={}", appId);

        try {
            return applicationsDAO.fetchSeekerUserIdByApplicationId(appId);
        } catch (Exception e) {
            logger.error("Error fetching seeker userId for applicationId={}", appId, e);
            return -1;
        }
    }

    // Get the job ID for a given application
    @Override
    public int getJobIdByApplicationId(int appId) {

        logger.info("Fetching jobId for applicationId={}", appId);

        try {
            return applicationsDAO.fetchJobIdByApplicationId(appId);
        } catch (Exception e) {
            logger.error("Error fetching jobId for applicationId={}", appId, e);
            return -1;
        }
    }
}
