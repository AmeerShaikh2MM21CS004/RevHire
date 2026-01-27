package com.revhire.service;

import com.revhire.dao.ApplicationsDAO;
import com.revhire.model.Application;
import com.revhire.service.impl.ApplicationServiceimpl;

import java.util.List;

public class ApplicationService implements ApplicationServiceimpl {

    private final ApplicationsDAO applicationsDAO = new ApplicationsDAO();

    public void applyForJob(int jobId, int seekerId) {

        if (applicationsDAO.hasAlreadyApplied(jobId, seekerId)) {
            System.out.println("❌ You have already applied for this job.");
            return;
        }

        applicationsDAO.applyJob(jobId, seekerId);
        System.out.println("✅ Job application submitted successfully.");
    }

    // Main method with optional reason
    public void withdrawApplication(int applicationId, String status, String reason) {
        applicationsDAO.updateStatus(applicationId, status, reason);
        System.out.println("✅ Application withdrawn successfully.");
    }

    public List<String> viewMyApplications(int seekerId) {
        return applicationsDAO.getApplicationsBySeeker(seekerId);
    }

    public List<Application> getApplicantsForJob(int jobId) {
        try {
            return applicationsDAO.fetchApplicationsByJobId(jobId);
        } catch (Exception e) {
            e.printStackTrace();
            return List.of(); // empty list on error
        }
    }

    public void updateApplicationStatus(int appId, String status) {
        try {
            applicationsDAO.updateStatusByApplicationId(appId, status);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Get the jobseeker (user) ID for a given application
    public int getSeekerUserIdByApplicationId(int appId) {
        try {
            return applicationsDAO.fetchSeekerUserIdByApplicationId(appId);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    // Get the job ID for a given application
    public int getJobIdByApplicationId(int appId) {
        try {
            return applicationsDAO.fetchJobIdByApplicationId(appId);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }
}
