package com.example.entity;

import java.time.LocalDateTime;

public class JobApplication {
    private int applicationID;
    private int jobID;
    private int applicantID;
    private LocalDateTime applicationDate;
    private String coverLetter;
    private String status;

    // Additional fields for join queries
    private String jobTitle;
    private String applicantName;
    private String companyName;

    // Constructors
    public JobApplication() {}

    public JobApplication(int jobID, int applicantID, String coverLetter) {
        this.jobID = jobID;
        this.applicantID = applicantID;
        this.coverLetter = coverLetter;
        this.applicationDate = LocalDateTime.now();
        this.status = "PENDING";
    }

    // Getters and Setters
    public int getApplicationID() { return applicationID; }
    public void setApplicationID(int applicationID) { this.applicationID = applicationID; }

    public int getJobID() { return jobID; }
    public void setJobID(int jobID) { this.jobID = jobID; }

    public int getApplicantID() { return applicantID; }
    public void setApplicantID(int applicantID) { this.applicantID = applicantID; }

    public LocalDateTime getApplicationDate() { return applicationDate; }
    public void setApplicationDate(LocalDateTime applicationDate) { this.applicationDate = applicationDate; }

    public String getCoverLetter() { return coverLetter; }
    public void setCoverLetter(String coverLetter) { this.coverLetter = coverLetter; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getJobTitle() { return jobTitle; }
    public void setJobTitle(String jobTitle) { this.jobTitle = jobTitle; }

    public String getApplicantName() { return applicantName; }
    public void setApplicantName(String applicantName) { this.applicantName = applicantName; }

    public String getCompanyName() { return companyName; }
    public void setCompanyName(String companyName) { this.companyName = companyName; }

    @Override
    public String toString() {
        return String.format(
            "Application ID: %d\n" +
            "Job: %s at %s\n" +
            "Applicant: %s\n" +
            "Status: %s\n" +
            "Applied: %s\n" +
            "%s",
            applicationID,
            jobTitle != null ? jobTitle : "Job #" + jobID,
            companyName != null ? companyName : "Company",
            applicantName != null ? applicantName : "Applicant #" + applicantID,
            status,
            applicationDate,
            "-".repeat(50));
    }
}
