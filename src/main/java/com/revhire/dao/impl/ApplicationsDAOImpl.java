package com.revhire.dao.impl;

import com.revhire.dao.ApplicationsDAO;
import com.revhire.model.Application;
import com.revhire.util.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ApplicationsDAOImpl implements ApplicationsDAO {

  private static final Logger logger = LogManager.getLogger(ApplicationsDAOImpl.class);

  // ---------------- APPLY JOB ----------------
  public void applyJob(int jobId, int seekerId) {

    logger.info("Applying job | jobId={}, seekerId={}", jobId, seekerId);

    String sql =
        """
                INSERT INTO applications (job_id, seeker_id, status, applied_date)
                VALUES (?, ?, 'APPLIED', CURRENT_TIMESTAMP)
                """;

    try (Connection conn = DBConnection.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql)) {

      ps.setInt(1, jobId);
      ps.setInt(2, seekerId);
      ps.executeUpdate();

      logger.info("Job applied successfully");
      System.out.println();
      System.out.println("Job application submitted successfully !!");

    } catch (SQLException e) {
      logger.error("Error while applying job", e);
      System.out.println();
      System.out.println("Something Went Wrong!!");
    }
  }

  // ---------------- CHECK ALREADY APPLIED ----------------
  public boolean hasAlreadyApplied(int jobId, int seekerId) {

    logger.debug("Checking application | jobId={}, seekerId={}", jobId, seekerId);

    String sql = """
        SELECT COUNT(*)
        FROM applications
        WHERE job_id = ? AND seeker_id = ?
        """;

    try (Connection conn = DBConnection.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {

      ps.setInt(1, jobId);
      ps.setInt(2, seekerId);

      try (ResultSet rs = ps.executeQuery()) {
        if (rs.next()) {
          boolean exists = rs.getInt(1) > 0;
          logger.info(
                  "Application exists check | jobId={}, seekerId={}, exists={}",
                  jobId, seekerId, exists
          );
          return exists;
        }
      }

    } catch (SQLException e) {
      logger.error("Error checking application existence | jobId={}, seekerId={}",
              jobId, seekerId, e);
    }

    return false;
  }

  // ---------------- UPDATE STATUS WITH REASON ----------------
  public void updateStatus(int applicationId, String status, String reason) {

    logger.info("Updating application status | appId={}, status={}", applicationId, status);

    String sql =
        """
                UPDATE applications
                SET status = ?,
                    withdraw_reason = ?
                WHERE application_id = ?
                """;

    try (Connection conn = DBConnection.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql)) {

      ps.setString(1, status);

      if (reason != null && !reason.isBlank()) {
        ps.setString(2, reason);
      } else {
        ps.setNull(2, Types.VARCHAR);
      }

      ps.setInt(3, applicationId);
      ps.executeUpdate();

      logger.info("Application status updated successfully");
      System.out.println();
      System.out.println("Application status updated successfully!!!");

    } catch (SQLException e) {
      logger.error("Error updating application status", e);
      System.out.println();
      System.out.println("Something Went Wrong!!");
    }
  }

  // ---------------- GET APPLICATIONS BY SEEKER ----------------
  public List<Application> getApplicationsBySeeker(int seekerId) {
    logger.info("Fetching applications for seekerId={}", seekerId);
    List<Application> list = new ArrayList<>();

    // Corrected SQL: changed applied_at to applied_date
    String sql = """
            SELECT a.application_id, a.job_id, a.seeker_id, a.status, a.applied_date 
            FROM applications a
            JOIN jobs j ON a.job_id = j.job_id
            WHERE a.seeker_id = ?
            """;

    try (Connection conn = DBConnection.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {

      ps.setInt(1, seekerId);
      try (ResultSet rs = ps.executeQuery()) {
        while (rs.next()) {
          Application app = new Application(
                  rs.getInt("application_id"),
                  rs.getInt("job_id"),
                  rs.getInt("seeker_id"),
                  rs.getString("status"),
                  rs.getTimestamp("applied_date") // Updated to match your DB column
          );
          list.add(app);
        }
      }
      logger.info("Total applications fetched: {}", list.size());

    } catch (SQLException e) {
      logger.error("Error fetching applications by seeker", e);
      System.out.println("\n[!] Database Error: " + e.getMessage());
    }
    return list;
  }

  // ---------------- FETCH APPLICATIONS BY JOB ----------------
  public List<Application> fetchApplicationsByJobId(int jobId) throws SQLException {

    logger.info("Fetching applications for jobId={}", jobId);

    String sql =
        """
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
        list.add(
            new Application(
                rs.getInt("application_id"),
                rs.getInt("job_id"),
                rs.getInt("seeker_id"),
                rs.getString("status"),
                rs.getTimestamp("applied_date")));
      }

      logger.info("Applications fetched: {}", list.size());
    }

    return list;
  }

  // ---------------- UPDATE STATUS BY APP ID ----------------
  public boolean updateStatusByApplicationId(int appId, String status) throws SQLException {

    logger.info("Updating status | appId={}, status={}", appId, status);

    String sql = "UPDATE applications SET status = ? WHERE application_id = ?";

    try (Connection conn = DBConnection.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql)) {

      ps.setString(1, status);
      ps.setInt(2, appId);

      boolean updated = ps.executeUpdate() > 0;
      logger.info("Status update success: {}", updated);
      System.out.println();
      System.out.println("Status update success!!");
      return updated;
    }
  }

  // ---------------- FETCH SEEKER ID ----------------
  public int fetchSeekerUserIdByApplicationId(int appId) throws SQLException {

    logger.debug("Fetching seekerId for appId={}", appId);

    String sql = "SELECT seeker_id FROM applications WHERE application_id = ?";

    try (Connection conn = DBConnection.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql)) {

      ps.setInt(1, appId);
      ResultSet rs = ps.executeQuery();
      return rs.next() ? rs.getInt("seeker_id") : -1;
    }
  }

  // ---------------- FETCH JOB ID ----------------
  public int fetchJobIdByApplicationId(int appId) throws SQLException {

    logger.debug("Fetching jobId for appId={}", appId);

    String sql = "SELECT job_id FROM applications WHERE application_id = ?";

    try (Connection conn = DBConnection.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql)) {

      ps.setInt(1, appId);
      ResultSet rs = ps.executeQuery();
      return rs.next() ? rs.getInt("job_id") : -1;
    }
  }
}
