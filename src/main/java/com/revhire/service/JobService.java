package com.revhire.service;

import java.sql.Date;
import java.util.List;

public interface JobService {

    void addJob(
            int employerId,
            String title,
            String description,
            String skills,
            int experience,
            String education,
            String location,
            String salary,
            String type,
            Date deadline
    );

    List<String> getAllJobs();

    List<String> getAllJobsOfEmployer(int employerId);

    List<String> searchJobs(
            String title,
            String location,
            Integer maxExp,
            String company,
            String salary,
            String type
    );

    void updateJob(
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
            Date deadline
    );

    void deleteJob(int jobId, int employerId);

    int getEmployerUserIdByJob(int jobId);

    void updateJobStatus(int jobId, int employerId, String status);

}
