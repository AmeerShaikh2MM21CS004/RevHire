package com.revhire.service;

import com.revhire.dao.ResumesDAO;
import com.revhire.service.impl.ResumeServiceimpl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.SQLException;

public class ResumeService implements ResumeServiceimpl {

    private static final Logger logger = LogManager.getLogger(ResumeService.class);

    private final ResumesDAO resumesDAO;

    // Default constructor
    public ResumeService() {
        this.resumesDAO = new ResumesDAO();
    }

    // Constructor for unit testing
    public ResumeService(ResumesDAO resumesDAO) {
        this.resumesDAO = resumesDAO;
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
            resumesDAO.upsertResume(seekerId, objective, education, experience, skills, projects);
            logger.info("✅ Resume saved successfully | seekerId={}", seekerId);

        } catch (SQLException e) {
            logger.error("❌ Failed to save resume | seekerId={}", seekerId, e);
            throw new RuntimeException("Failed to save resume for seekerId=" + seekerId, e);
        }
    }
}
