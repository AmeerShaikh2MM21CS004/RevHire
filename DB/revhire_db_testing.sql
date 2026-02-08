--values to test

--Insert Users

-- Job Seeker User
-- ================= JOB SEEKERS =================
-- JOB SEEKER USERS
INSERT INTO users (email, password_hash, role, security_question, security_answer_hash)
VALUES ('seeker1@gmail.com', 'hash123', 'JOB_SEEKER', 'Fav color?', 'blue');

INSERT INTO users (email, password_hash, role, security_question, security_answer_hash)
VALUES ('seeker2@gmail.com', 'hash123', 'JOB_SEEKER', 'Pet name?', 'tom');

-- EMPLOYER USERS
INSERT INTO users (email, password_hash, role, security_question, security_answer_hash)
VALUES ('hr@techcorp.com', 'hash456', 'EMPLOYER', 'Birth city?', 'blr');

INSERT INTO users (email, password_hash, role, security_question, security_answer_hash)
VALUES ('hr@finserv.com', 'hash456', 'EMPLOYER', 'Fav teacher?', 'rao');


-- Verify
SELECT * FROM users;    

INSERT INTO job_seekers (user_id, full_name, phone, location, total_experience)
VALUES (1, 'Ameer Shaikh', '9876543210', 'Bangalore', 1);

INSERT INTO job_seekers (user_id, full_name, phone, location, total_experience)
VALUES (2, 'Rahul Verma', '9123456780', 'Mumbai', 2);


SELECT * FROM job_seekers;

--Insert Resume
SET DEFINE OFF;
INSERT INTO resumes (seeker_id, objective, education, experience, skills, projects)
VALUES (
    1,
    'Looking for backend developer role',
    'B.Tech Computer Science',
    '1 year Java development',
    'Java, SQL, Spring',
    'Job Portal Application'
);


INSERT INTO resumes (seeker_id, objective, education, experience, skills)
VALUES (
    2,
    'Seeking data analyst role',
    'B.Sc Statistics',
    '2 years analytics',
    'Python, SQL, PowerBI'
);

SELECT * FROM resumes;


--Insert Employer Profile
-- Insert Employers aligned with users.user_id 11-20
-- Employers for user_id 1–20
INSERT INTO employers (user_id, company_name, industry, company_size, location)
VALUES (3, 'TechCorp Pvt Ltd', 'IT Services', 200, 'Bangalore');

INSERT INTO employers (user_id, company_name, industry, company_size, location)
VALUES (4, 'FinServe Ltd', 'Finance', 500, 'Mumbai');


SELECT * FROM employers;       


--Job
INSERT INTO jobs (employer_id, title, description, skills_required, experience_required, location, salary)
VALUES (
    1,
    'Java Developer',
    'Backend Java Developer role',
    'Java, SQL, Spring',
    1,
    'Bangalore',
    '6 LPA'
);

INSERT INTO jobs (employer_id, title, description, skills_required, experience_required, location, salary)
VALUES (
    2,
    'Data Analyst',
    'Analytics and reporting',
    'Python, SQL, PowerBI',
    2,
    'Mumbai',
    '8 LPA'
);

SELECT * FROM jobs;
SELECT job_id, title, status FROM jobs WHERE status = 'OPEN';


SELECT j.job_id, j.title, e.company_name,j.employer_id,
               j.location, j.salary, j.job_type, j.status
        FROM jobs j
        JOIN employers e ON j.employer_id = e.employer_id
        WHERE j.status = 'OPEN'


--Apply for Job
-- Applications: Job Seeker applies to different jobs
-- Format: (job_id, seeker_id)
-- Status defaults to 'APPLIED'
INSERT INTO applications (job_id, seeker_id)
VALUES (1, 1);

INSERT INTO applications (job_id, seeker_id)
VALUES (2, 2);

COMMIT;

-- Verify
SELECT * FROM applications;


--INSERT NOTIFICATION
INSERT INTO notifications (user_id, message)
VALUES (3, 'New application received for Java Developer');

INSERT INTO notifications (user_id, message)
VALUES (1, 'Your application has been submitted successfully');

INSERT INTO notifications (user_id, message)
VALUES (2, 'Your profile is 70% complete');

SELECT * FROM notifications;

select *
from notifications
where is_read ='N' and user_id='3';



--IMPORTANT SELECT QUERIES (REAL USE CASES)

--Job Search (By Location & Experience)
SELECT j.title, e.company_name, j.location
FROM jobs j
JOIN employers e ON j.employer_id = e.employer_id
WHERE j.location = 'Bangalore'
AND j.experience_required <= 1
AND j.status = 'OPEN';

--View Application Status (Job Seeker)
SELECT j.title, a.status
FROM applications a
JOIN jobs j ON a.job_id = j.job_id
WHERE a.seeker_id = 1;

--Employer View Applicants
SELECT js.full_name, a.status
FROM applications a
JOIN job_seekers js ON a.seeker_id = js.seeker_id
WHERE a.job_id = 1;


-- Child tables first
DELETE FROM notifications;
DELETE FROM applications;
DELETE FROM jobs;
DELETE FROM resumes;
DELETE FROM job_seekers;
DELETE FROM employers;
    
-- Parent table last
DELETE FROM users;

COMMIT;


--tables delete
DROP TABLE notifications CASCADE CONSTRAINTS;
DROP TABLE applications CASCADE CONSTRAINTS;
DROP TABLE jobs CASCADE CONSTRAINTS;
DROP TABLE resumes CASCADE CONSTRAINTS;
DROP TABLE job_seekers CASCADE CONSTRAINTS;
DROP TABLE employers CASCADE CONSTRAINTS;
DROP TABLE users CASCADE CONSTRAINTS;


COMMIT;



SHOW USER;




