package com.revhire.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ProfileUtil {

    private static final Logger logger = LogManager.getLogger(ProfileUtil.class);

    // ---------------- JOB SEEKER PROFILE COMPLETION ----------------
    public static int calculateJobSeekerCompletion(int seekerId) {
        final int TOTAL_FIELDS = 9;
        int filled = 0;

        logger.info("Calculating job seeker profile completion | seekerId={}", seekerId);

        try (Connection conn = DBConnection.getConnection()) {

            // ---------------- JOB SEEKER PROFILE ----------------
            String profileSql =
                    "SELECT full_name, phone, location, total_experience " +
                            "FROM job_seekers WHERE seeker_id = ?";

            try (PreparedStatement ps1 = conn.prepareStatement(profileSql)) {
                ps1.setInt(1, seekerId);

                try (ResultSet rs1 = ps1.executeQuery()) {
                    if (!rs1.next()) {
                        logger.warn("No job seeker found | seekerId={}", seekerId);
                        return 0;
                    }

                    if (rs1.getString("full_name") != null && !rs1.getString("full_name").trim().isEmpty()) filled++;
                    if (rs1.getString("phone") != null && !rs1.getString("phone").trim().isEmpty()) filled++;
                    if (rs1.getString("location") != null && !rs1.getString("location").trim().isEmpty()) filled++;
                    if (rs1.getObject("total_experience") != null) filled++;
                }
            }

            // ---------------- RESUME (OPTIONAL ROW) ----------------
            String resumeSql =
                    "SELECT objective, education, experience, skills, projects " +
                            "FROM resumes WHERE seeker_id = ?";

            try (PreparedStatement ps2 = conn.prepareStatement(resumeSql)) {
                ps2.setInt(1, seekerId);

                try (ResultSet rs2 = ps2.executeQuery()) {
                    if (rs2.next()) {
                        if (rs2.getString("objective") != null && !rs2.getString("objective").trim().isEmpty()) filled++;
                        if (rs2.getString("education") != null && !rs2.getString("education").trim().isEmpty()) filled++;
                        if (rs2.getString("experience") != null && !rs2.getString("experience").trim().isEmpty()) filled++;
                        if (rs2.getString("skills") != null && !rs2.getString("skills").trim().isEmpty()) filled++;
                        if (rs2.getString("projects") != null && !rs2.getString("projects").trim().isEmpty()) filled++;
                    }
                }
            }

            int percentage = (filled * 100) / TOTAL_FIELDS;
            logger.info("Profile completion calculated | seekerId={}, percentage={}", seekerId, percentage);

            // ---------------- UPDATE PROFILE COMPLETION ----------------
            String updateSql =
                    "UPDATE job_seekers " +
                            "SET profile_completion = ?, " +
                            "profile_completed = CASE WHEN ? = 100 THEN 'Y' ELSE 'N' END " +
                            "WHERE seeker_id = ?";

            try (PreparedStatement ups = conn.prepareStatement(updateSql)) {
                ups.setInt(1, percentage);
                ups.setInt(2, percentage);
                ups.setInt(3, seekerId);
                int rowsUpdated = ups.executeUpdate();
                logger.info("Profile completion updated | seekerId={}, rowsAffected={}", seekerId, rowsUpdated);
            }

            return percentage;

        } catch (SQLException e) {
            logger.error("Failed to calculate/update job seeker profile completion | seekerId={}", seekerId, e);
            return 0;
        }
    }

    // ---------------- EMPLOYER PROFILE COMPLETION ----------------
    public static int calculateEmployerCompletion(int employerId) {
        int totalFields = 6; // company_name, industry, size, description, website, location
        int filled = 0;

        logger.info("Calculating employer profile completion | employerId={}", employerId);

        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT company_name, industry, company_size, description, website, location FROM employers WHERE employer_id = ?";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, employerId);

                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        if (rs.getString("company_name") != null && !rs.getString("company_name").isBlank()) filled++;
                        if (rs.getString("industry") != null && !rs.getString("industry").isBlank()) filled++;
                        if (rs.getInt("company_size") > 0) filled++;
                        if (rs.getString("description") != null && !rs.getString("description").isBlank()) filled++;
                        if (rs.getString("website") != null && !rs.getString("website").isBlank()) filled++;
                        if (rs.getString("location") != null && !rs.getString("location").isBlank()) filled++;
                    }
                }
            }

            int percentage = (filled * 100) / totalFields;
            logger.info("Employer profile completion calculated | employerId={}, percentage={}", employerId, percentage);
            return percentage;

        } catch (SQLException e) {
            logger.error("Failed to calculate employer profile completion | employerId={}", employerId, e);
            return 0;
        }
    }
}
