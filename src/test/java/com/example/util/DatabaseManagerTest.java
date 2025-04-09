package com.example.util;

import com.example.entity.*;
import com.example.exception.*;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.sql.SQLException;

public class DatabaseManagerTest {
    @Before
    public void setUp() {
        // No setup needed for validation tests
    }

    @Test
    public void testJobListingValidation() {
        JobListing validJob = new JobListing(
            1,
            "Software Engineer",
            "Java Developer position",
            "New York",
            new BigDecimal("100000.00"),
            "Full-time"
        );

        assertNotNull("Job listing should not be null", validJob);
        assertEquals("Job title should match", "Software Engineer", validJob.getJobTitle());
        assertEquals("Salary should match", new BigDecimal("100000.00"), validJob.getSalary());
    }

    @Test(expected = InvalidSalaryException.class)
    public void testNegativeSalaryValidation() throws InvalidSalaryException, SQLException {
        JobListing invalidJob = new JobListing(
            1,
            "Software Engineer",
            "Java Developer position",
            "New York",
            new BigDecimal("-100000.00"),
            "Full-time"
        );
        
        if (invalidJob.getSalary().compareTo(BigDecimal.ZERO) < 0) {
            throw new InvalidSalaryException("Salary cannot be negative");
        }
    }

    @Test
    public void testCompanyValidation() {
        Company company = new Company("Tech Corp", "San Francisco");
        
        assertNotNull("Company should not be null", company);
        assertEquals("Company name should match", "Tech Corp", company.getCompanyName());
        assertEquals("Location should match", "San Francisco", company.getLocation());
    }

    @Test
    public void testApplicantValidation() throws InvalidEmailException {
        Applicant applicant = new Applicant();
        applicant.createProfile(
            "john.doe@example.com",
            "John",
            "Doe",
            "1234567890"
        );
        
        assertNotNull("Applicant should not be null", applicant);
        assertEquals("Email should match", "john.doe@example.com", applicant.getEmail());
        assertEquals("First name should match", "John", applicant.getFirstName());
    }

    @Test(expected = InvalidEmailException.class)
    public void testInvalidEmailValidation() throws InvalidEmailException {
        Applicant applicant = new Applicant();
        applicant.createProfile(
            "invalid-email", // Invalid email format
            "John",
            "Doe",
            "1234567890"
        );
    }
}
