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

    public int getSeekerIdByUserId(int userId) {
        String sql = "SELECT seeker_id FROM job_seekers WHERE user_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getInt("seeker_id");
            }
        } catch (SQLException e) {
            System.out.println("⚠️ Error fetching seeker ID: " + e.getMessage());
        }
        return -1;
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

    public static void updateJobSeekerProfile(
            int seekerId,
            String fullName,
            String phone,
            String location,
            Integer totalExperience) {

        StringBuilder sql = new StringBuilder("UPDATE job_seekers SET ");
        List<Object> params = new ArrayList<>();

        if (fullName != null && !fullName.isBlank()) {
            sql.append("full_name = ?, ");
            params.add(fullName);
        }

        if (phone != null && !phone.isBlank()) {
            sql.append("phone = ?, ");
            params.add(phone);
        }

        if (location != null && !location.isBlank()) {
            sql.append("location = ?, ");
            params.add(location);
        }

        if (totalExperience != null) {
            sql.append("total_experience = ?, ");
            params.add(totalExperience);
        }

        if (params.isEmpty()) {
            System.out.println("⚠️ Nothing to update.");
            return;
        }

        // Remove last comma
        sql.setLength(sql.length() - 2);

        sql.append(" WHERE seeker_id = ?");
        params.add(seekerId);

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {

            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }

            int rows = ps.executeUpdate();

            if (rows > 0) {
                System.out.println("✅ Job seeker profile updated successfully.");
            } else {
                System.out.println("⚠️ Job seeker not found.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int getUserIdBySeekerId(int seekerId) {

        String sql = "SELECT user_id FROM job_seekers WHERE seeker_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, seekerId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt("user_id");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return -1; // not found
    }



}

