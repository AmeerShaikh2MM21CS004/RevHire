package com.revhire.service;

import com.revhire.dao.JobsDAO;

import java.sql.Date;
import java.util.List;

public class JobService {

    private final JobsDAO jobsDAO = new JobsDAO();

    public void addJob(
            int employerId,
            String title,
            String description,
            String skills,
            int experience,
            String education,
            String location,
            String salary,
            String type,
            Date deadline) {

        jobsDAO.addJob(
                employerId, title, description, skills,
                experience, education, location,
                salary, type, deadline
        );

        System.out.println("✅ Job posted successfully.");
    }

    public List<String> getAllJobs() {
        return jobsDAO.getAllOpenJobs();
    }

    public List<String> getAllJobsOfEmployer(int employerId) {
        return jobsDAO.getJobsByEmployer(employerId);
    }

    public List<String> searchJobs(
            String title,
            String location,
            Integer maxExp,
            String company,
            String salary,
            String type) {

        return jobsDAO.searchJobs(title, location, maxExp, company, salary, type);
    }

    public void updateJob(
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
            Date deadline) {

        int rows = jobsDAO.updateJob(
                jobId, employerId, title, description,
                skills, experience, education,
                location, salary, type, deadline
        );

        if (rows == 0) {
            System.out.println("⚠️ Nothing updated or unauthorized access.");
        } else {
            System.out.println("✅ Job updated successfully.");
        }
    }

    public void deleteJob(int jobId, int employerId) {
        jobsDAO.deleteJob(jobId, employerId);
        System.out.println("🗑️ Job deleted successfully.");
    }

    public int getEmployerUserIdByJob(int jobId) {
        try {
            return jobsDAO.fetchEmployerUserIdByJob(jobId); // DAO call
        } catch (Exception e) {
            e.printStackTrace();
            return -1; // return -1 if job not found or error
        }
    }

    public void updateJobStatus(int jobId, int employerId, String status) {

        if (!status.equals("OPEN") && !status.equals("CLOSED")) {
            System.out.println("❌ Invalid status. Use OPEN or CLOSED.");
            return;
        }

        int rowsUpdated = jobsDAO.updateJobStatus(jobId, employerId, status);

        if (rowsUpdated == 0) {
            System.out.println("❌ Job not found or you are not authorized to modify this job.");
        } else {
            System.out.println("✅ Job status updated successfully to " + status + ".");
        }
    }

}
