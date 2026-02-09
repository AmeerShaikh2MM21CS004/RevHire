package com.revhire.dao.impl;

import com.revhire.util.DBConnection;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.revhire.dao.JobSeekersDAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JobSeekersDAOImpl implements JobSeekersDAO {

    private static final Logger logger = LogManager.getLogger(JobSeekersDAOImpl.class);

    @Override
    public void insertJobSeeker(
            int userId,
            String fullName,
            String phone,
            String location,
            int totalExperience,
            char profileCompleted) throws SQLException {

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
            logger.info("Job seeker profile created for userId={}", userId);

        } catch (SQLException e) {
            logger.error("Error inserting job seeker for userId={}", userId, e);
            System.out.println();
            System.out.println("Error inserting job seeker");
            throw e;
        }
    }

    @Override
    public int findSeekerIdByUserId(int userId) throws SQLException {
        String sql = "SELECT seeker_id FROM job_seekers WHERE user_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                int seekerId = rs.getInt("seeker_id");
                logger.info("Found seekerId={} for userId={}", seekerId, userId);
                return seekerId;
            }

            logger.warn("No job seeker found for userId={}", userId);

        } catch (SQLException e) {
            logger.error("Error finding seekerId for userId={}", userId, e);
            throw e;
        }
        return -1;
    }

    @Override
    public int updateJobSeeker(
            int seekerId,
            String fullName,
            String phone,
            String location,
            Integer totalExperience) throws SQLException {

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
            logger.warn("No fields provided to update for seekerId={}", seekerId);
            System.out.println();
            System.out.println("No fields provided to update");
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

            int rows = ps.executeUpdate();
            logger.info("Updated job seeker seekerId={}, rowsAffected={}", seekerId, rows);
            System.out.println();
            System.out.println("Updated job seeker");
            return rows;

        } catch (SQLException e) {
            logger.error("Error updating job seeker seekerId={}", seekerId, e);
            System.out.println();
            System.out.println("Error updating job seeker");
            throw e;
        }
    }

    @Override
    public List<Integer> findMatchingSeekerUserIds(
            String skills,
            Integer experience,
            String location
    ) throws SQLException {

        String sql = """
        SELECT DISTINCT js.user_id
        FROM job_seekers js
        JOIN resumes r ON js.seeker_id = r.seeker_id
        WHERE LOWER(r.skills) LIKE ?
          AND js.total_experience >= ?
          AND LOWER(js.location) LIKE ?
    """;

        List<Integer> userIds = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, "%" + skills.toLowerCase() + "%");
            ps.setInt(2, experience);
            ps.setString(3, "%" + location.toLowerCase() + "%");

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    userIds.add(rs.getInt("user_id"));
                }
            }

            logger.info("Matched {} job seekers for notification", userIds.size());
        }

        return userIds;
    }



}
