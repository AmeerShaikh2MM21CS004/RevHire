package com.revhire.model;

import org.junit.jupiter.api.Test;

import java.sql.Date;
import java.sql.Timestamp;

import static org.junit.jupiter.api.Assertions.*;

class JobTest {

    @Test
    void testJobGettersAndSetters() {
        // Arrange
        Job job = new Job();

        Date deadline = Date.valueOf("2025-12-31");
        Timestamp postedDate = Timestamp.valueOf("2025-01-15 09:45:00");

        // Act
        job.setJobId(101);
        job.setEmployerId(5001);
        job.setTitle("Java Backend Developer");
        job.setDescription("Responsible for backend development");
        job.setSkillsRequired("Java, Spring Boot, SQL");
        job.setExperienceRequired(3);
        job.setEducationRequired("B.E / B.Tech");
        job.setLocation("Bangalore");
        job.setSalary("8-12 LPA");
        job.setJobType("Full-Time");
        job.setDeadline(deadline);
        job.setStatus("OPEN");
        job.setPostedDate(postedDate);

        // Assert
        assertEquals(101, job.getJobId());
        assertEquals(5001, job.getEmployerId());
        assertEquals("Java Backend Developer", job.getTitle());
        assertEquals("Responsible for backend development", job.getDescription());
        assertEquals("Java, Spring Boot, SQL", job.getSkillsRequired());
        assertEquals(3, job.getExperienceRequired());
        assertEquals("B.E / B.Tech", job.getEducationRequired());
        assertEquals("Bangalore", job.getLocation());
        assertEquals("8-12 LPA", job.getSalary());
        assertEquals("Full-Time", job.getJobType());
        assertEquals(deadline, job.getDeadline());
        assertEquals("OPEN", job.getStatus());
        assertEquals(postedDate, job.getPostedDate());
    }
}
