package com.revhire.dao.impl;

import com.revhire.util.DBConnection;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JobsDAOImpl implements com.revhire.dao.JobsDAO {

    private static final Logger logger = LogManager.getLogger(JobsDAOImpl.class);

    public void addJob(
            int employerId,
            String title,
            String description,
            String skillsRequired,
            int experienceRequired,
            String educationRequired,
            String location,
            String salary,
            String jobType,
            Date deadline) {

        String sql = """
            INSERT INTO jobs (
                employer_id, title, description, skills_required,
                experience_required, education_required,
                location, salary, job_type, deadline, status
            )
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, 'OPEN')
        """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, employerId);
            ps.setString(2, title);
            ps.setString(3, description);
            ps.setString(4, skillsRequired);
            ps.setInt(5, experienceRequired);
            ps.setString(6, educationRequired);
            ps.setString(7, location);
            ps.setString(8, salary);
            ps.setString(9, jobType);
            ps.setDate(10, deadline);

            ps.executeUpdate();
            logger.info("Job added | employerId={}, title={}", employerId, title);
            System.out.println();
            System.out.println("New Job added!!");

        } catch (SQLException e) {
            logger.error("Failed to add job | employerId={}, title={}, error={}", employerId, title, e.getMessage());
            System.out.println();
            System.out.println("Failed to add job!!");
            throw new RuntimeException(e);
        }
    }

    public List<String> getAllOpenJobs() {
        List<String> jobs = new ArrayList<>();
        String sql = """
            SELECT j.job_id, j.title, e.company_name,
                   j.location, j.salary, j.job_type, j.status
            FROM jobs j
            JOIN employers e ON j.employer_id = e.employer_id
            WHERE j.status = 'OPEN'
        """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                jobs.add(
                        rs.getInt("job_id") + " | " +
                                rs.getString("title") + " | " +
                                rs.getString("company_name") + " | " +
                                rs.getString("location") + " | " +
                                rs.getString("salary") + " | " +
                                rs.getString("job_type") + " | " +
                                rs.getString("status")
                );
            }
            logger.info("Fetched all open jobs | count={}", jobs.size());

        } catch (SQLException e) {
            logger.error("Failed to fetch open jobs | error={}", e.getMessage());
            throw new RuntimeException(e);
        }
        return jobs;
    }

    public List<String> getJobsByEmployer(int employerId) {
        List<String> jobs = new ArrayList<>();
        String sql = """
            SELECT job_id, title, description, skills_required,
                                       experience_required, education_required,
                                       location, salary, job_type, deadline, status
                                FROM jobs
                                WHERE employer_id = ?
        """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, employerId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                jobs.add(
                        rs.getInt("job_id") + " | " +
                                rs.getString("title") + " | " +
                                rs.getString("description") + " | " +
                                rs.getString("skills_required") + " | " +
                                rs.getInt("experience_required") + " | " +
                                rs.getString("education_required") + " | " +
                                rs.getString("location") + " | " +
                                rs.getString("salary") + " | " +
                                rs.getString("job_type") + " | " +
                                rs.getDate("deadline") + " | " +
                                rs.getString("status")
                );
            }
            logger.info("Fetched jobs by employer | employerId={}, count={}", employerId, jobs.size());

        } catch (SQLException e) {
            logger.error("Failed to fetch jobs by employer | employerId={}, error={}", employerId, e.getMessage());
            throw new RuntimeException(e);
        }
        return jobs;
    }

    public List<String> searchJobs(
            String title,
            String location,
            Integer maxExp,
            String company,
            String salary,
            String type) {

        List<String> jobs = new ArrayList<>();
        StringBuilder sql = new StringBuilder("""
            SELECT j.job_id, j.title, e.company_name,
                   j.location, j.salary, j.job_type, j.status
            FROM jobs j
            JOIN employers e ON j.employer_id = e.employer_id
            WHERE j.status = 'OPEN'
        """);

        List<Object> params = new ArrayList<>();

        if (title != null && !title.isBlank()) {
            sql.append(" AND LOWER(j.title) LIKE ?");
            params.add("%" + title.toLowerCase() + "%");
        }
        if (location != null && !location.isBlank()) {
            sql.append(" AND LOWER(j.location) LIKE ?");
            params.add("%" + location.toLowerCase() + "%");
        }
        if (maxExp != null) {
            sql.append(" AND j.experience_required <= ?");
            params.add(maxExp);
        }
        if (company != null && !company.isBlank()) {
            sql.append(" AND LOWER(e.company_name) LIKE ?");
            params.add("%" + company.toLowerCase() + "%");
        }
        if (salary != null && !salary.isBlank()) {
            sql.append(" AND j.salary LIKE ?");
            params.add("%" + salary + "%");
        }
        if (type != null && !type.isBlank()) {
            sql.append(" AND LOWER(j.job_type) LIKE ?");
            params.add("%" + type.toLowerCase() + "%");
        }

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {

            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                jobs.add(
                        rs.getInt("job_id") + " | " +
                                rs.getString("title") + " | " +
                                rs.getString("company_name") + " | " +
                                rs.getString("location") + " | " +
                                rs.getString("salary") + " | " +
                                rs.getString("job_type")
                );
            }
            logger.info("Searched jobs | filters=[title={}, location={}, maxExp={}, company={}, salary={}, type={}] results={}",
                    title, location, maxExp, company, salary, type, jobs.size());

        } catch (SQLException e) {
            logger.error("Failed to search jobs | error={}", e.getMessage());
            throw new RuntimeException(e);
        }
        return jobs;
    }

    public int updateJob(
            int jobId,
            int employerId,
            String title,
            String description,
            String skills,
            Integer experience,
            String education,
            String location,
            String salary,
            String jobType,
            Date deadline) {

        StringBuilder sql = new StringBuilder("UPDATE jobs SET ");
        List<Object> params = new ArrayList<>();

        if (title != null) { sql.append("title=?, "); params.add(title); }
        if (description != null) { sql.append("description=?, "); params.add(description); }
        if (skills != null) { sql.append("skills_required=?, "); params.add(skills); }
        if (experience != null) { sql.append("experience_required=?, "); params.add(experience); }
        if (education != null) { sql.append("education_required=?, "); params.add(education); }
        if (location != null) { sql.append("location=?, "); params.add(location); }
        if (salary != null) { sql.append("salary=?, "); params.add(salary); }
        if (jobType != null) { sql.append("job_type=?, "); params.add(jobType); }
        if (deadline != null) { sql.append("deadline=?, "); params.add(deadline); }

        if (params.isEmpty()) return 0;

        sql.setLength(sql.length() - 2);
        sql.append(" WHERE job_id=? AND employer_id=?");
        params.add(jobId);
        params.add(employerId);

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {

            for (int i = 0; i < params.size(); i++)
                ps.setObject(i + 1, params.get(i));

            int updated = ps.executeUpdate();
            logger.info("Job updated | employerId={}, jobId={}, fieldsUpdated={}", employerId, jobId, params.size() - 2);
            System.out.println();
            System.out.println("Job updated!!");
            return updated;

        } catch (SQLException e) {
            logger.error("Failed to update job | employerId={}, jobId={}, error={}", employerId, jobId, e.getMessage());
            System.out.println();
            System.out.println("Failed to update job");
            throw new RuntimeException(e);
        }
    }

    public int updateJobStatus(int jobId, int employerId, String status) {
        String sql = """
        UPDATE jobs
        SET status = ?
        WHERE job_id = ? AND employer_id = ?
        """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, status);
            ps.setInt(2, jobId);
            ps.setInt(3, employerId);

            int rows = ps.executeUpdate();
            logger.info("Job status updated | employerId={}, jobId={}, status={}", employerId, jobId, status);
            System.out.println();
            System.out.println("Job Status updated!!");
            return rows;

        } catch (SQLException e) {
            logger.error("Failed to update job status | employerId={}, jobId={}, error={}", employerId, jobId, e.getMessage());
            System.out.println();
            System.out.println("Failed to update job status!!");
            throw new RuntimeException(e);
        }
    }

    public void deleteJob(int jobId, int employerId) {
        String deleteApps = "DELETE FROM applications WHERE job_id=?";
        String deleteJob  = "DELETE FROM jobs WHERE job_id=? AND employer_id=?";

        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false);

            try (PreparedStatement ps1 = conn.prepareStatement(deleteApps);
                 PreparedStatement ps2 = conn.prepareStatement(deleteJob)) {

                ps1.setInt(1, jobId);
                ps1.executeUpdate();

                ps2.setInt(1, jobId);
                ps2.setInt(2, employerId);
                int rows = ps2.executeUpdate();

                if (rows == 0) {
                    throw new RuntimeException("Job not found or unauthorized");
                }

                conn.commit();
                logger.info("Job deleted | employerId={}, jobId={}", employerId, jobId);
                System.out.println();
                System.out.println("Job deleted");
            }
        } catch (SQLException e) {
            logger.error("Failed to delete job | employerId={}, jobId={}, error={}", employerId, jobId, e.getMessage());
            System.out.println();
            System.out.println("Failed to delete job");
            throw new RuntimeException("Cannot delete job with applications", e);
        }
    }

    public int fetchEmployerUserIdByJob(int jobId) {
        String sql = """
        SELECT u.user_id
        FROM jobs j
        JOIN employers e ON j.employer_id = e.employer_id
        JOIN users u ON e.user_id = u.user_id
        WHERE j.job_id = ?
        """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, jobId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                int userId = rs.getInt("user_id");
                logger.info("Fetched employer userId | jobId={}, userId={}", jobId, userId);
                return userId;
            }

        } catch (SQLException e) {
            logger.error("Failed to fetch employer userId | jobId={}, error={}", jobId, e.getMessage());
        }

        return -1;
    }

}