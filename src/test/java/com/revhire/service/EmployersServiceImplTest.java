package com.revhire.service;

import com.revhire.dao.impl.EmployersDAOImpl;
import com.revhire.service.impl.EmployersServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EmployersServiceImplTest {

    private EmployersDAOImpl employersDAOImpl;
    private EmployersServiceImpl employersServiceImpl;

    @BeforeEach
    void setup() {
        employersDAOImpl = Mockito.mock(EmployersDAOImpl.class);
        employersServiceImpl = new EmployersServiceImpl(employersDAOImpl);
    }

    // ---------------- getEmployerIdByUserId ----------------

    @Test
    void getEmployerIdByUserId_shouldReturnEmployerId() {
        // Arrange
        int userId = 10;
        when(employersDAOImpl.getEmployerIdByUserId(userId))
                .thenReturn(1001);

        // Act
        int result = employersServiceImpl.getEmployerIdByUserId(userId);

        // Assert
        assertEquals(1001, result);
    }

    @Test
    void getEmployerIdByUserId_shouldThrowExceptionWhenNotFound() {
        // Arrange
        int userId = 10;
        when(employersDAOImpl.getEmployerIdByUserId(userId))
                .thenReturn(null);

        // Act + Assert
        RuntimeException ex = assertThrows(
                RuntimeException.class,
                () -> employersServiceImpl.getEmployerIdByUserId(userId)
        );

        assertEquals("Employer profile not found", ex.getMessage());
    }

    // ---------------- updateCompanyProfile ----------------

    @Test
    void updateCompanyProfile_shouldLogSuccessWhenRowsUpdated() {
        // Arrange
        when(employersDAOImpl.updateCompanyProfile(
                anyInt(),
                anyString(),
                anyString(),
                anyInt(),
                anyString(),
                anyString(),
                anyString()
        )).thenReturn(1);

        // Act
        employersServiceImpl.updateCompanyProfile(
                1,
                "RevHire",
                "IT",
                100,
                "Hiring platform",
                "https://revhire.com",
                "India"
        );

        // Assert
        verify(employersDAOImpl).updateCompanyProfile(
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
        when(employersDAOImpl.updateCompanyProfile(
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
                employersServiceImpl.updateCompanyProfile(
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
        employersServiceImpl.createEmployer(5);

        // Assert
        verify(employersDAOImpl).addEmployer(
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
