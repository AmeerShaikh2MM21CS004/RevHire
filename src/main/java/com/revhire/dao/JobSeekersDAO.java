package com.revhire.dao;

import com.revhire.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JobSeekersDAO {

    public void insertJobSeeker(int userId, String fullName, String phone,
                                String location, int totalExperience, char profileCompleted)
            throws SQLException {

        String sql = """
            INSERT INTO job_seekers
            (user_id, full_name, phone, location, total_experience, profile_completed)
            VALUES (?, ?, ?, ?, ?, ?)
        """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ps.setString(2, fullName);
            ps.setString(3, phone);
            ps.setString(4, location);
            ps.setInt(5, totalExperience);
            ps.setString(6, String.valueOf(profileCompleted));

            ps.executeUpdate();
        }
    }

    public int findSeekerIdByUserId(int userId) throws SQLException {
        String sql = "SELECT seeker_id FROM job_seekers WHERE user_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt("seeker_id");
            }
        }
        return -1;
    }

    public int updateJobSeeker(int seekerId, String fullName, String phone,
                               String location, Integer totalExperience)
            throws SQLException {

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
            return 0;
        }

        sql.setLength(sql.length() - 2);
        sql.append(" WHERE seeker_id = ?");
        params.add(seekerId);

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {

            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }
            return ps.executeUpdate();
        }
    }
}
