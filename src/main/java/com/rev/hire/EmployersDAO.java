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
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getInt("employer_id");
            }
        } catch (SQLException e) {
            System.out.println("⚠️ Error fetching employer ID: " + e.getMessage());
        }
        return -1;
    }


    public List<String> getAllEmployers() {
        List<String> employers = new ArrayList<>();
        String sql = "SELECT employer_id, user_id, company_name, industry, location FROM employers";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                employers.add(rs.getInt("employer_id") + " | " + rs.getInt("user_id") + " | " + rs.getString("company_name") + " | " + rs.getString("industry") + " | " + rs.getString("location"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return employers;
    }

    public void updateApplicationStatus(int appId, String status, int userId) {

        String sql = "UPDATE applications SET status=? WHERE application_id=?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, status);
            ps.setInt(2, appId);
            ps.executeUpdate();

            new NotificationsDAO()
                    .addNotification(userId,
                            "Your application #" + appId + " is now " + status);

            System.out.println("✅ Status updated.");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}

