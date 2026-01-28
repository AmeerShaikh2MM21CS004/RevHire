package com.revhire.dao;

import com.revhire.model.Application;
import com.revhire.util.DBConnection;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ApplicationsDAOTest {

    private static ApplicationsDAO applicationsDAO;

    // ⚠️ Make sure these IDs exist in your DB
    private static final int TEST_JOB_ID = 1;
    private static final int TEST_SEEKER_ID = 2;

    @BeforeAll
    static void setup() {
        applicationsDAO = new ApplicationsDAO();
    }

    // -------------------- APPLY JOB --------------------
    @Test
    @Order(1)
    void testApplyJob() {
        applicationsDAO.applyJob(TEST_JOB_ID, TEST_SEEKER_ID);

        boolean applied =
                applicationsDAO.hasAlreadyApplied(TEST_JOB_ID, TEST_SEEKER_ID);

        assertTrue(applied, "Application should be created successfully");
    }

    // -------------------- HAS ALREADY APPLIED (FALSE) --------------------
    @Test
    @Order(2)
    void testHasAlreadyAppliedFalse() {
        boolean applied =
                applicationsDAO.hasAlreadyApplied(9999, 9999);

        assertFalse(applied, "Should return false for invalid job/seeker");
    }

    // -------------------- GET APPLICATIONS BY SEEKER --------------------
    @Test
    @Order(3)
    void testGetApplicationsBySeeker() {
        List<String> applications =
                applicationsDAO.getApplicationsBySeeker(TEST_SEEKER_ID);

        assertNotNull(applications, "Applications list should not be null");
    }

    // -------------------- FETCH APPLICATIONS BY JOB --------------------
    @Test
    @Order(4)
    void testFetchApplicationsByJobId() throws SQLException {
        List<Application> applications =
                applicationsDAO.fetchApplicationsByJobId(TEST_JOB_ID);

        assertNotNull(applications);

        if (!applications.isEmpty()) {
            Application app = applications.get(0);
            assertEquals(TEST_JOB_ID, app.getJobId());
        }
    }

    // -------------------- UPDATE STATUS --------------------
    @Test
    @Order(5)
    void testUpdateStatusByApplicationId() throws SQLException {
        int appId = getLatestApplicationId();

        boolean updated =
                applicationsDAO.updateStatusByApplicationId(appId, "SHORTLISTED");

        assertTrue(updated, "Application status should be updated");
    }

    // -------------------- FETCH SEEKER ID --------------------
    @Test
    @Order(6)
    void testFetchSeekerUserIdByApplicationId() throws SQLException {
        int appId = getLatestApplicationId();

        int seekerId =
                applicationsDAO.fetchSeekerUserIdByApplicationId(appId);

        assertEquals(TEST_SEEKER_ID, seekerId);
    }

    // -------------------- FETCH JOB ID --------------------
    @Test
    @Order(7)
    void testFetchJobIdByApplicationId() throws SQLException {
        int appId = getLatestApplicationId();

        int jobId =
                applicationsDAO.fetchJobIdByApplicationId(appId);

        assertEquals(TEST_JOB_ID, jobId);
    }

    // -------------------- CLEANUP --------------------
    @AfterAll
    static void cleanup() throws SQLException {
        String sql = """
            DELETE FROM applications
            WHERE job_id = ? AND seeker_id = ?
        """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, TEST_JOB_ID);
            ps.setInt(2, TEST_SEEKER_ID);
            ps.executeUpdate();
        }
    }

    // -------------------- HELPER METHOD --------------------
    private int getLatestApplicationId() throws SQLException {
        String sql = """
            SELECT application_id
            FROM applications
            WHERE job_id = ? AND seeker_id = ?
            ORDER BY application_id DESC
            FETCH FIRST 1 ROWS ONLY
        """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, TEST_JOB_ID);
            ps.setInt(2, TEST_SEEKER_ID);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("application_id");
            }
        }
        throw new SQLException("No application found for test data");
    }
}
