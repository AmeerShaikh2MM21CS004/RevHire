package com.revhire.model;

import org.junit.jupiter.api.Test;

import java.sql.Timestamp;

import static org.junit.jupiter.api.Assertions.*;

class ResumeTest {

    @Test
    void testFullConstructorAndGetters() {
        // Arrange
        Timestamp updatedAt = Timestamp.valueOf("2025-01-25 14:30:00");

        // Act
        Resume resume = new Resume(
                1,
                101,
                "To become a skilled Java developer",
                "B.E Computer Science",
                "3 years experience in backend development",
                "Java, Spring Boot, SQL",
                "RevHire Job Portal",
                updatedAt
        );

        // Assert
        assertEquals(1, resume.getResumeId());
        assertEquals(101, resume.getSeekerId());
        assertEquals("To become a skilled Java developer", resume.getObjective());
        assertEquals("B.E Computer Science", resume.getEducation());
        assertEquals("3 years experience in backend development", resume.getExperience());
        assertEquals("Java, Spring Boot, SQL", resume.getSkills());
        assertEquals("RevHire Job Portal", resume.getProjects());
        assertEquals(updatedAt, resume.getUpdatedAt());
    }

    @Test
    void testSettersAndGetters() {
        // Arrange
        Resume resume = new Resume(
                0, 0, null, null, null, null, null, null
        );

        Timestamp newTime = Timestamp.valueOf("2025-02-01 10:15:00");

        // Act
        resume.setResumeId(2);
        resume.setSeekerId(202);
        resume.setObjective("Backend Engineer");
        resume.setEducation("MCA");
        resume.setExperience("5 years experience");
        resume.setSkills("Java, Microservices");
        resume.setProjects("Enterprise HR System");
        resume.setUpdatedAt(newTime);

        // Assert
        assertEquals(2, resume.getResumeId());
        assertEquals(202, resume.getSeekerId());
        assertEquals("Backend Engineer", resume.getObjective());
        assertEquals("MCA", resume.getEducation());
        assertEquals("5 years experience", resume.getExperience());
        assertEquals("Java, Microservices", resume.getSkills());
        assertEquals("Enterprise HR System", resume.getProjects());
        assertEquals(newTime, resume.getUpdatedAt());
    }
}
