package com.revhire.service.impl;

import com.revhire.model.Application;
import java.util.List;

public interface  ApplicationServiceimpl {

    void applyForJob(int jobId, int seekerId);

    void withdrawApplication(int applicationId, String status, String reason);

    List<String> viewMyApplications(int seekerId);

    List<Application> getApplicantsForJob(int jobId);

    void updateApplicationStatus(int appId, String status);

    int getSeekerUserIdByApplicationId(int appId);

    int getJobIdByApplicationId(int appId);

}
