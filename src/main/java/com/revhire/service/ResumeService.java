package com.revhire.service;

import com.revhire.dao.ResumesDAO;
import com.revhire.service.impl.ResumeServiceimpl;

import java.sql.SQLException;

public class ResumeService implements ResumeServiceimpl {

    private final ResumesDAO resumesDAO = new ResumesDAO();

    public void saveOrUpdateResume(
            int seekerId,
            String objective,
            String education,
            String experience,
            String skills,
            String projects
    ) {
        try {
            resumesDAO.upsertResume(
                    seekerId, objective, education,
                    experience, skills, projects
            );
            System.out.println("✅ Resume saved successfully.");
        } catch (SQLException e) {
            e.printStackTrace(); // 👈 keep this for debugging
            System.out.println("❌ Failed to save resume.");
        }
    }


}
