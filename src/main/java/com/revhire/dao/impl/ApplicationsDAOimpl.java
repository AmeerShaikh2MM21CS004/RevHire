package com.revhire.dao.impl;

import com.revhire.model.Application;

import java.sql.SQLException;
import java.util.List;

public interface ApplicationsDAOimpl {

    void applyJob(int jobId, int seekerId);

    boolean hasAlreadyApplied(int jobId, int seekerId);

    void updateStatus(int applicationId, String status, String reason);

    List<String> getApplicationsBySeeker(int seekerId);

    List<Application> fetchApplicationsByJobId(int jobId) throws SQLException;

    boolean updateStatusByApplicationId(int appId, String status) throws SQLException;

    int fetchSeekerUserIdByApplicationId(int appId) throws SQLException;

    int fetchJobIdByApplicationId(int appId) throws SQLException;
}
