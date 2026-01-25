package com.rev.hire;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EmployersDAO {

    public void addEmployer(int userId, String companyName, String industry, int companySize, String description, String website, String location) {
        String sql = "INSERT INTO employers (user_id, company_name, industry, company_size, description, website, location) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, userId);
            pstmt.setString(2, companyName);
            pstmt.setString(3, industry);
            pstmt.setInt(4, companySize);
            pstmt.setString(5, description);
            pstmt.setString(6, website);
            pstmt.setString(7, location);

            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int getEmployerIdByUserId(int userId) {

        String sql = "SELECT employer_id FROM employers WHERE user_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt("employer_id");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        throw new RuntimeException("❌ Employer profile not found.");
    }

    public void updateCompanyProfile(
            int employerId,
            String industry,
            Integer companySize, String description,
            String website, String location) {

        StringBuilder sql = new StringBuilder("UPDATE employers SET ");
        List<Object> params = new ArrayList<>();

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
            System.out.println("⚠️ Nothing to update.");
            return;
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

            if (rows > 0) {
                System.out.println("✅ Company profile updated successfully.");
            } else {
                System.out.println("⚠️ Employer not found.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}

