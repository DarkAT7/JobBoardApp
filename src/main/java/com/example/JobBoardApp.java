package com.example;

import com.example.entity.*;
import com.example.exception.*;
import com.example.util.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class JobBoardApp {
    private static final Scanner scanner = new Scanner(System.in);
    private static DatabaseManager dbManager;

    public static void main(String[] args) {
        try {
            // Initialize database connection
            String propertyFile = "src/main/resources/database.properties";
            String connectionString = DBPropertyUtil.getConnectionString(propertyFile);
            dbManager = new DatabaseManager(connectionString);

            boolean exit = false;
            while (!exit) {
                displayMainMenu();
                int choice = getIntInput("Enter your choice: ");

                switch (choice) {
                    case 1:
                        viewAllJobListings();
                        break;
                    case 2:
                        createApplicantProfile();
                        break;
                    case 3:
                        submitJobApplication();
                        break;
                    case 4:
                        postNewJob();
                        break;
                    case 5:
                        searchJobsBySalaryRange();
                        break;
                    case 6:
                        exit = true;
                        break;
                    default:
                        System.out.println("Invalid choice! Please try again.");
                        break;
                }
            }
        } catch (IOException | DatabaseConnectionException e) {
            System.err.println("Error initializing application: " + e.getMessage());
        } finally {
            scanner.close();
        }
    }

    private static void displayMainMenu() {
        System.out.println("\n=== Job Board System ===");
        System.out.println("1. View All Job Listings");
        System.out.println("2. Create Applicant Profile");
        System.out.println("3. Submit Job Application");
        System.out.println("4. Post New Job (Company)");
        System.out.println("5. Search Jobs by Salary Range");
        System.out.println("6. Exit");
    }

    private static void viewAllJobListings() {
        try {
            List<JobListing> jobs = dbManager.getJobListings();
            if (jobs.isEmpty()) {
                System.out.println("No job listings found.");
                return;
            }

            System.out.println("\n=== Current Job Listings ===");
            jobs.forEach(System.out::println);
        } catch (SQLException e) {
            System.err.println("Error retrieving job listings: " + e.getMessage());
        }
    }

    private static void createApplicantProfile() {
        try {
            System.out.println("\n=== Create Applicant Profile ===");
            String firstName = getStringInput("Enter first name: ");
            String lastName = getStringInput("Enter last name: ");
            String email = getStringInput("Enter email: ");
            String phone = getStringInput("Enter phone number: ");

            Applicant applicant = new Applicant(firstName, lastName, email, phone);

            dbManager.insertApplicant(applicant);
            System.out.println("Profile created successfully! Your Applicant ID is: " + applicant.getApplicantID());

        } catch (InvalidEmailException e) {
            System.err.println("Invalid email format: " + e.getMessage());
        } catch (SQLException e) {
            System.err.println("Error creating profile: " + e.getMessage());
        }
    }

    private static void submitJobApplication() {
        try {
            System.out.println("\n=== Submit Job Application ===");
            int applicantId = getIntInput("Enter your Applicant ID: ");
            viewAllJobListings(); // Show available jobs
            int jobId = getIntInput("Enter the Job ID you want to apply for: ");
            String coverLetter = getStringInput("Enter your cover letter: ");

            JobApplication application = new JobApplication(jobId, applicantId, coverLetter);
            dbManager.insertJobApplication(application);
            System.out.println("Application submitted successfully!");

        } catch (ApplicationDeadlineException e) {
            System.err.println("Application deadline passed: " + e.getMessage());
        } catch (SQLException e) {
            System.err.println("Error submitting application: " + e.getMessage());
        }
    }

    private static void postNewJob() {
        try {
            System.out.println("\n=== Post New Job ===");
            // First, handle company information
            String companyName = getStringInput("Enter company name: ");
            String location = getStringInput("Enter company location: ");
            
            Company company = new Company(companyName, location);
            dbManager.insertCompany(company);
            
            // Now create the job listing
            String jobTitle = getStringInput("Enter job title: ");
            String jobDescription = getStringInput("Enter job description: ");
            String jobLocation = getStringInput("Enter job location: ");
            BigDecimal salary = getBigDecimalInput("Enter salary: ");
            String jobType = getStringInput("Enter job type (Full-time/Part-time/Contract): ");

            JobListing job = new JobListing(company.getCompanyID(), jobTitle, jobDescription,
                                          jobLocation, salary, jobType);
            dbManager.insertJobListing(job);
            System.out.println("Job posted successfully!");

        } catch (InvalidSalaryException e) {
            System.err.println("Invalid salary: " + e.getMessage());
        } catch (SQLException e) {
            System.err.println("Error posting job: " + e.getMessage());
        }
    }

    private static void searchJobsBySalaryRange() {
        try {
            System.out.println("\n=== Search Jobs by Minimum Salary ===\n");
            BigDecimal minSalary = getBigDecimalInput("Enter minimum salary: ");

            List<JobListing> jobs = dbManager.getJobListings();
            
            List<JobListing> matchingJobs = jobs.stream()
                .filter(job -> job.getSalary().compareTo(minSalary) >= 0)
                .collect(Collectors.toList());

            if (matchingJobs.isEmpty()) {
                System.out.println("No jobs found with salary above " + minSalary);
            } else {
                System.out.println("\nJobs with salary above " + minSalary + ":");
                matchingJobs.forEach(System.out::println);
            }

        } catch (SQLException e) {
            System.err.println("Error searching jobs: " + e.getMessage());
        }
    }

    private static String getStringInput(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }

    private static int getIntInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number!");
            }
        }
    }

    private static BigDecimal getBigDecimalInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                return new BigDecimal(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number!");
            }
        }
    }
}
