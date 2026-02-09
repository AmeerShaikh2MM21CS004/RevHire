package com.revhire.model;

import java.sql.Timestamp;

public class Application {
    private int applicationId;
    private int jobId;
    private int seekerId;
    private String seekerName;
    private String status;
    private Timestamp appliedAt;
    private String withdrawReason; // Changed to camelCase for Java standards

    public Application(int applicationId, int jobId, int seekerId, String seekerName,String status, Timestamp appliedAt, String withdrawReason) {
        this.applicationId = applicationId;
        this.jobId = jobId;
        this.seekerId = seekerId;
        this.seekerName = seekerName;
        this.status = status;
        this.appliedAt = appliedAt;
        this.withdrawReason = withdrawReason;
    }

    public Application(int applicationId, int jobId, int seekerId, String status, Timestamp appliedAt, String withdrawReason) {
        this.applicationId = applicationId;
        this.jobId = jobId;
        this.seekerId = seekerId;
        this.status = status;
        this.appliedAt = appliedAt;
        this.withdrawReason = withdrawReason;
    }

    // Getters
    public int getApplicationId() { return applicationId; }
    public int getJobId() { return jobId; }
    public int getSeekerId() { return seekerId; }
    public String getSeekerName() { return seekerName; }
    public String getStatus() { return status; }
    public Timestamp getAppliedAt() { return appliedAt; }
    public String getWithdrawReason() { return withdrawReason; }

    @Override
    public String toString() {
        return String.format("Application ID: %d | Job ID: %d | Seeker ID: %d | Status: %s | Applied On: %s | Reason: %s",
                applicationId, jobId, seekerId, status, appliedAt,
                (withdrawReason != null ? withdrawReason : "N/A"));
    }
}