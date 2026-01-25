package com.rev.hire;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ApplicationsDAO {

    // Add a new job application (status default = APPLIED)
    public void addApplication(int jobId, int seekerId) {

        String checkSql = """
        SELECT COUNT(*) 
        FROM applications 
        WHERE job_id = ? AND seeker_id = ?
    """;

        String insertSql = """
        INSERT INTO applications (job_id, seeker_id)
        VALUES (?, ?)
    """;

        try (Connection conn = DBConnection.getConnection()) {

            // 1️⃣ Check duplicate
            try (PreparedStatement checkPs = conn.prepareStatement(checkSql)) {
                checkPs.setInt(1, jobId);
                checkPs.setInt(2, seekerId);

                ResultSet rs = checkPs.executeQuery();
                if (rs.next() && rs.getInt(1) > 0) {
                    System.out.println("⚠️ You have already applied for this job.");
                    return;
                }
            }

            // 2️⃣ Insert application
            try (PreparedStatement insertPs = conn.prepareStatement(insertSql)) {
                insertPs.setInt(1, jobId);
                insertPs.setInt(2, seekerId);
                insertPs.executeUpdate();
            }

            System.out.println("✅ Application submitted successfully!");

        } catch (SQLException e) {
            System.out.println("❌ Failed to apply: " + e.getMessage());
        }
    }

    public List<String> getApplicationsBySeeker(int seekerId) {

        List<String> applications = new ArrayList<>();

        String sql = """
        SELECT 
            a.application_id,
            j.title,
            e.company_name,
            j.location,
            a.status,
            a.applied_date
        FROM applications a
        JOIN jobs j ON a.job_id = j.job_id
        JOIN employers e ON j.employer_id = e.employer_id
        WHERE a.seeker_id = ?
        ORDER BY a.applied_date DESC
    """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, seekerId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                String record =
                        "Application ID: " + rs.getInt("application_id") +
                                " | Job: " + rs.getString("title") +
                                " | Company: " + rs.getString("company_name") +
                                " | Location: " + rs.getString("location") +
                                " | Status: " + rs.getString("status") +
                                " | Applied On: " + rs.getTimestamp("applied_date");

                applications.add(record);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return applications;
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

    public void withdrawApplication(int applicationId, String reason) {

        String sql = """
        UPDATE applications
        SET status = 'WITHDRAWN',
            withdraw_reason = ?
        WHERE application_id = ?
    """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, reason);
            ps.setInt(2, applicationId);
            ps.executeUpdate();

            System.out.println("✅ Application withdrawn successfully.");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
