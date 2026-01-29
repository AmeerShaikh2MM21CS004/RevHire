package com.revhire.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class JobSeekerTest {

    @Test
    void testJobSeekerGettersAndSetters() {
        // Arrange
        JobSeeker jobSeeker = new JobSeeker();

        // Act
        jobSeeker.setSeekerId(10);
        jobSeeker.setUserId(2001);
        jobSeeker.setFullName("Ameer Shaikh");
        jobSeeker.setPhone("9876543210");
        jobSeeker.setLocation("Bangalore");
        jobSeeker.setTotalExperience(3);
        jobSeeker.setProfileCompleted('Y');
        jobSeeker.setProfileCompletion(90);

        // Assert
        assertEquals(10, jobSeeker.getSeekerId());
        assertEquals(2001, jobSeeker.getUserId());
        assertEquals("Ameer Shaikh", jobSeeker.getFullName());
        assertEquals("9876543210", jobSeeker.getPhone());
        assertEquals("Bangalore", jobSeeker.getLocation());
        assertEquals(3, jobSeeker.getTotalExperience());
        assertEquals('Y', jobSeeker.getProfileCompleted());
        assertEquals(90, jobSeeker.getProfileCompletion());
    }
}
