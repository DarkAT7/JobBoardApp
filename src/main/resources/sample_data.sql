-- Sample data for Job Board Application

-- Insert tech companies
INSERT INTO companies (CompanyName, Location) VALUES
('TechCorp India', 'Bangalore'),
('InnovateX Solutions', 'Mumbai'),
('Digital Dynamics', 'Hyderabad');

-- Insert job listings with USD salaries
INSERT INTO job_listings (CompanyID, JobTitle, JobDescription, JobLocation, Salary, JobType, PostedDate) VALUES
(1, 'Senior Software Engineer', 'Lead development of cloud-native applications using Java and Spring Boot', 'Bangalore', 120000.00, 'Full-time', NOW()),
(1, 'DevOps Engineer', 'Manage CI/CD pipelines and cloud infrastructure', 'Bangalore', 95000.00, 'Full-time', NOW()),
(1, 'Product Manager', 'Lead product strategy and roadmap for enterprise solutions', 'Bangalore', 130000.00, 'Full-time', NOW()),
(2, 'Full Stack Developer', 'Build scalable web applications using MERN stack', 'Mumbai', 85000.00, 'Full-time', NOW()),
(2, 'Data Scientist', 'Develop ML models for predictive analytics', 'Mumbai', 110000.00, 'Full-time', NOW()),
(2, 'UI/UX Designer', 'Create intuitive user interfaces for web and mobile applications', 'Mumbai', 75000.00, 'Full-time', NOW()),
(3, 'Cloud Architect', 'Design and implement cloud-native solutions on AWS/Azure', 'Hyderabad', 140000.00, 'Full-time', NOW()),
(3, 'Security Engineer', 'Implement and maintain cybersecurity measures', 'Hyderabad', 115000.00, 'Full-time', NOW()),
(3, 'Mobile Developer', 'Develop cross-platform mobile applications', 'Hyderabad', 90000.00, 'Full-time', NOW()),
(3, 'Technical Lead', 'Lead development team and architect solutions', 'Hyderabad', 125000.00, 'Full-time', NOW());

-- Insert sample applicants
INSERT INTO applicants (FirstName, LastName, Email, Phone) VALUES
('Arjun', 'Patel', 'arjun.patel@gmail.com', '+91-9876543210'),
('Priya', 'Sharma', 'priya.sharma@outlook.com', '+91-9876543211'),
('Rahul', 'Verma', 'rahul.verma@gmail.com', '+91-9876543212'),
('Neha', 'Gupta', 'neha.gupta@yahoo.com', '+91-9876543213'),
('Aditya', 'Kumar', 'aditya.kumar@gmail.com', '+91-9876543214'),
('Ananya', 'Singh', 'ananya.singh@outlook.com', '+91-9876543215'),
('Rohan', 'Mehta', 'rohan.mehta@gmail.com', '+91-9876543216'),
('Zara', 'Khan', 'zara.khan@yahoo.com', '+91-9876543217'),
('Vikram', 'Reddy', 'vikram.reddy@gmail.com', '+91-9876543218'),
('Meera', 'Iyer', 'meera.iyer@outlook.com', '+91-9876543219');

-- Insert some job applications
INSERT INTO job_applications (JobID, ApplicantID, ApplicationDate, CoverLetter) VALUES
(1, 1, NOW(), 'I am excited to apply for the Senior Software Engineer position...'),
(2, 3, NOW(), 'With my experience in DevOps and cloud technologies...'),
(4, 2, NOW(), 'As a full stack developer with 5 years of experience...'),
(7, 5, NOW(), 'Having architected cloud solutions for major enterprises...'),
(5, 4, NOW(), 'My background in data science and machine learning...');
