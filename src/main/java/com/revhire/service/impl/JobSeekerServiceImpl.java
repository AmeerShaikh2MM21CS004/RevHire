package com.revhire.service.impl;

import com.revhire.dao.impl.JobSeekersDAOImpl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.SQLException;

public class JobSeekerServiceImpl implements com.revhire.service.JobSeekerService {

    private static final Logger logger =
            LogManager.getLogger(JobSeekerServiceImpl.class);

    private final JobSeekersDAOImpl jobSeekerDAO;

    // Default constructor
    public JobSeekerServiceImpl() {
        this.jobSeekerDAO = new JobSeekersDAOImpl();
    }

    // Constructor for unit testing
    public JobSeekerServiceImpl(JobSeekersDAOImpl jobSeekerDAO) {
        this.jobSeekerDAO = jobSeekerDAO;
    }

    @Override
    public void createProfile(
            int userId,
            String fullName,
            String phone,
            String location,
            int totalExperience) {

        logger.info("Creating job seeker profile | userId={}", userId);

        try {
            jobSeekerDAO.insertJobSeeker(
                    userId,
                    fullName,
                    phone,
                    location,
                    totalExperience,
                    'Y'
            );

            logger.info("Job seeker profile created successfully | userId={}", userId);

        } catch (SQLException e) {
            logger.error("Failed to create job seeker profile | userId={}", userId, e);
            throw new RuntimeException("Failed to create job seeker profile", e);
        }
    }

    @Override
    public void updateJobSeekerProfile(
            int seekerId,
            String fullName,
            String phone,
            String location,
            Integer totalExperience) {

        logger.info("Updating job seeker profile | seekerId={}", seekerId);

        try {
            int rows = jobSeekerDAO.updateJobSeeker(
                    seekerId,
                    fullName,
                    phone,
                    location,
                    totalExperience
            );

            if (rows > 0) {
                logger.info("Job seeker profile updated | seekerId={}", seekerId);
            } else {
                logger.warn("No fields updated | seekerId={}", seekerId);
            }

        } catch (SQLException e) {
            logger.error("Failed to update job seeker profile | seekerId={}", seekerId, e);
            throw new RuntimeException("Failed to update job seeker profile", e);
        }
    }

    @Override
    public int getSeekerIdByUserId(int userId) {

        logger.info("Fetching seekerId for userId={}", userId);

        try {
            int seekerId = jobSeekerDAO.findSeekerIdByUserId(userId);

            if (seekerId == -1) {
                logger.warn("Job seeker profile not found | userId={}", userId);
                throw new RuntimeException("Job seeker profile not found");
            }

            logger.info("SeekerId retrieved | userId={}, seekerId={}", userId, seekerId);
            return seekerId;

        } catch (SQLException e) {
            logger.error("Error fetching seekerId | userId={}", userId, e);
            throw new RuntimeException("Error fetching seeker ID", e);
        }
    }

    @Override
    public void createJobSeeker(int userId) {

        logger.info("Creating default job seeker profile | userId={}", userId);

        createProfile(userId, null, null, null, 0);
    }
}
