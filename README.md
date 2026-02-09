# RevHire – Console-Based Job Portal Application

**RevHire** is a **Java-based console application** that connects **Job Seekers** and **Employers** on a single platform.  
Job seekers can build resumes, search and apply for jobs, while employers can post jobs, manage applications, and shortlist or reject candidates.

The project follows a **layered architecture (DAO → Service → UI)** and is designed to be **scalable**, making it ready for future conversion into a **web or microservices-based application**.

---

## 📌 Application Overview

**RevHire** simulates a real-world job portal system with:

- Role-based access (Job Seeker / Employer)
- Secure authentication and password management
- Resume creation and management
- Job posting, search, and application workflows
- In-app notification system

---

## 🧑‍💼 Job Seeker Features

- Register & Login
- Create and manage profile
- Build structured textual resumes:
  - Objective
  - Education
  - Experience
  - Skills
  - Projects
- Search jobs using filters:
  - Role, Location, Experience, Company, Salary, Job Type
- Apply for jobs (one-click)
- Track application status:
  - Applied, Shortlisted, Rejected, Withdrawn
- Withdraw applications
- Receive in-app notifications:
  - Application status updates
  - Job matches

---

## 🏢 Employer Features

- Register company & login
- Create and manage job postings:
  - Title, Description, Skills, Experience, Education, Location, Salary, Job Type, Deadline
- Manage job postings:
  - View, Edit, Close/Reopen, Delete
- View and manage applicants for each job
- Shortlist / Reject applications
- Update company profile
- Receive notifications for new applications

---

## 🔐 Authentication & Account Management

- Role-based registration (Job Seeker / Employer)
- Secure login using hashed passwords
- Change password with validation
- Forgot password recovery using security questions
- Profile completion tracking

---

## 🔔 Notification System

- Real-time in-app notifications for:
  - Application status updates
  - New job applications
  - Job-related updates

---

## 🧱 Project Architecture
```
UI (Console)
↓
Service Layer
↓
DAO Layer
↓
Database (JDBC)
```

- **DAO Layer**: Handles database operations
  - **Service Layer**: Business logic
  - **UI Layer**: Console-based user interaction
  - **Util Package**: DB connection, hashing, helpers
  - **Log4j**: Centralized logging (file-based)

---

## 📂 Project Structure
``````
RevHire
│
├── src
│   ├── main
│   │   ├── java
│   │   │   └── com.revhare
│   │   │       ├── dao
│   │   │       │   ├── impl
│   │   │       │   │   ├── ApplicationsDAOImpl.java
│   │   │       │   │   ├── EmployersDAOImpl.java
│   │   │       │   │   ├── JobsDAOImpl.java
│   │   │       │   │   ├── JobSeekersDAOImpl.java
│   │   │       │   │   ├── NotificationsDAOImpl.java
│   │   │       │   │   ├── ResumesDAOImpl.java
│   │   │       │   │   └── UserDAOImpl.java
│   │   │       │   ├── ApplicationsDAO.java
│   │   │       │   ├── EmployersDAO.java
│   │   │       │   ├── JobsDAO.java
│   │   │       │   ├── JobSeekersDAO.java
│   │   │       │   ├── NotificationsDAO.java
│   │   │       │   ├── ResumesDAO.java
│   │   │       │   └── UserDAO.java
│   │   │       │
│   │   │       ├── model
│   │   │       │   ├── Application.java
│   │   │       │   ├── Employer.java
│   │   │       │   ├── Job.java
│   │   │       │   ├── JobSeeker.java
│   │   │       │   ├── Notification.java
│   │   │       │   ├── Resume.java
│   │   │       │   └── User.java
│   │   │       │
│   │   │       ├── service
│   │   │       │   ├── impl
│   │   │       │   │   ├── ApplicationServiceImpl.java
│   │   │       │   │   ├── EmployersServiceImpl.java
│   │   │       │   │   ├── JobSeekersImpl.java
│   │   │       │   │   ├── JobServiceImpl.java
│   │   │       │   │   ├── NotificationsServiceImpl.java
│   │   │       │   │   ├── ResumeServiceImpl.java
│   │   │       │   │   ├── UserServiceImpl.java
│   │   │       │   ├── ApplicationsService.java
│   │   │       │   ├── EmployersService.java
│   │   │       │   ├── JobSeekersService.java
│   │   │       │   ├── JobService.java
│   │   │       │   ├── NotificationsService.java
│   │   │       │   ├── ResumeService.java
│   │   │       │   └── UserService.java
│   │   │       │
│   │   │       └── util
│   │   │           ├── DBConnection.java
│   │   │           ├── HashUtil.java
│   │   │           └── ProfileUtil.java
│   │   └── resource
│   │         └── log4j2.xml
│   └── test
│        └── java
│            └── com.revhare
│                ├── model
│                │   ├── Application.java
│                │   ├── Employer.java
│                │   ├── Job.java
│                │   ├── JobSeeker.java
│                │   ├── Notification.java
│                │   ├── Resume.java
│                │   └── User.java
│                │
│                └── service
│                    ├── ApplicationsTest.java
│                    ├── EmployersTest.java
│                    ├── JobSeekersTest.java
│                    ├── JobTest.java
│                    ├── NotificationsTest.java
│                    ├── ResumeTest.java
│                    └── UserTest.java
└──pom.xml
``````
---

![Scipt flow](Images/RevHireFLow.png)
---

![Scipt flow](Images/RevHire_Application.png)
---

---

## 🗃️ ER Diagram (Database Design)

![ER Diagram](Images/ERD_DIagram.png)

---

## 🛠️ Technologies Used

- **Java** (OOP, Collections, Streams)
- **JDBC** (Oracle / MySQL)
- **Log4j2** (Logging)
- **JUnit** (Unit Testing)
- **Git** (Version Control)

---

## 📝 Logging

- Configured using **Log4j2**
- Logs are **written only to files** (no console logging)
- Default log file location: `/logs/application.log`

---

## 🧪 Test Coverage

- Unit tests implemented using **JUnit**
- **Overall coverage:** 91%  
  ![Test Coverage](Images/Test_Coverage.png)

---

## ▶️ How to Run

1. Clone the repository
2. Configure the database connection in `DBConnection.java`
3. Run SQL scripts to create the required tables
4. Execute `Main.java`
5. Use the console-based menu to navigate the system

---

## 🎥 Demo Video

📺 **YouTube Demo:**  
[Watch Here](Link_Here)

---

## 📌 Future Enhancements

- Web-based UI (Spring Boot)
- REST APIs with JWT authentication
- Resume upload in PDF format
- Advanced job recommendation engine
- Microservices-based architecture

---

## 👤 Author

**Ameer Shaikh**  
*Java | JDBC | Log4j | Backend Development*  



