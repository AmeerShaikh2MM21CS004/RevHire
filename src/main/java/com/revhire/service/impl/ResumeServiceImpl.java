package com.revhire.service.impl;

import com.revhire.dao.impl.ResumesDAOImpl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.SQLException;

public class ResumeServiceImpl implements com.revhire.service.ResumeService {

    private static final Logger logger = LogManager.getLogger(ResumeServiceImpl.class);

    private final ResumesDAOImpl resumesDAOImpl;

    // Default constructor
    public ResumeServiceImpl() {
        this.resumesDAOImpl = new ResumesDAOImpl();
    }

    // Constructor for unit testing
    public ResumeServiceImpl(ResumesDAOImpl resumesDAOImpl) {
        this.resumesDAOImpl = resumesDAOImpl;
    }

    // ---------------- SAVE OR UPDATE RESUME ----------------
    @Override
    public void saveOrUpdateResume(
            int seekerId,
            String objective,
            String education,
            String experience,
            String skills,
            String projects
    ) {
        logger.info("Saving/updating resume | seekerId={}", seekerId);

        try {
            resumesDAOImpl.upsertResume(seekerId, objective, education, experience, skills, projects);
            logger.info("✅ Resume saved successfully | seekerId={}", seekerId);

        } catch (SQLException e) {
            logger.error("❌ Failed to save resume | seekerId={}", seekerId, e);
            throw new RuntimeException("Failed to save resume for seekerId=" + seekerId, e);
        }
    }
}
