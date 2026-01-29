package com.revhire.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EmployerTest {

    @Test
    void testEmployerGettersAndSetters() {
        // Arrange
        Employer employer = new Employer();

        // Act
        employer.setEmployerId(1);
        employer.setUserId(1001);
        employer.setCompanyName("RevHire Pvt Ltd");
        employer.setIndustry("IT Services");
        employer.setCompanySize(250);
        employer.setDescription("Recruitment platform for job seekers");
        employer.setWebsite("https://revhire.com");
        employer.setLocation("Bangalore");
        employer.setProfileCompletion(85);

        // Assert
        assertEquals(1, employer.getEmployerId());
        assertEquals(1001, employer.getUserId());
        assertEquals("RevHire Pvt Ltd", employer.getCompanyName());
        assertEquals("IT Services", employer.getIndustry());
        assertEquals(250, employer.getCompanySize());
        assertEquals("Recruitment platform for job seekers", employer.getDescription());
        assertEquals("https://revhire.com", employer.getWebsite());
        assertEquals("Bangalore", employer.getLocation());
        assertEquals(85, employer.getProfileCompletion());
    }
}
