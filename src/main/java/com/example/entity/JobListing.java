package com.example.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.sql.SQLException;

public class JobListing {
    private int jobID;
    private int companyID;
    private String jobTitle;
    private String jobDescription;
    private String jobLocation;
    private BigDecimal salary;
    private String jobType;
    private LocalDateTime postedDate;
    private String companyName; // For join queries

    // Constructors
    public JobListing() {}

    public JobListing(int companyID, String jobTitle, String jobDescription, 
                     String jobLocation, BigDecimal salary, String jobType) {
        this.companyID = companyID;
        this.jobTitle = jobTitle;
        this.jobDescription = jobDescription;
        this.jobLocation = jobLocation;
        this.salary = salary;
        this.jobType = jobType;
        this.postedDate = LocalDateTime.now();
    }

    // Getters and Setters
    public int getJobID() { return jobID; }
    public void setJobID(int jobID) { this.jobID = jobID; }

    public int getCompanyID() { return companyID; }
    public void setCompanyID(int companyID) { this.companyID = companyID; }

    public String getJobTitle() { return jobTitle; }
    public void setJobTitle(String jobTitle) { this.jobTitle = jobTitle; }

    public String getJobDescription() { return jobDescription; }
    public void setJobDescription(String jobDescription) { this.jobDescription = jobDescription; }

    public String getJobLocation() { return jobLocation; }
    public void setJobLocation(String jobLocation) { this.jobLocation = jobLocation; }

    public BigDecimal getSalary() { return salary; }
    public void setSalary(BigDecimal salary) { this.salary = salary; }

    public String getJobType() { return jobType; }
    public void setJobType(String jobType) { this.jobType = jobType; }

    public LocalDateTime getPostedDate() { return postedDate; }
    public void setPostedDate(LocalDateTime postedDate) { this.postedDate = postedDate; }

    public String getCompanyName() { return companyName; }
    public void setCompanyName(String companyName) { this.companyName = companyName; }

    // Required methods from problem statement
    public void apply(int applicantID, String coverLetter) throws SQLException {
        // Implementation handled by DatabaseManager
    }

    public List<Applicant> getApplicants() throws SQLException {
        // Implementation handled by DatabaseManager
        return null;
    }

    @Override
    public String toString() {
        return String.format("Job: %s at %s\nLocation: %s\nType: %s\nSalary: $%s\nPosted: %s\n%s",
            jobTitle, companyName != null ? companyName : "Company #" + companyID,
            jobLocation, jobType, salary, postedDate,
            "-".repeat(50));
    }
}
