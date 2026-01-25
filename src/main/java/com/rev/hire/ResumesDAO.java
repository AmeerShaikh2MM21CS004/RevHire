package com.rev.hire;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ResumesDAO {

    public void addResume(int seekerId, String objective, String education, String experience, String skills, String projects, String certifications) {
        String sql = "INSERT INTO resumes (seeker_id, objective, education, experience, skills, projects, certifications) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, seekerId);
            pstmt.setString(2, objective);
            pstmt.setString(3, education);
            pstmt.setString(4, experience);
            pstmt.setString(5, skills);
            pstmt.setString(6, projects);
            pstmt.setString(7, certifications);

            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<String> getAllResumes() {
        List<String> resumes = new ArrayList<>();
        String sql = "SELECT resume_id, seeker_id, last_updated FROM resumes";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                resumes.add(rs.getInt("resume_id") + " | " + rs.getInt("seeker_id") + " | " + rs.getTimestamp("last_updated"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resumes;
    }

    public static void viewResume(int seekerId) {
        String sql = "SELECT * FROM resumes WHERE seeker_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, seekerId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                System.out.println("=== Resume ===");
                System.out.println("Objective: " + rs.getString("objective"));
                System.out.println("Education: " + rs.getString("education"));
                System.out.println("Experience: " + rs.getString("experience"));
                System.out.println("Skills: " + rs.getString("skills"));
                System.out.println("Projects: " + rs.getString("projects"));
                System.out.println("================");
            } else {
                System.out.println("⚠️ Resume not found.");
            }

        } catch (SQLException e) {
            System.out.println("⚠️ Error fetching resume: " + e.getMessage());
        }
    }

    public static void saveOrUpdateResume(
            int seekerId,
            String objective,
            String education,
            String experience,
            String skills,
            String projects
    ) {

        String sql = """
        MERGE INTO resumes r
        USING dual
        ON (r.seeker_id = ?)
        WHEN MATCHED THEN
          UPDATE SET
            objective = ?, education = ?, experience = ?,
            skills = ?, projects = ?, updated_at = CURRENT_TIMESTAMP
        WHEN NOT MATCHED THEN
          INSERT (seeker_id, objective, education, experience, skills, projects)
          VALUES (?, ?, ?, ?, ?, ?)
    """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, seekerId);
            ps.setString(2, objective);
            ps.setString(3, education);
            ps.setString(4, experience);
            ps.setString(5, skills);
            ps.setString(6, projects);

            ps.setInt(7, seekerId);
            ps.setString(8, objective);
            ps.setString(9, education);
            ps.setString(10, experience);
            ps.setString(11, skills);
            ps.setString(12, projects);

            ps.executeUpdate();
            System.out.println("📄 Resume saved successfully!");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
