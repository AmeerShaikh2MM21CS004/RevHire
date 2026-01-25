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

    public String getResumeById(int resumeId) {
        String sql = "SELECT * FROM resumes WHERE resume_id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, resumeId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("resume_id") + " | " + rs.getInt("seeker_id") + " | " +
                            rs.getString("objective") + " | " + rs.getString("education") +
                            " | " + rs.getString("experience");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
