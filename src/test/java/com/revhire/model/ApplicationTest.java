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
        String status = "APPLIED";
        Timestamp appliedAt = Timestamp.valueOf("2025-01-20 10:30:00");

        // Act
        Application application = new Application(
                applicationId,
                jobId,
                seekerId,
                status,
                appliedAt
        );

        // Assert
        assertEquals(applicationId, application.getApplicationId());
        assertEquals(jobId, application.getJobId());
        assertEquals(seekerId, application.getSeekerId());
        assertEquals(status, application.getStatus());
        assertEquals(appliedAt, application.getAppliedAt());
    }

    @Test
    void testToStringMethod() {
        // Arrange
        Timestamp appliedAt = Timestamp.valueOf("2025-01-20 10:30:00");

        Application application = new Application(
                1,
                101,
                501,
                "APPLIED",
                appliedAt
        );

        // Act
        String result = application.toString();

        // Assert
        assertTrue(result.contains("Application ID: 1"));
        assertTrue(result.contains("Job ID: 101"));
        assertTrue(result.contains("Seeker ID: 501"));
        assertTrue(result.contains("Status: APPLIED"));
        assertTrue(result.contains(appliedAt.toString()));
    }
}
