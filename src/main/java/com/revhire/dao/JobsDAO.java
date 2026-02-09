package com.revhire.dao;

import com.revhire.model.Job;

import java.sql.Date;
import java.util.List;

public interface JobsDAO {

    void addJob(
            int employerId,
            String title,
            String description,
            String skillsRequired,
            int experienceRequired,
            String educationRequired,
            String location,
            String salary,
            String jobType,
            Date deadline
    );

    List<Job> getAllOpenJobs();

    List<String> getJobsByEmployer(int employerId);

    List<Job> searchJobs(
            String title,
            String location,
            Integer maxExp,
            String company,
            String salary,
            String type
    );

    int updateJob(
            int jobId,
            int employerId,
            String title,
            String description,
            String skills,
            Integer experience,
            String education,
            String location,
            String salary,
            String jobType,
            Date deadline
    );

    int updateJobStatus(int jobId, int employerId, String status);

    void deleteJob(int jobId, int employerId);

    int fetchEmployerUserIdByJob(int jobId);

}
