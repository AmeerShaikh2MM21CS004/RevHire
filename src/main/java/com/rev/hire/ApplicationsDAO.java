package com.rev.hire;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ApplicationsDAO {

    // Add a new job application (status default = APPLIED)
    public void addApplication(int jobId, int seekerId) {
        String sql = "INSERT INTO applications (job_id, seeker_id) VALUES (?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, jobId);
            pstmt.setInt(2, seekerId);

            pstmt.executeUpdate();
            System.out.println("✅ Application submitted successfully.");

        } catch (SQLException e) {

            // ORA-00001 → unique constraint (job_id, seeker_id)
            if (e.getErrorCode() == 1) {
                System.out.println("⚠️ You have already applied for this job.");
            } else {
                System.out.println("⚠️ Error adding application: " + e.getMessage());
            }
        }
    }

    // Get all applications
    public List<String> getAllApplications() {
        List<String> apps = new ArrayList<>();
        String sql = "SELECT application_id, job_id, seeker_id, status, applied_date FROM applications";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                apps.add(
                        rs.getInt("application_id") + " | " +
                                rs.getInt("job_id") + " | " +
                                rs.getInt("seeker_id") + " | " +
                                rs.getString("status") + " | " +
                                rs.getTimestamp("applied_date")
                );
            }
        } catch (SQLException e) {
            System.out.println("⚠️ Error fetching applications: " + e.getMessage());
        }
        return apps;
    }

    // Get application by ID
    public String getApplicationById(int appId) {
        String sql = "SELECT application_id, job_id, seeker_id, status, applied_date " +
                "FROM applications WHERE application_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, appId);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return
                            rs.getInt("application_id") + " | " +
                                    rs.getInt("job_id") + " | " +
                                    rs.getInt("seeker_id") + " | " +
                                    rs.getString("status") + " | " +
                                    rs.getTimestamp("applied_date");
                }
            }
        } catch (SQLException e) {
            System.out.println("⚠️ Error fetching application by ID: " + e.getMessage());
        }
        return null;
    }

    // Update application status
    public void updateApplicationStatus(int appId, String status) {
        String sql = "UPDATE applications SET status = ? WHERE application_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, status);
            pstmt.setInt(2, appId);

            int rows = pstmt.executeUpdate();
            if (rows > 0) {
                System.out.println("✅ Application status updated to " + status);
            } else {
                System.out.println("⚠️ Application ID not found.");
            }
        } catch (SQLException e) {
            System.out.println("⚠️ Error updating application status: " + e.getMessage());
        }
    }
}
