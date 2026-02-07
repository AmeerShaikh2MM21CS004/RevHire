package com.revhire.service.impl;

import com.revhire.dao.impl.ApplicationsDAOImpl;
import com.revhire.model.Application;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class ApplicationServiceImpl implements com.revhire.service.ApplicationService {

    private static final Logger logger =
            LogManager.getLogger(ApplicationServiceImpl.class);

    private final ApplicationsDAOImpl applicationsDAOImpl;

    // Default constructor
    public ApplicationServiceImpl() {
        this.applicationsDAOImpl = new ApplicationsDAOImpl();
    }

    // Constructor for testing (dependency injection)
    public ApplicationServiceImpl(ApplicationsDAOImpl applicationsDAOImpl) {
        this.applicationsDAOImpl = applicationsDAOImpl;
    }

    @Override
    public void applyForJob(int jobId, int seekerId) {

        logger.info("Applying for jobId={} by seekerId={}", jobId, seekerId);

        if (applicationsDAOImpl.hasAlreadyApplied(jobId, seekerId)) {
            logger.warn("Duplicate application attempt: jobId={}, seekerId={}", jobId, seekerId);
            return;
        }

        applicationsDAOImpl.applyJob(jobId, seekerId);
        logger.info("Job application submitted successfully: jobId={}, seekerId={}", jobId, seekerId);
    }

    // Withdraw application with optional reason
    @Override
    public void withdrawApplication(int applicationId, String status, String reason) {

        logger.info("Withdrawing applicationId={}, status={}, reason={}",
                applicationId, status, reason);

        applicationsDAOImpl.updateStatus(applicationId, status, reason);

        logger.info("Application withdrawn successfully: applicationId={}", applicationId);
    }

    @Override
    public List<String> viewMyApplications(int seekerId) {

        logger.info("Fetching applications for seekerId={}", seekerId);
        return applicationsDAOImpl.getApplicationsBySeeker(seekerId);
    }

    @Override
    public List<Application> getApplicantsForJob(int jobId) {

        logger.info("Fetching applicants for jobId={}", jobId);

        try {
            return applicationsDAOImpl.fetchApplicationsByJobId(jobId);
        } catch (Exception e) {
            logger.error("Error fetching applicants for jobId={}", jobId, e);
            return List.of(); // safe fallback
        }
    }

    @Override
    public void updateApplicationStatus(int appId, String status) {

        logger.info("Updating application status: applicationId={}, status={}", appId, status);

        try {
            applicationsDAOImpl.updateStatusByApplicationId(appId, status);
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
            return applicationsDAOImpl.fetchSeekerUserIdByApplicationId(appId);
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
            return applicationsDAOImpl.fetchJobIdByApplicationId(appId);
        } catch (Exception e) {
            logger.error("Error fetching jobId for applicationId={}", appId, e);
            return -1;
        }
    }
}
