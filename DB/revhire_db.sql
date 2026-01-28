    --Users Table
CREATE TABLE users (
    user_id NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    email VARCHAR2(100) UNIQUE NOT NULL,
    password_hash VARCHAR2(255) NOT NULL,
    role VARCHAR2(20) CHECK (role IN ('JOB_SEEKER', 'EMPLOYER')) NOT NULL,

    -- 🔐 Forgot Password Support
    security_question VARCHAR2(255) NOT NULL,
    security_answer_hash VARCHAR2(255) NOT NULL,

    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

    
    --job_seeker Table
-- ================= JOB SEEKERS =================
    CREATE TABLE job_seekers (
    seeker_id NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    user_id NUMBER UNIQUE NOT NULL,
    full_name VARCHAR2(100),
    phone VARCHAR2(15),
    location VARCHAR2(100),
    total_experience NUMBER,
    profile_completed CHAR(1) DEFAULT 'N' CHECK (profile_completed IN ('Y','N')),
    profile_completion NUMBER(3,0) DEFAULT 0, -- profile completion in %
    CONSTRAINT fk_job_seekers_user
        FOREIGN KEY (user_id) REFERENCES users(user_id)
);

    
    --Resume Table
CREATE TABLE resumes (
    resume_id NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    seeker_id NUMBER UNIQUE,
    objective VARCHAR2(500),
    education VARCHAR2(1000),
    experience VARCHAR2(1000),
    skills VARCHAR2(500),
    projects VARCHAR2(1000),
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (seeker_id) REFERENCES job_seekers(seeker_id)
);

ALTER TABLE resumes
ADD last_updated TIMESTAMP DEFAULT CURRENT_TIMESTAMP;
    
    --Employer
CREATE TABLE employers (
    employer_id NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    user_id NUMBER UNIQUE NOT NULL,
    company_name VARCHAR2(150) NOT NULL,
    industry VARCHAR2(100),
    company_size NUMBER,
    description CLOB,
    website VARCHAR2(150),
    location VARCHAR2(100),
    profile_completion NUMBER(3,0) DEFAULT 0, -- profile completion in %
    CONSTRAINT fk_employers_user
        FOREIGN KEY (user_id) REFERENCES users(user_id)
);
 
    --jobs
CREATE TABLE jobs (
    job_id NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    employer_id NUMBER NOT NULL,
    title VARCHAR2(100) NOT NULL,
    description VARCHAR2(1000),
    skills_required VARCHAR2(500),
    experience_required NUMBER,
    education_required VARCHAR2(200),
    location VARCHAR2(200),
    salary VARCHAR2(100),
    job_type VARCHAR2(50),
    deadline DATE,
    status VARCHAR2(20) DEFAULT 'OPEN',
    FOREIGN KEY (employer_id) REFERENCES employers(employer_id)
);

    
    --applications
CREATE TABLE applications (
    application_id NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    job_id NUMBER NOT NULL,
    seeker_id NUMBER NOT NULL,
    status VARCHAR2(15) DEFAULT 'APPLIED' 
        CHECK (status IN ('APPLIED', 'SHORTLISTED', 'REJECTED', 'WITHDRAWN')),
    applied_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    withdraw_reason VARCHAR2(255),
    CONSTRAINT uq_job_seeker UNIQUE (job_id, seeker_id),
    CONSTRAINT fk_applications_job
        FOREIGN KEY (job_id) REFERENCES jobs(job_id),
    CONSTRAINT fk_applications_seeker
        FOREIGN KEY (seeker_id) REFERENCES job_seekers(seeker_id)
);


    --notifications
CREATE TABLE notifications (
    notification_id NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    user_id NUMBER NOT NULL,
    message VARCHAR2(500) NOT NULL,
    is_read CHAR(1) DEFAULT 'N' CHECK (is_read IN ('Y','N')),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(user_id)
);

