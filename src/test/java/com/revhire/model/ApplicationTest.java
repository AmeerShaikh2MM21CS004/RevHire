package com.revhire.model;

import org.junit.jupiter.api.Test;

import java.sql.Timestamp;

import static org.junit.jupiter.api.Assertions.*;

class ApplicationTest {

    @Test
    void testApplicationConstructorAndGetters() {
        // Arrange
        int applicationId = 1;
        int jobId = 101;
        int seekerId = 501;
        String status = "WITHDRAWN";
        Timestamp appliedAt = Timestamp.valueOf("2025-01-20 10:30:00");
        String withdrawReason = "Found another opportunity"; // Defined properly

        // Act
        Application application = new Application(
                applicationId,
                jobId,
                seekerId,
                status,
                appliedAt,
                withdrawReason
        );

        // Assert
        assertEquals(applicationId, application.getApplicationId());
        assertEquals(jobId, application.getJobId());
        assertEquals(seekerId, application.getSeekerId());
        assertEquals(status, application.getStatus());
        assertEquals(appliedAt, application.getAppliedAt());
        assertEquals(withdrawReason, application.getWithdrawReason()); // Corrected getter
    }

    @Test
    void testToStringMethod() {
        // Arrange
        Timestamp appliedAt = Timestamp.valueOf("2025-01-20 10:30:00");
        String reason = "Found a better offer"; // Hardcoded string for the test

        Application application = new Application(
                1,
                101,
                501,
                "APPLIED",
                appliedAt,
                reason);

        // Act
        String result = application.toString();

        // Assert
        assertTrue(result.contains("Application ID: 1"), "Should contain Application ID");
        assertTrue(result.contains("Job ID: 101"), "Should contain Job ID");
        assertTrue(result.contains("Seeker ID: 501"), "Should contain Seeker ID");
        assertTrue(result.contains("Status: APPLIED"), "Should contain Status");
        assertTrue(result.contains(appliedAt.toString()), "Should contain the timestamp");
        assertTrue(result.contains(reason), "Should contain the withdrawal reason");
    }
}
