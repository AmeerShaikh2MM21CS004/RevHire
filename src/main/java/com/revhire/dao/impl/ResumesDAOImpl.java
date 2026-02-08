package com.revhire.dao.impl;

import com.revhire.dao.ResumesDAO;
import com.revhire.model.Resume;
import com.revhire.util.DBConnection;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ResumesDAOImpl implements ResumesDAO {

    private static final Logger logger =  LogManager.getLogger(ResumesDAOImpl.class);

    @Override
    public void upsertResume(
            int seekerId,
            String objective,
            String education,
            String experience,
            String skills,
            String projects
    ) throws SQLException {

        String sql = """
            MERGE INTO resumes r
            USING dual
            ON (r.seeker_id = ?)
            WHEN MATCHED THEN
              UPDATE SET
                objective = ?,
                education = ?,
                experience = ?,
                skills = ?,
                projects = ?
            WHEN NOT MATCHED THEN
              INSERT (seeker_id, objective, education, experience, skills, projects)
              VALUES (?, ?, ?, ?, ?, ?)
        """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            // MATCH condition
            ps.setInt(1, seekerId);

            // UPDATE
            ps.setString(2, objective);
            ps.setString(3, education);
            ps.setString(4, experience);
            ps.setString(5, skills);
            ps.setString(6, projects);

            // INSERT
            ps.setInt(7, seekerId);
            ps.setString(8, objective);
            ps.setString(9, education);
            ps.setString(10, experience);
            ps.setString(11, skills);
            ps.setString(12, projects);

            int rows = ps.executeUpdate();

            logger.info("Resume upsert completed for seekerId={}, rowsAffected={}",
                    seekerId, rows);
            System.out.println();
            System.out.println("Resume upsert completed");

        } catch (SQLException e) {
            logger.error("Error upserting resume for seekerId={}", seekerId, e);
            System.out.println();
            System.out.println("Error upserting resume");
            throw e;
        }
    }

    @Override
    public Resume fetchResumeBySeekerId(int seekerId) throws SQLException {

        String sql = "SELECT * FROM resumes WHERE seeker_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, seekerId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                logger.info("Resume found for seekerId={}", seekerId);

                return new Resume(
                        rs.getInt("resume_id"),
                        rs.getInt("seeker_id"),
                        rs.getString("objective"),
                        rs.getString("education"),
                        rs.getString("experience"),
                        rs.getString("skills"),
                        rs.getString("projects"),
                        rs.getTimestamp("last_updated")
                );
            }

            logger.warn("No resume found for seekerId={}", seekerId);

        } catch (SQLException e) {
            logger.error("Error fetching resume for seekerId={}", seekerId, e);
            throw e;
        }

        return null;
    }

    @Override
    public List<Resume> fetchAllResumes() throws SQLException {

        List<Resume> list = new ArrayList<>();
        String sql = "SELECT resume_id, seeker_id, last_updated FROM resumes";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(new Resume(
                        rs.getInt("resume_id"),
                        rs.getInt("seeker_id"),
                        null, null, null, null, null,
                        rs.getTimestamp("last_updated")
                ));
            }

            logger.info("Fetched {} resumes (summary view)", list.size());

        } catch (SQLException e) {
            logger.error("Error fetching all resumes", e);
            throw e;
        }

        return list;
    }
}
