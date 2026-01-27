package com.revhire.dao;

import com.revhire.dao.impl.EmployersDAOimpl;
import com.revhire.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EmployersDAO implements EmployersDAOimpl {

    public void addEmployer(
            int userId,
            String companyName,
            String industry,
            int companySize,
            String description,
            String website,
            String location) {

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

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static Integer getEmployerIdByUserId(int userId) {

        String sql = "SELECT employer_id FROM employers WHERE user_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt("employer_id");
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public int updateCompanyProfile(
            int employerId,
            String company,
            String industry,
            Integer companySize,
            String description,
            String website,
            String location) {

        StringBuilder sql = new StringBuilder("UPDATE employers SET ");
        List<Object> params = new ArrayList<>();

        // ✅ FIXED: check company, not industry
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
            return 0;
        }

        // remove last comma
        sql.setLength(sql.length() - 2);
        sql.append(" WHERE employer_id = ?");
        params.add(employerId);

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {

            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }

            return ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
