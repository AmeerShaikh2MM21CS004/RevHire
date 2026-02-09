package com.revhire.service;

import com.revhire.dao.impl.EmployersDAOImpl;
import com.revhire.service.impl.EmployersServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmployersServiceImplTest {

    @Mock
    private EmployersDAOImpl employersDAOImpl;

    @InjectMocks
    private EmployersServiceImpl employersServiceImpl;

    // ---------------- getEmployerIdByUserId ----------------

    @Test
    void getEmployerIdByUserId_shouldReturnEmployerId() {
        when(employersDAOImpl.getEmployerIdByUserId(10))
                .thenReturn(1001);

        int result =
                employersServiceImpl.getEmployerIdByUserId(10);

        assertEquals(1001, result);
    }

    @Test
    void getEmployerIdByUserId_shouldThrowExceptionWhenNotFound() {
        when(employersDAOImpl.getEmployerIdByUserId(10))
                .thenReturn(null);

        RuntimeException ex = assertThrows(
                RuntimeException.class,
                () -> employersServiceImpl.getEmployerIdByUserId(10)
        );

        assertEquals("Employer profile not found", ex.getMessage());
    }

    // ---------------- updateCompanyProfile ----------------

    @Test
    void updateCompanyProfile_shouldLogSuccessWhenRowsUpdated() {
        when(employersDAOImpl.updateCompanyProfile(
                anyInt(),
                anyString(),
                anyString(),
                anyInt(),
                anyString(),
                anyString(),
                anyString()
        )).thenReturn(1);

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
        when(employersDAOImpl.updateCompanyProfile(
                anyInt(),
                anyString(),
                anyString(),
                anyInt(),
                anyString(),
                anyString(),
                anyString()
        )).thenReturn(0);

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
        employersServiceImpl.createEmployer(5);

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
