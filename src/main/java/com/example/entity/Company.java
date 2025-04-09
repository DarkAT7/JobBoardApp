package com.example.entity;

import java.math.BigDecimal;
import java.util.List;
import java.sql.SQLException;

public class Company {
    private int companyID;
    private String companyName;
    private String location;

    // Constructors
    public Company() {}

    public Company(String companyName, String location) {
        this.companyName = companyName;
        this.location = location;
    }

    // Getters and Setters
    public int getCompanyID() { return companyID; }
    public void setCompanyID(int companyID) { this.companyID = companyID; }

    public String getCompanyName() { return companyName; }
    public void setCompanyName(String companyName) { this.companyName = companyName; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    // Required methods from problem statement
    public JobListing postJob(String jobTitle, String jobDescription, String jobLocation, 
                            BigDecimal salary, String jobType) throws SQLException {
        JobListing job = new JobListing(this.companyID, jobTitle, jobDescription, 
                                       jobLocation, salary, jobType);
        // Actual insertion handled by DatabaseManager
        return job;
    }

    public List<JobListing> getJobs() throws SQLException {
        // Implementation handled by DatabaseManager
        return null;
    }

    @Override
    public String toString() {
        return String.format("Company: %s\nLocation: %s\n%s",
            companyName, location, "-".repeat(30));
    }
}
