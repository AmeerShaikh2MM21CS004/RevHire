package com.rev.hire;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JobSeekersDAO {

    public void addJobSeeker(int userId, String fullName, String phone, String location, int totalExperience, char profileCompleted) {
        String sql = "INSERT INTO job_seekers (user_id, full_name, phone, location, total_experience, profile_completed) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, userId);
            pstmt.setString(2, fullName);
            pstmt.setString(3, phone);
            pstmt.setString(4, location);
            pstmt.setInt(5, totalExperience);
            pstmt.setString(6, String.valueOf(profileCompleted));

            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<String> getAllJobSeekers() {
        List<String> seekers = new ArrayList<>();
        String sql = "SELECT seeker_id, user_id, full_name, phone, location FROM job_seekers";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                seekers.add(rs.getInt("seeker_id") + " | " + rs.getInt("user_id") + " | " + rs.getString("full_name") + " | " + rs.getString("phone") + " | " + rs.getString("location"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return seekers;
    }
}

