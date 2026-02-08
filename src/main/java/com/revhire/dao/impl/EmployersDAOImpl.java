package com.revhire.dao.impl;

import com.revhire.util.DBConnection;

import com.revhire.dao.EmployersDAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class EmployersDAOImpl implements EmployersDAO {

    private static final Logger logger =  LogManager.getLogger(EmployersDAOImpl.class);

    // ---------------- ADD EMPLOYER ----------------
    public void addEmployer(
            int userId,
            String companyName,
            String industry,
            int companySize,
            String description,
            String website,
            String location) {

        logger.info("Adding employer | userId={}, company={}", userId, companyName);

        String sql = """
                INSERT INTO employers
                (user_id, company_name, industry, company_size, description, website, location)
                VALUES (?, ?, ?, ?, ?, ?, ?)
                """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ps.setString(2, companyName);
            ps.setString(3, industry);
            ps.setInt(4, companySize);
            ps.setString(5, description);
            ps.setString(6, website);
            ps.setString(7, location);

            ps.executeUpdate();
            logger.info("Employer added successfully");

        } catch (SQLException e) {
            logger.error("Error adding employer", e);
            throw new RuntimeException("Failed to add employer", e);
        }
    }

    // ---------------- GET EMPLOYER ID BY USER ID ----------------
    public Integer getEmployerIdByUserId(int userId) {

        Logger staticLogger = LogManager.getLogger(EmployersDAOImpl.class);
        staticLogger.debug("Fetching employerId for userId={}", userId);

        String sql = "SELECT employer_id FROM employers WHERE user_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Integer employerId = rs.getInt("employer_id");
                staticLogger.info("EmployerId found: {}", employerId);
                return employerId;
            }

        } catch (SQLException e) {
            staticLogger.error("Error fetching employerId", e);
            throw new RuntimeException("Failed to fetch employerId", e);
        }
        return null;
    }

    // ---------------- UPDATE COMPANY PROFILE ----------------
    public int updateCompanyProfile(
            int employerId,
            String company,
            String industry,
            Integer companySize,
            String description,
            String website,
            String location) {

        logger.info("Updating company profile | employerId={}", employerId);

        StringBuilder sql = new StringBuilder("UPDATE employers SET ");
        List<Object> params = new ArrayList<>();

        if (company != null && !company.isBlank()) {
            sql.append("company_name = ?, ");
            params.add(company);
        }

        if (industry != null && !industry.isBlank()) {
            sql.append("industry = ?, ");
            params.add(industry);
        }

        if (companySize != null) {
            sql.append("company_size = ?, ");
            params.add(companySize);
        }

        if (description != null && !description.isBlank()) {
            sql.append("description = ?, ");
            params.add(description);
        }

        if (website != null && !website.isBlank()) {
            sql.append("website = ?, ");
            params.add(website);
        }

        if (location != null && !location.isBlank()) {
            sql.append("location = ?, ");
            params.add(location);
        }

        if (params.isEmpty()) {
            logger.warn("No fields provided for update | employerId={}", employerId);
            System.out.println();
            System.out.println("No fields provided for update!!");
            return 0;
        }

        sql.setLength(sql.length() - 2); // remove last comma
        sql.append(" WHERE employer_id = ?");
        params.add(employerId);

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {

            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }

            int rows = ps.executeUpdate();
            logger.info("Company profile updated | rowsAffected={}", rows);
            System.out.println();
            System.out.println("Company profile updated!!");
            return rows;

        } catch (SQLException e) {
            logger.error("Error updating company profile", e);
            throw new RuntimeException("Failed to update company profile", e);
        }
    }
}
