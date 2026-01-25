package com.rev.hire;

import java.sql.Date;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        UserDAO userDAO = new UserDAO();
        JobSeekersDAO seekersDAO = new JobSeekersDAO();
        EmployersDAO employersDAO = new EmployersDAO();
        JobsDAO jobsDAO = new JobsDAO();
        ApplicationsDAO applicationsDAO = new ApplicationsDAO();

        while (true) {
            System.out.println("\n========= RevHire Job Portal =========");
            System.out.println("1. Login");
            System.out.println("2. Register");
            System.out.println("3. Exit");
            System.out.print("Enter choice: ");

            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {

                // 🔐 LOGIN
                case 1 -> {
                    System.out.print("Email: ");
                    String email = sc.nextLine();
                    System.out.print("Password: ");
                    String password = sc.nextLine();
                    User user = userDAO.login(email, password);

                    if (user == null) {
                        System.out.println("❌ Invalid credentials.");
                        continue;
                    }
                    System.out.println("✅ Login successful!");

                    if (user.getRole().equals("JOB_SEEKER")) {

                        int seekerId = seekersDAO.getSeekerIdByUserId(user.getUserId());
                        jobSeekerMenu(sc, seekerId, jobsDAO, applicationsDAO);

                    } else if (user.getRole().equals("EMPLOYER")) {

                        int employerId = employersDAO.getEmployerIdByUserId(user.getUserId());

                        // 🔥 IMPORTANT: correct method call
                        employerMenu(sc, employerId, jobsDAO, applicationsDAO, employersDAO);
                    }


                    System.out.println();
                }

                // 📝 REGISTER
                case 2 -> {
                    System.out.print("Email: ");
                    String email = sc.nextLine();
                    System.out.print("Password: ");
                    String password = sc.nextLine();
                    System.out.print("Role (JOB_SEEKER / EMPLOYER): ");
                    String role = sc.nextLine().toUpperCase();

                    int userId = userDAO.addUserAndReturnId(email, password, role);
                    if (userId == -1) {
                        System.out.println("❌ Registration failed.");
                        continue;
                    }

                    if (role.equals("JOB_SEEKER")) {
                        System.out.print("Full Name: ");
                        String name = sc.nextLine();
                        System.out.print("Phone: ");
                        String phone = sc.nextLine();
                        System.out.print("Location: ");
                        String location = sc.nextLine();
                        seekersDAO.addJobSeeker(userId, name, phone, location, 0, 'Y');
                    } else {
                        System.out.print("Company Name: ");
                        String company = sc.nextLine();
                        System.out.print("Industry: ");
                        String industry = sc.nextLine();
                        System.out.print("Company Size: ");
                        String compSize = sc.nextLine();
                        System.out.print("Description: ");
                        String desc = sc.nextLine();
                        System.out.print("website: ");
                        String website = sc.nextLine();
                        System.out.print("Location: ");
                        String location = sc.nextLine();
                        employersDAO.addEmployer(userId, company, industry, Integer.parseInt(compSize), desc, website, location);
                    }
                    System.out.println("✅ Registration successful! Please login.");
                    System.out.println();
                }
                // ❌ EXIT
                case 3 -> {
                    System.out.println("👋 Goodbye!");
                    sc.close();
                    return;
                }
                default -> System.out.println("❌ Invalid choice.");
            }
        }
    }

    static void jobSeekerMenu(Scanner sc, int seekerId,
                              JobsDAO jobsDAO,
                              ApplicationsDAO applicationsDAO) {

        while (true) {
            System.out.println("\n--- Job Seeker Dashboard ---");
            System.out.println("1. Create / Update Resume");
            System.out.println("2. Manage Profile");
            System.out.println("3. View All Jobs");
            System.out.println("4. Search Jobs with Filters");
            System.out.println("5. Apply for Job");
            System.out.println("6. View My Applications");
            System.out.println("7. Withdraw Application");
            System.out.println("8. Logout");
            System.out.println();
            System.out.print("Choice: ");

            int ch = sc.nextInt();
            sc.nextLine(); // consume newline
            System.out.println();

            switch (ch) {

                case 1 -> {
                    System.out.print("Objective: ");
                    String obj = sc.nextLine();

                    System.out.print("Education: ");
                    String edu = sc.nextLine();

                    System.out.print("Experience: ");
                    String exp = sc.nextLine();

                    System.out.print("Skills: ");
                    String skills = sc.nextLine();

                    System.out.print("Projects: ");
                    String projects = sc.nextLine();

                    ResumesDAO.saveOrUpdateResume(seekerId, obj, edu, exp, skills, projects);
                }

                case 2 -> {
                    System.out.println("=== Update Profile ===");

                    System.out.print("Full Name (leave empty to skip): ");
                    String name = sc.nextLine();

                    System.out.print("Phone (leave empty to skip): ");
                    String phone = sc.nextLine();

                    System.out.print("Location (leave empty to skip): ");
                    String location = sc.nextLine();

                    System.out.print("Total Experience (years, leave empty to skip): ");
                    String expInput = sc.nextLine();

                    Integer totalExp = null;
                    if (!expInput.isBlank()) {
                        try {
                            totalExp = Integer.parseInt(expInput);
                        } catch (NumberFormatException e) {
                            System.out.println("❌ Invalid number. Experience not updated.");
                        }
                    }

                    JobSeekersDAO.updateJobSeekerProfile(seekerId, name, phone, location, totalExp);
                }

                case 3 -> jobsDAO.getAllJobs().forEach(System.out::println);

                case 4 -> {
                    System.out.println("=== Job Search Filters ===");
                    System.out.print("Job Title: ");
                    String title = sc.nextLine();

                    System.out.print("Location: ");
                    String location = sc.nextLine();

                    System.out.print("Max Experience Required: ");
                    String expInput = sc.nextLine();
                    Integer maxExp = expInput.isBlank() ? null : Integer.parseInt(expInput);

                    System.out.print("Company Name: ");
                    String company = sc.nextLine();

                    System.out.print("Salary: ");
                    String salary = sc.nextLine();

                    System.out.print("Job Type: ");
                    String jobType = sc.nextLine();

                    List<String> results = jobsDAO.searchJobs(
                            title, location, maxExp, company, salary, jobType);

                    if (results.isEmpty()) {
                        System.out.println("⚠️ No jobs found.");
                    } else {
                        results.forEach(System.out::println);
                    }
                }

                case 5 -> {
                    System.out.print("Job ID: ");
                    int jobId = sc.nextInt();
                    sc.nextLine();
                    applicationsDAO.addApplication(jobId, seekerId);
                }

                case 6 -> applicationsDAO
                        .getApplicationsBySeeker(seekerId)
                        .forEach(System.out::println);

                case 7 -> {
                    System.out.print("Application ID: ");
                    int appId = sc.nextInt();
                    sc.nextLine();

                    System.out.print("Reason for withdrawal (optional): ");
                    String reason = sc.nextLine();

                    applicationsDAO.withdrawApplication(appId, reason);
                }

                case 8 -> {
                    System.out.println("🔓 Logged out.");
                    return;
                }

                default -> System.out.println("❌ Invalid option.");
            }
        }
    }



    // ================= EMPLOYER DASHBOARD =================
    static void employerMenu(
            Scanner sc,
            int employerId,
            JobsDAO jobsDAO,
            ApplicationsDAO applicationsDAO,
            EmployersDAO employersDAO
    ) {

        while (true) {
            System.out.println("\n--- Employer Dashboard ---");
            System.out.println("1. Post Job");
            System.out.println("2. View My Jobs");
            System.out.println("3. Edit Job");
            System.out.println("4. Close / Reopen Job");
            System.out.println("5. Delete Job");
            System.out.println("6. View Applicants");
            System.out.println("7. Update Application Status");
            System.out.println("8. Update Company Profile");
            System.out.println("9. Logout");
            System.out.print("Choice: ");

            int ch = sc.nextInt();
            sc.nextLine();

            switch (ch) {

                // ---------------- POST JOB ----------------
                case 1 -> {
                    System.out.print("Job Title: ");
                    String title = sc.nextLine();

                    System.out.print("Description: ");
                    String desc = sc.nextLine();

                    System.out.print("Skills Required: ");
                    String skills = sc.nextLine();

                    System.out.print("Experience Required: ");
                    int exp = sc.nextInt();
                    sc.nextLine();

                    System.out.print("Education Required: ");
                    String edu = sc.nextLine();

                    System.out.print("Location: ");
                    String location = sc.nextLine();

                    System.out.print("Salary: ");
                    String salary = sc.nextLine();

                    System.out.print("Job Type: ");
                    String type = sc.nextLine();

                    System.out.print("Deadline (YYYY-MM-DD): ");
                    Date deadline = Date.valueOf(sc.nextLine());

                    jobsDAO.addJob(
                            employerId, title, desc, skills,
                            exp, edu, location, salary, type, deadline
                    );
                }

                // ---------------- VIEW JOBS ----------------
                case 2 -> jobsDAO
                        .getAllJobsOfEmployer(employerId)
                        .forEach(System.out::println);

                // ---------------- EDIT JOB ----------------
                case 3 -> {
                    System.out.print("Job ID to Edit: ");
                    int jobId = sc.nextInt();
                    sc.nextLine();

                    System.out.println("⚠️ Leave any field empty to keep it unchanged");

                    System.out.print("New Title: ");
                    String title = sc.nextLine();

                    System.out.print("New Description: ");
                    String desc = sc.nextLine();

                    System.out.print("New Skills Required: ");
                    String skills = sc.nextLine();

                    System.out.print("New Experience Required (years): ");
                    String expInput = sc.nextLine();
                    Integer exp = expInput.isBlank() ? null : Integer.parseInt(expInput);

                    System.out.print("New Education Required: ");
                    String edu = sc.nextLine();

                    System.out.print("New Location: ");
                    String location = sc.nextLine();

                    System.out.print("New Salary: ");
                    String salary = sc.nextLine();

                    System.out.print("New Job Type: ");
                    String jobType = sc.nextLine();

                    System.out.print("New Deadline (YYYY-MM-DD): ");
                    String deadlineInput = sc.nextLine();
                    Date deadline = deadlineInput.isBlank()
                            ? null
                            : Date.valueOf(deadlineInput);

                    jobsDAO.updateJob(
                            jobId,
                            employerId,
                            title.isBlank() ? null : title,
                            desc.isBlank() ? null : desc,
                            skills.isBlank() ? null : skills,
                            exp,
                            edu.isBlank() ? null : edu,
                            location.isBlank() ? null : location,
                            salary.isBlank() ? null : salary,
                            jobType.isBlank() ? null : jobType,
                            deadline
                    );
                }


                // ---------------- CLOSE / REOPEN ----------------
                case 4 -> {
                    System.out.print("Job ID: ");
                    int jobId = sc.nextInt();
                    sc.nextLine();

                    System.out.print("Status (OPEN / CLOSED): ");
                    String status = sc.nextLine().toUpperCase();

                    jobsDAO.updateJobStatus(jobId, employerId, status);
                }

                // ---------------- DELETE JOB ----------------
                case 5 -> {
                    System.out.print("Job ID to Delete: ");
                    int jobId = sc.nextInt();
                    sc.nextLine();

                    jobsDAO.deleteJob(jobId, employerId);
                }

                // ---------------- VIEW APPLICANTS ----------------
                case 6 -> {
                    System.out.print("Job ID: ");
                    int jobId = sc.nextInt();
                    sc.nextLine();

                    applicationsDAO
                            .getApplicantsForJob(jobId, employerId)
                            .forEach(System.out::println);
                }

                // ---------------- SHORTLIST / REJECT ----------------
                case 7 -> {
                    System.out.print("Application ID: ");
                    int appId = sc.nextInt();
                    sc.nextLine();

                    System.out.print("Status (SHORTLISTED / REJECTED): ");
                    String status = sc.nextLine().toUpperCase();

                    int userId = applicationsDAO.getUserIdByApplicationId(appId);

                    applicationsDAO.updateApplicationStatus(appId, status, userId);
                }



                // ---------------- UPDATE COMPANY ----------------
                case 8 -> {
                    System.out.println("Leave blank to keep existing value");

                    System.out.print("Industry: ");
                    String industryInput = sc.nextLine();
                    String industry = industryInput.isBlank() ? null : industryInput;

                    System.out.print("Company Size: ");
                    String sizeInput = sc.nextLine();
                    Integer companySize = sizeInput.isBlank() ? null : Integer.parseInt(sizeInput);

                    System.out.print("Company Description: ");
                    String descInput = sc.nextLine();
                    String description = descInput.isBlank() ? null : descInput;

                    System.out.print("Website: ");
                    String websiteInput = sc.nextLine();
                    String website = websiteInput.isBlank() ? null : websiteInput;

                    System.out.print("Location: ");
                    String locationInput = sc.nextLine();
                    String location = locationInput.isBlank() ? null : locationInput;

                    employersDAO.updateCompanyProfile(
                            employerId,
                            industry,
                            companySize,
                            description,
                            website,
                            location
                    );
                }

                case 9 -> {
                    System.out.println("🔓 Logged out.");
                    return;
                }

                default -> System.out.println("❌ Invalid option.");
            }
        }
    }
}
