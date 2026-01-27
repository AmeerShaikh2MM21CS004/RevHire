package com.revhire.service;

import com.revhire.dao.JobSeekersDAO;
import com.revhire.service.impl.JobSeekerServiceimpl;

import java.sql.SQLException;

public class JobSeekerService implements JobSeekerServiceimpl {

    private final JobSeekersDAO jobSeekerDAO = new JobSeekersDAO();

    public void createProfile(int userId, String fullName, String phone,
                              String location, int totalExperience) {

        try {
            jobSeekerDAO.insertJobSeeker(
                    userId, fullName, phone, location, totalExperience, 'Y'
            );
        } catch (SQLException e) {
            System.out.println("❌ Failed to create profile.");
        }
    }

    public void updateJobSeekerProfile(int seekerId, String fullName, String phone,
                              String location, Integer totalExperience) {

        try {
            int rows = jobSeekerDAO.updateJobSeeker(
                    seekerId, fullName, phone, location, totalExperience
            );

            if (rows > 0) {
                System.out.println("✅ Job seeker profile updated successfully.");
            } else {
                System.out.println("⚠️ Nothing to update.");
            }
        } catch (SQLException e) {
            System.out.println("❌ Failed to update profile.");
        }
    }

    public int getSeekerIdByUserId(int userId) {
        try {
            int seekerId = jobSeekerDAO.findSeekerIdByUserId(userId);
            if (seekerId == -1) {
                throw new RuntimeException("Job seeker profile not found");
            }
            return seekerId;
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching seeker ID", e);
        }
    }

    public void createJobSeeker(int userId) {
        createProfile(userId, null, null, null, 0);
    }


}
