package com.example.util;

import com.example.entity.*;
import com.example.exception.*;
import java.sql.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDateTime;

public class DatabaseManager {
    private final Connection connection;

    public DatabaseManager(String connectionString) throws DatabaseConnectionException {
        try {
            this.connection = DBConnUtil.getConnection(connectionString);
        } catch (SQLException e) {
            throw new DatabaseConnectionException("Failed to establish database connection", e);
        }
    }

    public void initializeDatabase() throws DatabaseConnectionException {
        try {
            // Read and execute the SQL schema file
            String schemaFile = "src/main/resources/job_board_schema.sql";
            String schema = new String(java.nio.file.Files.readAllBytes(java.nio.file.Paths.get(schemaFile)));
            try (Statement stmt = connection.createStatement()) {
                stmt.execute(schema);
            }
        } catch (Exception e) {
            throw new DatabaseConnectionException("Failed to initialize database", e);
        }
    }

    public void insertJobListing(JobListing job) throws InvalidSalaryException, SQLException {
        if (job.getSalary().compareTo(BigDecimal.ZERO) < 0) {
            throw new InvalidSalaryException("Salary cannot be negative");
        }

        String sql = "CALL sp_post_job(?, ?, ?, ?, ?, ?)";
        try (CallableStatement stmt = connection.prepareCall(sql)) {
            stmt.setInt(1, job.getCompanyID());
            stmt.setString(2, job.getJobTitle());
            stmt.setString(3, job.getJobDescription());
            stmt.setString(4, job.getJobLocation());
            stmt.setBigDecimal(5, job.getSalary());
            stmt.setString(6, job.getJobType());
            stmt.execute();
        }
    }

    public void insertCompany(Company company) throws SQLException {
        String sql = "INSERT INTO companies (CompanyName, Location) VALUES (?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, company.getCompanyName());
            stmt.setString(2, company.getLocation());
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    company.setCompanyID(rs.getInt(1));
                }
            }
        }
    }

    public void insertApplicant(Applicant applicant) throws InvalidEmailException, SQLException {
        if (!isValidEmail(applicant.getEmail())) {
            throw new InvalidEmailException("Invalid email format");
        }

        String sql = "INSERT INTO applicants (FirstName, LastName, Email, Phone) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, applicant.getFirstName());
            stmt.setString(2, applicant.getLastName());
            stmt.setString(3, applicant.getEmail());
            stmt.setString(4, applicant.getPhone());
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    applicant.setApplicantID(rs.getInt(1));
                }
            }
        }
    }

    public void insertJobApplication(JobApplication application) throws ApplicationDeadlineException, SQLException {
        // Check if the job is still accepting applications
        if (isApplicationDeadlinePassed(application.getJobID())) {
            throw new ApplicationDeadlineException("Application deadline has passed for this job");
        }

        String sql = "CALL sp_apply_for_job(?, ?, ?)";
        try (CallableStatement stmt = connection.prepareCall(sql)) {
            stmt.setInt(1, application.getJobID());
            stmt.setInt(2, application.getApplicantID());
            stmt.setString(3, application.getCoverLetter());
            stmt.execute();
        }
    }

    public List<JobListing> getJobListings() throws SQLException {
        List<JobListing> jobs = new ArrayList<>();
        String sql = "SELECT * FROM vw_job_listings_with_company";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                jobs.add(extractJobListingFromResultSet(rs));
            }
        }
        return jobs;
    }

    public List<Company> getCompanies() throws SQLException {
        List<Company> companies = new ArrayList<>();
        String sql = "SELECT * FROM companies";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                companies.add(extractCompanyFromResultSet(rs));
            }
        }
        return companies;
    }

    public List<Applicant> getApplicants() throws SQLException {
        List<Applicant> applicants = new ArrayList<>();
        String sql = "SELECT * FROM applicants";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                applicants.add(extractApplicantFromResultSet(rs));
            }
        }
        return applicants;
    }

    public List<JobApplication> getApplicationsForJob(int jobID) throws SQLException {
        List<JobApplication> applications = new ArrayList<>();
        String sql = "SELECT * FROM vw_applications_with_details WHERE JobID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, jobID);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    applications.add(extractJobApplicationFromResultSet(rs));
                }
            }
        }
        return applications;
    }

    public double calculateAverageSalary() throws InvalidSalaryException, SQLException {
        String sql = "SELECT AVG(Salary) as avg_salary FROM job_listings WHERE Salary >= 0";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                double avgSalary = rs.getDouble("avg_salary");
                if (rs.wasNull() || avgSalary < 0) {
                    throw new InvalidSalaryException("Invalid salary values found in database");
                }
                return avgSalary;
            }
            return 0.0;
        }
    }

    private boolean isValidEmail(String email) {
        return email != null && email.matches("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    }

    private boolean isApplicationDeadlinePassed(int jobID) throws SQLException {
        String sql = "SELECT PostedDate FROM job_listings WHERE JobID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, jobID);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    LocalDateTime postedDate = rs.getTimestamp("PostedDate").toLocalDateTime();
                    // Assuming 30 days application window
                    return postedDate.plusDays(30).isBefore(LocalDateTime.now());
                }
            }
        }
        return true;
    }

    // Helper methods to extract entities from ResultSet
    private JobListing extractJobListingFromResultSet(ResultSet rs) throws SQLException {
        JobListing job = new JobListing();
        job.setJobID(rs.getInt("JobID"));
        job.setCompanyID(rs.getInt("CompanyID"));
        job.setJobTitle(rs.getString("JobTitle"));
        job.setJobDescription(rs.getString("JobDescription"));
        job.setJobLocation(rs.getString("JobLocation"));
        job.setSalary(rs.getBigDecimal("Salary"));
        job.setJobType(rs.getString("JobType"));
        job.setPostedDate(rs.getTimestamp("PostedDate").toLocalDateTime());
        return job;
    }

    private Company extractCompanyFromResultSet(ResultSet rs) throws SQLException {
        Company company = new Company();
        company.setCompanyID(rs.getInt("CompanyID"));
        company.setCompanyName(rs.getString("CompanyName"));
        company.setLocation(rs.getString("Location"));
        return company;
    }

    private Applicant extractApplicantFromResultSet(ResultSet rs) throws SQLException {
        Applicant applicant = new Applicant();
        applicant.setApplicantID(rs.getInt("ApplicantID"));
        applicant.setFirstName(rs.getString("FirstName"));
        applicant.setLastName(rs.getString("LastName"));
        applicant.setEmail(rs.getString("Email"));
        applicant.setPhone(rs.getString("Phone"));
        return applicant;
    }

    private JobApplication extractJobApplicationFromResultSet(ResultSet rs) throws SQLException {
        JobApplication application = new JobApplication();
        application.setApplicationID(rs.getInt("ApplicationID"));
        application.setJobID(rs.getInt("JobID"));
        application.setApplicantID(rs.getInt("ApplicantID"));
        application.setApplicationDate(rs.getTimestamp("ApplicationDate").toLocalDateTime());
        application.setCoverLetter(rs.getString("CoverLetter"));
        return application;
    }
}
