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

    public List<String> getApplicantsForJob(int jobId, int employerId) {

        List<String> applicants = new ArrayList<>();

        String sql = """
        SELECT 
            a.application_id,
            s.seeker_id,
            s.full_name,
            s.phone,
            s.location,
            a.status,
            a.applied_date
        FROM applications a
        JOIN job_seekers s ON a.seeker_id = s.seeker_id
        JOIN jobs j ON a.job_id = j.job_id
        WHERE a.job_id = ?
          AND j.employer_id = ?
        ORDER BY a.applied_date DESC
    """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, jobId);
            ps.setInt(2, employerId);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                applicants.add(
                        "Application ID: " + rs.getInt("application_id") +
                                " | Seeker ID: " + rs.getInt("seeker_id") +
                                " | Name: " + rs.getString("full_name") +
                                " | Phone: " + rs.getString("phone") +
                                " | Location: " + rs.getString("location") +
                                " | Status: " + rs.getString("status") +
                                " | Applied On: " + rs.getTimestamp("applied_date")
                );
            }

            if (applicants.isEmpty()) {
                applicants.add("⚠️ No applications found for this job.");
            }

        } catch (SQLException e) {
            System.out.println("⚠️ Error fetching applicants: " + e.getMessage());
        }

        return applicants;
    }

    public int getUserIdByApplicationId(int appId) {

        String sql =
                "SELECT u.user_id " +
                        "FROM applications a " +
                        "JOIN job_seekers js ON a.seeker_id = js.seeker_id " +
                        "JOIN users u ON js.user_id = u.user_id " +
                        "WHERE a.application_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, appId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt("user_id");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }


    public void updateApplicationStatus(int appId, String status, int userId) {

        String sql =
                "UPDATE applications SET status = ? WHERE application_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, status);
            ps.setInt(2, appId);

            int rows = ps.executeUpdate();

            if (rows > 0) {

                new NotificationsDAO().addNotification(
                        userId,
                        "Your application #" + appId + " is now " + status
                );

                System.out.println("✅ Status updated successfully.");

            } else {
                System.out.println("❌ Invalid Application ID.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
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
