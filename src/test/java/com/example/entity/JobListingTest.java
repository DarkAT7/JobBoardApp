package com.example.entity;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.math.BigDecimal;

public class JobListingTest {
    private JobListing jobListing;
    private final int COMPANY_ID = 1;
    private final String JOB_TITLE = "Software Engineer";
    private final String JOB_DESCRIPTION = "Java Developer position";
    private final String JOB_LOCATION = "New York";
    private final BigDecimal SALARY = new BigDecimal("100000.00");
    private final String JOB_TYPE = "Full-time";

    @Before
    public void setUp() {
        jobListing = new JobListing(COMPANY_ID, JOB_TITLE, JOB_DESCRIPTION, 
                                  JOB_LOCATION, SALARY, JOB_TYPE);
    }

    @Test
    public void testJobListingCreation() {
        assertNotNull("JobListing should not be null", jobListing);
        assertEquals("Company ID should match", COMPANY_ID, jobListing.getCompanyID());
        assertEquals("Job title should match", JOB_TITLE, jobListing.getJobTitle());
        assertEquals("Job description should match", JOB_DESCRIPTION, jobListing.getJobDescription());
        assertEquals("Job location should match", JOB_LOCATION, jobListing.getJobLocation());
        assertEquals("Salary should match", SALARY, jobListing.getSalary());
        assertEquals("Job type should match", JOB_TYPE, jobListing.getJobType());
        assertNotNull("Posted date should not be null", jobListing.getPostedDate());
    }

    @Test
    public void testToString() {
        String jobString = jobListing.toString();
        assertTrue("ToString should contain job title", jobString.contains(JOB_TITLE));
        assertTrue("ToString should contain location", jobString.contains(JOB_LOCATION));
        assertTrue("ToString should contain job type", jobString.contains(JOB_TYPE));
        assertTrue("ToString should contain salary", jobString.contains(SALARY.toString()));
    }
}
