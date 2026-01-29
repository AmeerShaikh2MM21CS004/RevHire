package com.revhire.service;

import com.revhire.dao.EmployersDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EmployersServiceTest {

    private EmployersDAO employersDAO;
    private EmployersService employersService;

    @BeforeEach
    void setup() {
        employersDAO = Mockito.mock(EmployersDAO.class);
        employersService = new EmployersService(employersDAO);
    }

    // ---------------- getEmployerIdByUserId ----------------

    @Test
    void getEmployerIdByUserId_shouldReturnEmployerId() {
        // Arrange
        int userId = 10;
        when(employersDAO.getEmployerIdByUserId(userId))
                .thenReturn(1001);

        // Act
        int result = employersService.getEmployerIdByUserId(userId);

        // Assert
        assertEquals(1001, result);
    }

    @Test
    void getEmployerIdByUserId_shouldThrowExceptionWhenNotFound() {
        // Arrange
        int userId = 10;
        when(employersDAO.getEmployerIdByUserId(userId))
                .thenReturn(null);

        // Act + Assert
        RuntimeException ex = assertThrows(
                RuntimeException.class,
                () -> employersService.getEmployerIdByUserId(userId)
        );

        assertEquals("Employer profile not found", ex.getMessage());
    }

    // ---------------- updateCompanyProfile ----------------

    @Test
    void updateCompanyProfile_shouldLogSuccessWhenRowsUpdated() {
        // Arrange
        when(employersDAO.updateCompanyProfile(
                anyInt(),
                anyString(),
                anyString(),
                anyInt(),
                anyString(),
                anyString(),
                anyString()
        )).thenReturn(1);

        // Act
        employersService.updateCompanyProfile(
                1,
                "RevHire",
                "IT",
                100,
                "Hiring platform",
                "https://revhire.com",
                "India"
        );

        // Assert
        verify(employersDAO).updateCompanyProfile(
                eq(1),
                eq("RevHire"),
                eq("IT"),
                eq(100),
                eq("Hiring platform"),
                eq("https://revhire.com"),
                eq("India")
        );
    }

    @Test
    void updateCompanyProfile_shouldHandleNoRowsUpdated() {
        // Arrange
        when(employersDAO.updateCompanyProfile(
                anyInt(),
                any(),
                any(),
                any(),
                any(),
                any(),
                any()
        )).thenReturn(0);

        // Act (should NOT throw)
        assertDoesNotThrow(() ->
                employersService.updateCompanyProfile(
                        1,
                        "RevHire",
                        "IT",
                        100,
                        "Hiring platform",
                        "https://revhire.com",
                        "India"
                )
        );
    }

    // ---------------- createEmployer ----------------

    @Test
    void createEmployer_shouldCreateEmployerWithDefaults() {
        // Act
        employersService.createEmployer(5);

        // Assert
        verify(employersDAO).addEmployer(
                eq(5),
                eq("Not Provided"),
                isNull(),
                eq(0),
                isNull(),
                isNull(),
                isNull()
        );
    }
}
