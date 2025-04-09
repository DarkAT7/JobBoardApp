-- Create the job board database
CREATE DATABASE IF NOT EXISTS job_board;
USE job_board;

-- Create Company table
CREATE TABLE IF NOT EXISTS companies (
    CompanyID INT PRIMARY KEY AUTO_INCREMENT,
    CompanyName VARCHAR(255) NOT NULL,
    Location VARCHAR(255) NOT NULL,
    CreatedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UpdatedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Create JobListing table
CREATE TABLE IF NOT EXISTS job_listings (
    JobID INT PRIMARY KEY AUTO_INCREMENT,
    CompanyID INT NOT NULL,
    JobTitle VARCHAR(255) NOT NULL,
    JobDescription TEXT NOT NULL,
    JobLocation VARCHAR(255) NOT NULL,
    Salary DECIMAL(12,2) NOT NULL,
    JobType VARCHAR(50) NOT NULL,
    PostedDate DATETIME NOT NULL,
    CreatedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UpdatedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (CompanyID) REFERENCES companies(CompanyID),
    INDEX idx_company (CompanyID),
    INDEX idx_location (JobLocation),
    INDEX idx_type (JobType)
);

-- Create Applicant table
CREATE TABLE IF NOT EXISTS applicants (
    ApplicantID INT PRIMARY KEY AUTO_INCREMENT,
    FirstName VARCHAR(100) NOT NULL,
    LastName VARCHAR(100) NOT NULL,
    Email VARCHAR(255) NOT NULL UNIQUE,
    Phone VARCHAR(20) NOT NULL,
    CreatedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UpdatedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_email (Email),
    INDEX idx_name (LastName, FirstName)
);

-- Create JobApplication table
CREATE TABLE IF NOT EXISTS job_applications (
    ApplicationID INT PRIMARY KEY AUTO_INCREMENT,
    JobID INT NOT NULL,
    ApplicantID INT NOT NULL,
    ApplicationDate DATETIME NOT NULL,
    CoverLetter TEXT,
    Status VARCHAR(50) DEFAULT 'PENDING',
    CreatedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UpdatedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (JobID) REFERENCES job_listings(JobID),
    FOREIGN KEY (ApplicantID) REFERENCES applicants(ApplicantID),
    UNIQUE KEY unique_application (JobID, ApplicantID),
    INDEX idx_status (Status),
    INDEX idx_date (ApplicationDate)
);

-- Create views for common queries
CREATE OR REPLACE VIEW vw_job_listings_with_company AS
SELECT 
    j.*,
    c.CompanyName,
    c.Location AS CompanyLocation
FROM job_listings j
JOIN companies c ON j.CompanyID = c.CompanyID;

CREATE OR REPLACE VIEW vw_applications_with_details AS
SELECT 
    ja.*,
    j.JobTitle,
    j.JobLocation,
    j.JobType,
    c.CompanyName,
    a.FirstName,
    a.LastName,
    a.Email
FROM job_applications ja
JOIN job_listings j ON ja.JobID = j.JobID
JOIN companies c ON j.CompanyID = c.CompanyID
JOIN applicants a ON ja.ApplicantID = a.ApplicantID;

-- Create stored procedures for common operations
DELIMITER //

-- Procedure to post a new job
CREATE PROCEDURE sp_post_job(
    IN p_company_id INT,
    IN p_job_title VARCHAR(255),
    IN p_job_description TEXT,
    IN p_job_location VARCHAR(255),
    IN p_salary DECIMAL(12,2),
    IN p_job_type VARCHAR(50)
)
BEGIN
    INSERT INTO job_listings (
        CompanyID, JobTitle, JobDescription, 
        JobLocation, Salary, JobType, PostedDate
    ) VALUES (
        p_company_id, p_job_title, p_job_description,
        p_job_location, p_salary, p_job_type, NOW()
    );
END //

-- Procedure to apply for a job
CREATE PROCEDURE sp_apply_for_job(
    IN p_job_id INT,
    IN p_applicant_id INT,
    IN p_cover_letter TEXT
)
BEGIN
    INSERT INTO job_applications (
        JobID, ApplicantID, ApplicationDate, CoverLetter
    ) VALUES (
        p_job_id, p_applicant_id, NOW(), p_cover_letter
    );
END //

-- Function to get total applications for a job
CREATE FUNCTION fn_get_application_count(p_job_id INT) 
RETURNS INT
DETERMINISTIC
BEGIN
    DECLARE total INT;
    SELECT COUNT(*) INTO total 
    FROM job_applications 
    WHERE JobID = p_job_id;
    RETURN total;
END //

DELIMITER ;

-- Create triggers for data integrity
DELIMITER //

-- Trigger to validate salary before insert
CREATE TRIGGER tr_validate_salary_before_insert
BEFORE INSERT ON job_listings
FOR EACH ROW
BEGIN
    IF NEW.Salary < 0 THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Salary cannot be negative';
    END IF;
END //

-- Trigger to validate email format before insert
CREATE TRIGGER tr_validate_email_before_insert
BEFORE INSERT ON applicants
FOR EACH ROW
BEGIN
    IF NEW.Email NOT REGEXP '^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,}$' THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Invalid email format';
    END IF;
END //

DELIMITER ;
