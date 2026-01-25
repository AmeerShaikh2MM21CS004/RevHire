package com.rev.hire;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JobsDAO {

    public void addJob(int employerId, String title, String description, String skillsRequired, int experienceRequired, String educationRequired, String location, String salary, String jobType, Date deadline) {
        String sql = "INSERT INTO jobs (employer_id, title, description, skills_required, experience_required, education_required, location, salary, job_type, deadline) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, employerId);
            pstmt.setString(2, title);
            pstmt.setString(3, description);
            pstmt.setString(4, skillsRequired);
            pstmt.setInt(5, experienceRequired);
            pstmt.setString(6, educationRequired);
            pstmt.setString(7, location);
            pstmt.setString(8, salary);
            pstmt.setString(9, jobType);
            pstmt.setDate(10, deadline);

            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<String> getAllJobs() {
        List<String> jobs = new ArrayList<>();
        String sql = "SELECT job_id, employer_id, title, location, status FROM jobs";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                jobs.add(rs.getInt("job_id") + " | " + rs.getInt("employer_id") + " | " + rs.getString("title") + " | " + rs.getString("location") + " | " + rs.getString("status"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return jobs;
    }
}
