package com.revhire.service;

import com.revhire.model.Application;
import java.util.List;

public interface ApplicationService {

    boolean applyForJob(int jobId, int seekerId);

    boolean withdrawApplication(int applicationId, String status, String reason);

    List<Application> viewMyApplications(int seekerId);

    List<Application> getApplicantsForJob(int jobId);

    void updateApplicationStatus(int appId, String status);

    int getSeekerUserIdByApplicationId(int appId);

    // Get the jobseeker (user) ID for a given application
    int getSeekerIdByApplicationId(int appId);

    int getJobIdByApplicationId(int appId);

}
