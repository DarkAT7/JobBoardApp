package com.example.entity;

import java.sql.SQLException;
import com.example.exception.InvalidEmailException;

public class Applicant {
    private int applicantID;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;

    // Constructors
    public Applicant() {}

    public Applicant(String firstName, String lastName, String email, String phone) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
    }

    // Getters and Setters
    public int getApplicantID() { return applicantID; }
    public void setApplicantID(int applicantID) { this.applicantID = applicantID; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    // Required methods from problem statement
    public void createProfile(String email, String firstName, String lastName, String phone) 
            throws InvalidEmailException {
        // Validate email format
        if (!isValidEmail(email)) {
            throw new InvalidEmailException("Invalid email format");
        }
        
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
    }

    private boolean isValidEmail(String email) {
        return email != null && 
               email.matches("^[A-Za-z0-9+_.-]+@(.+)$") && 
               email.contains(".");
    }

    public void applyForJob(int jobID, String coverLetter) throws SQLException {
        // Implementation handled by DatabaseManager
    }

    @Override
    public String toString() {
        return String.format("Applicant: %s %s\nEmail: %s\nPhone: %s\n%s",
            firstName, lastName, email, phone, "-".repeat(30));
    }
}
