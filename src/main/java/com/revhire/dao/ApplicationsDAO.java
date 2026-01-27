package com.revhire.dao;

import com.revhire.dao.impl.ApplicationsDAOimpl;
import com.revhire.model.Application;
import com.revhire.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ApplicationsDAO implements ApplicationsDAOimpl {

    public void applyJob(int jobId, int seekerId) {
        String sql = """
                        INSERT INTO applications (job_id, seeker_id, status, applied_date)
                        VALUES (?, ?, 'APPLIED', CURRENT_TIMESTAMP)
                    """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, jobId);
            ps.setInt(2, seekerId);
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean hasAlreadyApplied(int jobId, int seekerId) {
        String sql = """
            SELECT COUNT(*) FROM applications
            WHERE job_id = ? AND seeker_id = ?
        """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, jobId);
            ps.setInt(2, seekerId);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void updateStatus(int applicationId, String status, String reason) {
        String sql = """
        UPDATE applications
        SET status = ?,
            withdraw_reason = ?
        WHERE application_id = ?
    """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, status);

            // Optional reason
            if (reason != null && !reason.isBlank()) {
                ps.setString(2, reason);
            } else {
                ps.setNull(2, java.sql.Types.VARCHAR);
            }

            ps.setInt(3, applicationId);

            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public List<String> getApplicationsBySeeker(int seekerId) {
        List<String> list = new ArrayList<>();

        String sql = """
            SELECT *
            FROM applications a
            JOIN jobs j ON a.job_id = j.job_id
            WHERE a.seeker_id = ?
        """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, seekerId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                list.add(
                        rs.getInt("application_id") + " | " +
                                rs.getString("job_id") + " | " +
                                rs.getString("status") + " | " +
                                rs.getString("applied_date") + " | " +
                                rs.getString("withdraw_reason")
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<Application> fetchApplicationsByJobId(int jobId) throws SQLException {
        String sql = """
                        SELECT application_id, job_id, seeker_id, status, applied_date
                        FROM applications
                        WHERE job_id = ?
                    """;

        List<Application> list = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, jobId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                list.add(new Application(
                        rs.getInt("application_id"),
                        rs.getInt("job_id"),
                        rs.getInt("seeker_id"),
                        rs.getString("status"),
                        rs.getTimestamp("applied_date")
                ));
            }
        }

        return list;
    }

    public boolean updateStatusByApplicationId(int appId, String status) throws SQLException {
        String sql = "UPDATE applications SET status = ? WHERE application_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, status);
            ps.setInt(2, appId);

            return ps.executeUpdate() > 0;
        }
    }

    // Fetch job seeker user ID from application
    public int fetchSeekerUserIdByApplicationId(int appId) throws SQLException {
        String sql = "SELECT seeker_id FROM applications WHERE application_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, appId);
            ResultSet rs = ps.executeQuery();
            return rs.next() ? rs.getInt("seeker_id") : -1;
        }
    }

    // Fetch job ID from application
    public int fetchJobIdByApplicationId(int appId) throws SQLException {
        String sql = "SELECT job_id FROM applications WHERE application_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, appId);
            ResultSet rs = ps.executeQuery();
            return rs.next() ? rs.getInt("job_id") : -1;
        }
    }
}
