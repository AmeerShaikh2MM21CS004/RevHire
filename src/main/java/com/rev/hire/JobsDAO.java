package com.rev.hire;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JobsDAO {

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
            Date deadline
    ) {

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

            // ✅ AUTO-COMMIT (default)
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

            System.out.println("✅ Job posted successfully!");

        } catch (SQLException e) {
            System.out.println("❌ Error adding job: " + e.getMessage());
        }
    }

    // ---------------- VIEW ALL JOBS ----------------
    public List<String> getAllJobs() {
        List<String> jobs = new ArrayList<>();

        String sql = """
                    SELECT j.job_id, j.title, e.company_name,
                           j.location, j.salary, j.job_type, j.status
                    FROM jobs j
                    JOIN employers e ON j.employer_id = e.employer_id
                    WHERE NVL(j.status, 'OPEN') = 'OPEN'
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

        } catch (SQLException e) {
            System.out.println("⚠️ JDBC ERROR: " + e.getMessage());
        }

        return jobs;
    }

    // ---------------- VIEW ALL JOBS FOR LOGGED-IN EMPLOYER ----------------
    public List<String> getAllJobsOfEmployer(int employerId) {

        List<String> jobs = new ArrayList<>();

        String sql = """
                    SELECT j.job_id,
                           j.title,
                           j.location,
                           j.salary,
                           j.job_type,
                           j.status,
                           j.posted_date
                    FROM jobs j
                    WHERE j.employer_id = ?
                    ORDER BY j.posted_date DESC
                """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            // ✅ Bind logged-in employer ID
            ps.setInt(1, employerId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    jobs.add(
                            rs.getInt("job_id") + " | " +
                                    rs.getString("title") + " | " +
                                    rs.getString("location") + " | " +
                                    rs.getString("salary") + " | " +
                                    rs.getString("job_type") + " | " +
                                    rs.getString("status") + " | " +
                                    rs.getTimestamp("posted_date")
                    );
                }
            }

        } catch (SQLException e) {
            System.out.println("⚠️ JDBC ERROR (Employer Jobs): " + e.getMessage());
        }

        return jobs;
    }


    public List<String> searchJobs(
            String title,
            String location,
            Integer maxExp,
            String company,
            String salary,
            String type
    ) {

        List<String> jobs = new ArrayList<>();

        StringBuilder sql = new StringBuilder("""
                    SELECT j.job_id, j.title, e.company_name,
                           j.location, j.salary, j.job_type, j.status
                    FROM jobs j
                    JOIN employers e ON j.employer_id = e.employer_id
                    WHERE j.status = 'OPEN'
                """);

        List<String> titleWords = new ArrayList<>();

        if (title != null && !title.trim().isEmpty()) {
            String[] words = title.trim().toLowerCase().split("\\s+");
            sql.append(" AND (");
            for (int i = 0; i < words.length; i++) {
                if (i > 0) sql.append(" OR ");
                sql.append(" LOWER(j.title) LIKE ?");
                titleWords.add(words[i]);
            }
            sql.append(")");
        }

        if (location != null && !location.trim().isEmpty())
            sql.append(" AND LOWER(j.location) LIKE ?");

        if (maxExp != null)
            sql.append(" AND j.experience_required <= ?");

        if (company != null && !company.trim().isEmpty())
            sql.append(" AND LOWER(e.company_name) LIKE ?");

        if (salary != null && !salary.trim().isEmpty())
            sql.append(" AND j.salary LIKE ?");

        if (type != null && !type.trim().isEmpty())
            sql.append(" AND LOWER(j.job_type) LIKE ?");

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {

            int idx = 1;

            for (String word : titleWords)
                ps.setString(idx++, "%" + word + "%");

            if (location != null && !location.trim().isEmpty())
                ps.setString(idx++, "%" + location.toLowerCase() + "%");

            if (maxExp != null)
                ps.setInt(idx++, maxExp);

            if (company != null && !company.trim().isEmpty())
                ps.setString(idx++, "%" + company.toLowerCase() + "%");

            if (salary != null && !salary.trim().isEmpty())
                ps.setString(idx++, "%" + salary + "%");

            if (type != null && !type.trim().isEmpty())
                ps.setString(idx++, "%" + type.toLowerCase() + "%");

            ResultSet rs = ps.executeQuery();

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

        } catch (SQLException e) {
            System.out.println("⚠️ Error searching jobs: " + e.getMessage());
        }

        return jobs;
    }

    public void updateJob(int jobId, int employerId, String title, String salary) {

        String sql = """
                    UPDATE jobs
                    SET title = ?, salary = ?
                    WHERE job_id = ? AND employer_id = ?
                """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, title);
            ps.setString(2, salary);
            ps.setInt(3, jobId);
            ps.setInt(4, employerId);

            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateJobStatus(int jobId, int employerId, String status) {

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

            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteJob(int jobId, int employerId) {

        String sql = """
                    DELETE FROM jobs
                    WHERE job_id = ? AND employer_id = ?
                """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, jobId);
            ps.setInt(2, employerId);

            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateApplicationStatus(
            int appId, String status, String comment) {

        String sql = """
                    UPDATE applications
                    SET status = ?, comments = ?
                    WHERE application_id = ?
                """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, status);
            ps.setString(2, comment);
            ps.setInt(3, appId);

            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}