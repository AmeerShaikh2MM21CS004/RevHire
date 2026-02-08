package com.revhire.model;

import java.sql.Timestamp;

public class Application {
    private int applicationId;
    private int jobId;
    private int seekerId;
    private String status;
    private Timestamp appliedAt;

    public Application(int applicationId, int jobId, int seekerId, String status, Timestamp appliedAt) {
        this.applicationId = applicationId;
        this.jobId = jobId;
        this.seekerId = seekerId;
        this.status = status;
        this.appliedAt = appliedAt;
    }

    // Getters
    public int getApplicationId() { return applicationId; }
    public int getJobId() { return jobId; }
    public int getSeekerId() { return seekerId; }
    public String getStatus() { return status; }
    public Timestamp getAppliedAt() { return appliedAt; }

    @Override
    public String toString() {
        return "Application ID: " + applicationId +
                " | Job ID: " + jobId +
                " | Seeker ID: " + seekerId +
                " | Status: " + status +
                " | Applied On: " + appliedAt;
    }
}
