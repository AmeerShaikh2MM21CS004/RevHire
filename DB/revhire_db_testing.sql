--values to test

--Insert Users

-- Job Seeker User
INSERT INTO users (email, password_hash, role)
-- Job Seekers
INSERT INTO users (email, password_hash, role) VALUES ('seeker1@gmail.com', 'hashed_pwd_1', 'JOB_SEEKER');
INSERT INTO users (email, password_hash, role) VALUES ('seeker2@gmail.com', 'hashed_pwd_2', 'JOB_SEEKER');
INSERT INTO users (email, password_hash, role) VALUES ('seeker3@gmail.com', 'hashed_pwd_3', 'JOB_SEEKER');
INSERT INTO users (email, password_hash, role) VALUES ('seeker4@gmail.com', 'hashed_pwd_4', 'JOB_SEEKER');
INSERT INTO users (email, password_hash, role) VALUES ('seeker5@gmail.com', 'hashed_pwd_5', 'JOB_SEEKER');
INSERT INTO users (email, password_hash, role) VALUES ('seeker6@gmail.com', 'hashed_pwd_6', 'JOB_SEEKER');
INSERT INTO users (email, password_hash, role) VALUES ('seeker7@gmail.com', 'hashed_pwd_7', 'JOB_SEEKER');
INSERT INTO users (email, password_hash, role) VALUES ('seeker8@gmail.com', 'hashed_pwd_8', 'JOB_SEEKER');
INSERT INTO users (email, password_hash, role) VALUES ('seeker9@gmail.com', 'hashed_pwd_9', 'JOB_SEEKER');
INSERT INTO users (email, password_hash, role) VALUES ('seeker10@gmail.com', 'hashed_pwd_10', 'JOB_SEEKER');

-- Employers
INSERT INTO users (email, password_hash, role) VALUES ('employer1@company.com', 'hashed_pwd_11', 'EMPLOYER');
INSERT INTO users (email, password_hash, role) VALUES ('employer2@company.com', 'hashed_pwd_12', 'EMPLOYER');
INSERT INTO users (email, password_hash, role) VALUES ('employer3@company.com', 'hashed_pwd_13', 'EMPLOYER');
INSERT INTO users (email, password_hash, role) VALUES ('employer4@company.com', 'hashed_pwd_14', 'EMPLOYER');
INSERT INTO users (email, password_hash, role) VALUES ('employer5@company.com', 'hashed_pwd_15', 'EMPLOYER');
INSERT INTO users (email, password_hash, role) VALUES ('employer6@company.com', 'hashed_pwd_16', 'EMPLOYER');
INSERT INTO users (email, password_hash, role) VALUES ('employer7@company.com', 'hashed_pwd_17', 'EMPLOYER');
INSERT INTO users (email, password_hash, role) VALUES ('employer8@company.com', 'hashed_pwd_18', 'EMPLOYER');
INSERT INTO users (email, password_hash, role) VALUES ('employer9@company.com', 'hashed_pwd_19', 'EMPLOYER');
INSERT INTO users (email, password_hash, role) VALUES ('employer10@company.com', 'hashed_pwd_20', 'EMPLOYER');

-- Verify
SELECT user_id, email, role FROM users;

INSERT INTO job_seekers (user_id, full_name, phone, location, total_experience, profile_completed)
VALUES (1,'Ameer Shaikh','900000001','Bangalore',1,'Y');
INSERT INTO job_seekers (user_id, full_name, phone, location, total_experience, profile_completed)
VALUES (2,'Rahul Verma','900000002','Pune',2,'Y');
INSERT INTO job_seekers (user_id, full_name, phone, location, total_experience, profile_completed)
VALUES (3,'Sneha Patil','900000003','Mumbai',0,'Y');
INSERT INTO job_seekers (user_id, full_name, phone, location, total_experience, profile_completed)
VALUES (4,'Kiran Rao','900000004','Hyderabad',3,'Y');
INSERT INTO job_seekers (user_id, full_name, phone, location, total_experience, profile_completed)
VALUES (5,'Anita Joshi','900000005','Delhi',5,'Y');
INSERT INTO job_seekers (user_id, full_name, phone, location, total_experience, profile_completed)
VALUES (6,'Sahil Khan','900000006','Bangalore',2,'Y');
INSERT INTO job_seekers (user_id, full_name, phone, location, total_experience, profile_completed)
VALUES (7,'Neha Singh','900000007','Pune',1,'Y');
INSERT INTO job_seekers (user_id, full_name, phone, location, total_experience, profile_completed)
VALUES (8,'Rohit Sharma','900000008','Mumbai',4,'Y');
INSERT INTO job_seekers (user_id, full_name, phone, location, total_experience, profile_completed)
VALUES (9,'Pooja Mehta','900000009','Hyderabad',3,'Y');
INSERT INTO job_seekers (user_id, full_name, phone, location, total_experience, profile_completed)
VALUES (10,'Arjun Malhotra','900000010','Delhi',6,'Y');

SELECT * FROM job_seekers;

--Insert Resume
SET DEFINE OFF;
IINSERT INTO resumes (seeker_id, objective, education, experience, skills, projects)
VALUES (1,'Java Developer','B.E CSE','1 year internship','Java, SQL','RevHire');

INSERT INTO resumes (seeker_id, objective, education, experience, skills, projects)
VALUES (2,'Backend Developer','B.Tech','2 years','Java, Spring','E-Commerce');

INSERT INTO resumes (seeker_id, objective, education, experience, skills, projects)
VALUES (3,'Fresher','B.E','Fresher','Java, HTML','Mini Project');

INSERT INTO resumes (seeker_id, objective, education, experience, skills, projects)
VALUES (4,'Senior Developer','M.Tech','3 years','Java, Microservices','Bank App');

INSERT INTO resumes (seeker_id, objective, education, experience, skills, projects)
VALUES (5,'Lead Engineer','B.E','5 years','Java, Kafka','ERP');

INSERT INTO resumes (seeker_id, objective, education, experience, skills, projects)
VALUES (6,'Java Developer','B.Tech','2 years','Java, SQL','HRMS');

INSERT INTO resumes (seeker_id, objective, education, experience, skills, projects)
VALUES (7,'Junior Developer','B.Sc','1 year','Java','Portfolio');

INSERT INTO resumes (seeker_id, objective, education, experience, skills, projects)
VALUES (8,'Backend Engineer','B.E','4 years','Java, Spring','CRM');

INSERT INTO resumes (seeker_id, objective, education, experience, skills, projects)
VALUES (9,'API Developer','MCA','3 years','Java, REST','Payment App');

INSERT INTO resumes (seeker_id, objective, education, experience, skills, projects)
VALUES (10,'Architect','B.Tech','6 years','Java, Cloud','SaaS');

SELECT seeker_id, skills FROM resumes;


--Insert Employer Profile
-- Insert Employers aligned with users.user_id 11-20
-- Employers for user_id 1–20
INSERT INTO employers (user_id, company_name, industry, company_size, description, website, location)
VALUES (1,'TechNova','IT',300,'IT services company','https://technova.com','Bangalore');

INSERT INTO employers (user_id, company_name, industry, company_size, description, website, location)
VALUES (2,'CodeCraft','Product',120,'SaaS based product company','https://codecraft.com','Pune');

INSERT INTO employers (user_id, company_name, industry, company_size, description, website, location)
VALUES (3,'DataWave','Analytics',200,'AI and Data Analytics company','https://datawave.com','Mumbai');

INSERT INTO employers (user_id, company_name, industry, company_size, description, website, location)
VALUES (4,'CloudCore','Cloud',500,'Cloud infrastructure services','https://cloudcore.com','Hyderabad');

INSERT INTO employers (user_id, company_name, industry, company_size, description, website, location)
VALUES (5,'FinEdge','FinTech',250,'Financial technology solutions','https://finedge.com','Delhi');

INSERT INTO employers (user_id, company_name, industry, company_size, description, website, location)
VALUES (6,'SoftPlus','IT',150,'Software development company','https://softplus.com','Bangalore');

INSERT INTO employers (user_id, company_name, industry, company_size, description, website, location)
VALUES (7,'DevHub','Startup',80,'Development tools and services','https://devhub.com','Pune');

INSERT INTO employers (user_id, company_name, industry, company_size, description, website, location)
VALUES (8,'LogicWorks','IT',400,'Consulting and IT services','https://logicworks.com','Mumbai');

INSERT INTO employers (user_id, company_name, industry, company_size, description, website, location)
VALUES (9,'AppForge','Product',220,'Mobile app development','https://appforge.com','Hyderabad');

INSERT INTO employers (user_id, company_name, industry, company_size, description, website, location)
VALUES (10,'NextGen','AI',350,'AI / ML solutions','https://nextgen.com','Delhi');

-- Extra employers for 11–20
INSERT INTO employers (user_id, company_name, industry, company_size, description, website, location)
VALUES (11,'TechPulse','IT',280,'Cloud & AI services','https://techpulse.com','Bangalore');

INSERT INTO employers (user_id, company_name, industry, company_size, description, website, location)
VALUES (12,'SoftEdge','Software',140,'Software solutions','https://softedge.com','Pune');

INSERT INTO employers (user_id, company_name, industry, company_size, description, website, location)
VALUES (13,'DataMinds','Analytics',220,'Data analytics services','https://dataminds.com','Mumbai');

INSERT INTO employers (user_id, company_name, industry, company_size, description, website, location)
VALUES (14,'CloudWorks','Cloud',450,'Cloud & infra','https://cloudworks.com','Hyderabad');

INSERT INTO employers (user_id, company_name, industry, company_size, description, website, location)
VALUES (15,'FinSolve','FinTech',270,'Financial software solutions','https://finsolve.com','Delhi');

INSERT INTO employers (user_id, company_name, industry, company_size, description, website, location)
VALUES (16,'NextSoft','IT',160,'Enterprise software','https://nextsoft.com','Bangalore');

INSERT INTO employers (user_id, company_name, industry, company_size, description, website, location)
VALUES (17,'DevWorks','Startup',90,'App & Web dev','https://devworks.com','Pune');

INSERT INTO employers (user_id, company_name, industry, company_size, description, website, location)
VALUES (18,'LogicSoft','IT',420,'IT consulting','https://logicsoft.com','Mumbai');

INSERT INTO employers (user_id, company_name, industry, company_size, description, website, location)
VALUES (19,'AppLogic','Product',240,'Mobile & web apps','https://applogic.com','Hyderabad');

INSERT INTO employers (user_id, company_name, industry, company_size, description, website, location)
VALUES (20,'NextInnovate','AI',360,'AI & ML innovations','https://nextinnovate.com','Delhi');


SELECT employer_id, company_name FROM employers;


--Job
INSERT INTO jobs (employer_id, title, description, skills_required, experience_required, education_required, location, salary, job_type, deadline)
VALUES (1,'Java Developer','Backend development','Java, JDBC',1,'B.E','Bangalore','6–8 LPA','Full Time',DATE '2026-03-31');

INSERT INTO jobs (employer_id, title, description, skills_required, experience_required, education_required, location, salary, job_type, deadline)
VALUES (2,'Backend Developer','API development','Java, Spring',2,'B.Tech','Pune','5–7 LPA','Full Time',DATE '2026-04-15');

INSERT INTO jobs (employer_id, title, description, skills_required, experience_required, education_required, location, salary, job_type, deadline)
VALUES (3,'Data Analyst','Data processing','SQL, Python',1,'B.Sc','Mumbai','4–6 LPA','Full Time',DATE '2026-05-01');

INSERT INTO jobs (employer_id, title, description, skills_required, experience_required, education_required, location, salary, job_type, deadline)
VALUES (4,'Cloud Engineer','Cloud deployment','AWS, Docker',3,'B.E','Hyderabad','8–10 LPA','Full Time',DATE '2026-03-28');

INSERT INTO jobs (employer_id, title, description, skills_required, experience_required, education_required, location, salary, job_type, deadline)
VALUES (5,'FinTech Analyst','Finance apps','Python, SQL',2,'B.E','Delhi','6–8 LPA','Full Time',DATE '2026-04-10');

INSERT INTO jobs (employer_id, title, description, skills_required, experience_required, education_required, location, salary, job_type, deadline)
VALUES (6,'Java Engineer','Backend systems','Java, Spring',2,'B.Tech','Bangalore','5–7 LPA','Full Time',DATE '2026-05-05');

INSERT INTO jobs (employer_id, title, description, skills_required, experience_required, education_required, location, salary, job_type, deadline)
VALUES (7,'Frontend Developer','Web development','HTML, CSS, JS',1,'B.Sc','Pune','4–6 LPA','Full Time',DATE '2026-03-31');

INSERT INTO jobs (employer_id, title, description, skills_required, experience_required, education_required, location, salary, job_type, deadline)
VALUES (8,'CRM Developer','Customer apps','Java, Spring',4,'B.E','Mumbai','7–9 LPA','Full Time',DATE '2026-04-20');

INSERT INTO jobs (employer_id, title, description, skills_required, experience_required, education_required, location, salary, job_type, deadline)
VALUES (9,'Mobile App Developer','Android apps','Java, Kotlin',3,'B.Tech','Hyderabad','6–8 LPA','Full Time',DATE '2026-04-30');

INSERT INTO jobs (employer_id, title, description, skills_required, experience_required, education_required, location, salary, job_type, deadline)
VALUES (10,'AI Engineer','ML models','Python, TensorFlow',5,'M.Tech','Delhi','10–12 LPA','Full Time',DATE '2026-05-15');

SELECT job_id, title, status FROM jobs;


--Apply for Job
-- Applications: Job Seeker applies to different jobs
-- Format: (job_id, seeker_id)
-- Status defaults to 'APPLIED'

INSERT INTO applications (job_id, seeker_id) VALUES (1, 1);
INSERT INTO applications (job_id, seeker_id) VALUES (2, 1);
INSERT INTO applications (job_id, seeker_id) VALUES (3, 2);
INSERT INTO applications (job_id, seeker_id) VALUES (4, 2);
INSERT INTO applications (job_id, seeker_id) VALUES (5, 3);
INSERT INTO applications (job_id, seeker_id) VALUES (6, 3);
INSERT INTO applications (job_id, seeker_id) VALUES (7, 4);
INSERT INTO applications (job_id, seeker_id) VALUES (8, 4);
INSERT INTO applications (job_id, seeker_id) VALUES (9, 5);
INSERT INTO applications (job_id, seeker_id) VALUES (10, 5);
INSERT INTO applications (job_id, seeker_id) VALUES (11, 6);
INSERT INTO applications (job_id, seeker_id) VALUES (12, 6);
INSERT INTO applications (job_id, seeker_id) VALUES (13, 7);
INSERT INTO applications (job_id, seeker_id) VALUES (14, 7);
INSERT INTO applications (job_id, seeker_id) VALUES (15, 8);
INSERT INTO applications (job_id, seeker_id) VALUES (16, 8);
INSERT INTO applications (job_id, seeker_id) VALUES (17, 9);
INSERT INTO applications (job_id, seeker_id) VALUES (18, 9);
INSERT INTO applications (job_id, seeker_id) VALUES (19, 10);
INSERT INTO applications (job_id, seeker_id) VALUES (20, 10);

-- Optional: Update some statuses to simulate real workflow
UPDATE applications SET status = 'SHORTLISTED' WHERE application_id IN (1,3,5,7,9);
UPDATE applications SET status = 'REJECTED' WHERE application_id IN (2,4,6,8,10);
UPDATE applications SET status = 'WITHDRAWN' WHERE application_id = 20;

-- Verify
SELECT * FROM applications;


-- Update some statuses
UPDATE applications SET status='SHORTLISTED' WHERE application_id IN (1,3,5);
UPDATE applications SET status='REJECTED' WHERE application_id IN (2,4);

SELECT application_id, status FROM applications;

--INSERT NOTIFICATION
INSERT INTO notifications (user_id, message)
VALUES (1,'Your application for Java Developer has been shortlisted.');

INSERT INTO notifications (user_id, message)
VALUES (3,'Your application for Data Analyst has been shortlisted.');

INSERT INTO notifications (user_id, message)
VALUES (5,'Your application for FinTech Analyst has been shortlisted.');

INSERT INTO notifications (user_id, message)
VALUES (2,'Your application for Backend Developer has been rejected.');

INSERT INTO notifications (user_id, message)
VALUES (4,'Your application for Cloud Engineer has been rejected.');


SELECT message, is_read FROM notifications;


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




