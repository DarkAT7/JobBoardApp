package com.example.entity;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.sql.SQLException;

public class CompanyTest {
    private Company company;
    private final String COMPANY_NAME = "Tech Corp";
    private final String LOCATION = "San Francisco";

    @Before
    public void setUp() {
        company = new Company(COMPANY_NAME, LOCATION);
    }

    @Test
    public void testCompanyCreation() {
        assertNotNull("Company should not be null", company);
        assertEquals("Company name should match", COMPANY_NAME, company.getCompanyName());
        assertEquals("Location should match", LOCATION, company.getLocation());
    }

    @Test
    public void testPostJob() throws SQLException {
        String jobTitle = "Software Engineer";
        String jobDescription = "Java Developer position";
        String jobLocation = "San Francisco";
        BigDecimal salary = new BigDecimal("100000.00");
        String jobType = "Full-time";

        JobListing job = company.postJob(jobTitle, jobDescription, jobLocation, salary, jobType);

        assertNotNull("Job listing should not be null", job);
        assertEquals("Company ID should match", company.getCompanyID(), job.getCompanyID());
        assertEquals("Job title should match", jobTitle, job.getJobTitle());
        assertEquals("Job description should match", jobDescription, job.getJobDescription());
        assertEquals("Job location should match", jobLocation, job.getJobLocation());
        assertEquals("Salary should match", salary, job.getSalary());
        assertEquals("Job type should match", jobType, job.getJobType());
    }

    @Test
    public void testToString() {
        String companyString = company.toString();
        assertTrue("ToString should contain company name", companyString.contains(COMPANY_NAME));
        assertTrue("ToString should contain location", companyString.contains(LOCATION));
    }
}
