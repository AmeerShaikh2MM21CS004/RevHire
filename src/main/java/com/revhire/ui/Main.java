package com.revhire.ui;

import com.revhire.model.Notification;
import com.revhire.service.*;
import com.revhire.model.User;
import com.revhire.util.HashUtil;
import com.revhire.util.ProfileUtil;

import java.sql.*;
import java.util.List;
import java.util.Scanner;

public class Main {

    static final String[] SECURITY_QUESTIONS = {
            "What is your favorite color?",
            "What is your birth city?",
            "What is your mother’s maiden name?",
            "What was the name of your first pet?",
            "What is your favorite movie?"
    };


    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        ApplicationService applicationService = new ApplicationService();
        EmployersService employerService = new EmployersService();
        JobService jobService = new JobService();
        JobSeekerService jobSeekerService = new JobSeekerService();
        NotificationsService notificationService = new NotificationsService();
        UserService userService= new UserService();
        ResumeService resumeService=new ResumeService();

        while (true) {
            System.out.println("\n========= RevHire Job Portal =========");
            System.out.println("1. Login");
            System.out.println("2. Register");
            System.out.println("3. Forgot Password");
            System.out.println("4. Exit");
            System.out.print("Enter choice: ");

            int choice;
            try {
                choice = Integer.parseInt(sc.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("❌ Invalid input.");
                continue;
            }

            switch (choice) {

                // ================= LOGIN =================
                case 1 -> {
                    System.out.print("Email: ");
                    String email = sc.nextLine();

                    System.out.print("Password: ");
                    String password = sc.nextLine();

                    User user = userService.login(email, password);
                    if (user == null) {
                        System.out.println("❌ Invalid credentials.");
                        continue;
                    }

                    int userId = user.getUserId();

                    if (user.getRole().equals("JOB_SEEKER")) {
                        int seekerId = jobSeekerService.getSeekerIdByUserId(userId);
                        jobSeekerMenu(sc, userId, seekerId,
                                jobService, applicationService,
                                notificationService, userService,
                                jobSeekerService, resumeService);
                    } else {
                        int employerId = employerService.getEmployerIdByUserId(userId);
                        employerMenu(sc, userId, employerId,
                                jobService, applicationService,
                                employerService, notificationService,
                                userService);
                    }
                }

                // ================= REGISTER =================
                case 2 -> {
                    System.out.print("Email: ");
                    String email = sc.nextLine();

                    System.out.print("Password: ");
                    String password = sc.nextLine();

                    System.out.print("Role (JOB_SEEKER / EMPLOYER): ");
                    String role = sc.nextLine().toUpperCase();

                    System.out.println("Choose security question:");
                    for (int i = 0; i < SECURITY_QUESTIONS.length; i++) {
                        System.out.println((i + 1) + ". " + SECURITY_QUESTIONS[i]);
                    }

                    int q = Integer.parseInt(sc.nextLine());
                    System.out.print("Answer: ");
                    String ans = sc.nextLine();

                    int userId = userService.addUserAndReturnId(
                            email,
                            HashUtil.hash(password),
                            role,
                            SECURITY_QUESTIONS[q - 1],
                            HashUtil.hash(ans)
                    );

                    if (role.equals("JOB_SEEKER")) {
                        jobSeekerService.createJobSeeker(userId);
                    } else {
                        employerService.createEmployer(userId);
                    }

                    System.out.println("✅ Registration successful!");
                }

                // ================= FORGOT PASSWORD =================
                case 3 -> {
                    System.out.print("Email: ");
                    String email = sc.nextLine();

                    String question = userService.getSecurityQuestionByEmail(email);
                    if (question == null) {
                        System.out.println("❌ Email not found.");
                        continue;
                    }

                    System.out.println(question);
                    System.out.print("Answer: ");
                    String ans = sc.nextLine();

                    if (userService.verifySecurityAnswer(email, ans)) {
                        System.out.print("New Password: ");
                        userService.resetPassword(email, ans, sc.nextLine());
                        System.out.println("✅ Password reset successful.");
                    } else {
                        System.out.println("❌ Incorrect answer.");
                    }
                }

                case 4 -> {
                    System.out.println("👋 Goodbye!");
                    return;
                }
            }
        }
    }

    static void jobSeekerMenu(
            Scanner sc,
            int userId,
            int seekerId,
            JobService jobService,
            ApplicationService applicationService,
            NotificationsService notificationService,
            UserService userService,
            JobSeekerService jobSeekerService,
            ResumeService resumeService
    )
    {
        System.out.println(userId);
        while (true) {
            // Calculate profile completion dynamically
            int completion = ProfileUtil.calculateJobSeekerCompletion(seekerId);

            int unreadCount = notificationService.getUnreadNotificationCount(userId);

            if (unreadCount > 0) {
                System.out.println("🔔 You have " + unreadCount + " new notifications");
            }

            System.out.println("\n--- Job Seeker Dashboard ---");
            if(completion<100) {
                System.out.println("📊 Profile Completion: " + completion + "%");
            }
            System.out.println();
            System.out.println("1. View Notification");
            System.out.println("2. Create / Update Resume");
            System.out.println("3. Manage Profile");
            System.out.println("4. View All Jobs");
            System.out.println("5. Search Jobs with Filters");
            System.out.println("6. Apply for Job");
            System.out.println("7. View My Applications");
            System.out.println("8. Withdraw Application");
            System.out.println("9. Change Password");
            System.out.println("10. Logout");
            System.out.print("Choice: ");

            int ch = sc.nextInt();
            sc.nextLine();
            System.out.println();

            switch (ch) {

                case 1 -> {
                    List<String> notes = notificationService.showUnreadNotifications(userId);

                    if (notes.isEmpty()) {
                        System.out.println("📭 No new notifications.");
                    } else {
                        for (String note : notes) {
                            System.out.println(note);
                        }
                        notificationService.markAllAsRead(userId);
                    }

                    System.out.println("\nPress Enter to return to dashboard...");
                    sc.nextLine();
                }

                case 2 -> {
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
                    System.out.println();

                    resumeService.saveOrUpdateResume(seekerId, obj, edu, exp, skills, projects);
                }

                case 3 -> {
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
                            System.out.println();
                            System.out.println("❌ Invalid number. Experience not updated.");
                            System.out.println();
                        }
                    }

                    jobSeekerService.updateJobSeekerProfile(seekerId, name, phone, location, totalExp);
                }

                case 4 -> jobService.getAllJobs().forEach(System.out::println);

                case 5 -> {
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

                    List<String> results = jobService.searchJobs(
                            title, location, maxExp, company, salary, jobType);

                    if (results.isEmpty()) {
                        System.out.println();
                        System.out.println("⚠️ No jobs found.");
                        System.out.println();
                    } else {
                        results.forEach(System.out::println);
                    }
                }

                case 6 -> {
                    System.out.print("Job ID: ");
                    int jobId = sc.nextInt();
                    sc.nextLine();

                    applicationService.applyForJob(jobId, seekerId);

                    int employerUserId = jobService.getEmployerUserIdByJob(jobId);

                    if (employerUserId != -1) {
                        notificationService.addNotification(
                                employerUserId,
                                "New application received for Job ID: " + jobId
                        );
                    }
                }

                case 7 -> applicationService.viewMyApplications(seekerId)
                        .forEach(System.out::println);

                case 8 -> {
                    System.out.print("Application ID: ");
                    int applicationId = sc.nextInt();
                    sc.nextLine();

                    System.out.print("Reason for withdrawal (optional): ");
                    String reason = sc.nextLine();

                    // Convert empty input to null
                    if (reason.isBlank()) {
                        reason = "Withdrawn by candidate";
                    }

                    applicationService.withdrawApplication(applicationId, "WITHDRAWN", reason);
                }

                case 9 -> {
                    System.out.print("Current Password: ");
                    String current = sc.nextLine();

                    System.out.print("New Password: ");
                    String newPwd = sc.nextLine();

                    if (userService.changePassword(userId, current, newPwd)) {
                        System.out.println();
                        System.out.println("✅ Password changed successfully.");
                        System.out.println();
                    } else {
                        System.out.println();
                        System.out.println("❌ Current password incorrect.");
                        System.out.println();
                    }
                }

                case 10 -> {
                    System.out.println();
                    System.out.println("Logged out.");
                    return;
                }

                default -> {    System.out.println();
                    System.out.println("❌ Invalid option.");
                }
            }
        }
    }

    // ================= EMPLOYER DASHBOARD =================
    static void employerMenu(
            Scanner sc,
            int userId,
            int employerId,
            JobService jobService,
            ApplicationService applicationService,
            EmployersService employerService,
            NotificationsService notificationService,
            UserService userService
    )
    {

        while (true) {
            int completion = ProfileUtil.calculateEmployerCompletion(employerId);

            List<Notification> unread = notificationService.fetchUnreadNotifications(userId);

            if (!unread.isEmpty()) {
                System.out.println("🔔 You have " + unread.size() + " new notifications");
            }


            System.out.println("\n--- Employer Dashboard ---");
            if(completion<100) {
                System.out.println("📊 Profile Completion: " + completion + "%");
            }
            System.out.println("1. View Notifications");
            System.out.println("2. Post Job");
            System.out.println("3. View My Jobs");
            System.out.println("4. Edit Job");
            System.out.println("5. Close / Reopen Job");
            System.out.println("6. Delete Job");
            System.out.println("7. View Applicants");
            System.out.println("8. Update Application Status");
            System.out.println("9. Update Company Profile");
            System.out.println("10. change Password");
            System.out.println("11. Logout");
            System.out.print("Choice: ");

            int ch = sc.nextInt();
            sc.nextLine();

            switch (ch) {

                case 1 -> {
                    List<String> notes = notificationService.showUnreadNotifications(userId);

                    if (notes.isEmpty()) {
                        System.out.println("📭 No new notifications.");
                    } else {
                        for (String note : notes) {
                            System.out.println(note);
                        }
                        notificationService.markAllAsRead(userId);
                    }

                    System.out.println("\nPress Enter to return to dashboard...");
                    sc.nextLine();
                }

                // ---------------- POST JOB ----------------
                case 2 -> {
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

                    jobService.addJob(
                            employerId, title, desc, skills,
                            exp, edu, location, salary, type, deadline
                    );
                    System.out.println();
                }

                // ---------------- VIEW JOBS ----------------
                case 3 -> {
                            jobService
                        .getAllJobsOfEmployer(employerId)
                        .forEach(System.out::println);
                        System.out.println();
                }

                // ---------------- EDIT JOB ----------------
                case 4 -> {
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

                    jobService.updateJob(
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
                    System.out.println();
                }


                // ---------------- CLOSE / REOPEN ----------------
                case 5 -> {
                    System.out.print("Job ID: ");
                    int jobId = sc.nextInt();
                    sc.nextLine();

                    System.out.print("Status (OPEN / CLOSED): ");
                    String status = sc.nextLine().toUpperCase();

                    jobService.updateJobStatus(jobId, employerId, status);
                    System.out.println();
                }

                // ---------------- DELETE JOB ----------------
                case 6 -> {
                    System.out.print("Job ID to Delete: ");
                    int jobId = sc.nextInt();
                    sc.nextLine();

                    jobService.deleteJob(jobId, employerId);
                    System.out.println();
                }

                // ---------------- VIEW APPLICANTS ----------------
                case 7 -> {
                    System.out.print("Job ID: ");
                    int jobId = sc.nextInt();
                    sc.nextLine();
                    System.out.println();

                    applicationService
                            .getApplicantsForJob(jobId)
                            .forEach(System.out::println);

                }

                // ---------------- SHORTLIST / REJECT ----------------
                case 8 -> {
                    System.out.print("Application ID: ");
                    int appId = sc.nextInt();
                    sc.nextLine();

                    System.out.print("Status (SHORTLISTED / REJECTED): ");
                    String status = sc.nextLine().toUpperCase();

                    // Update status
                    applicationService.updateApplicationStatus(appId, status);

                    // Notify Job Seeker
                    int seekerUserId =
                            applicationService.getSeekerUserIdByApplicationId(appId);

                    int jobId =
                            applicationService.getJobIdByApplicationId(appId);

                    if (seekerUserId != -1) {
                        notificationService.addNotification(
                                seekerUserId,
                                "Your application for Job ID " + jobId +
                                        " is now " + status
                        );
                    }
                System.out.println();
                }

                // ---------------- UPDATE COMPANY ----------------
                case 9 -> {
                    System.out.println("Leave blank to keep existing value");

                    System.out.print("Company Name: ");
                    String companyNameInput = sc.nextLine();
                    String company = companyNameInput.isBlank() ? null : companyNameInput;

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

                    employerService.updateCompanyProfile(
                            employerId,
                            company,
                            industry,
                            companySize,
                            description,
                            website,
                            location
                    );

                    System.out.println();
                }

                case 10 -> {
                    System.out.print("Current Password: ");
                    String current = sc.nextLine();

                    System.out.print("New Password: ");
                    String newPwd = sc.nextLine();

                    if (userService.changePassword(userId, current, newPwd)) {
                        System.out.println();
                        System.out.println("✅ Password changed successfully.");
                        System.out.println();
                    } else {
                        System.out.println();
                        System.out.println("❌ Current password incorrect.");
                        System.out.println();
                    }
                }

                case 11 -> {
                    System.out.println();
                    System.out.println("🔓 Logged out.");
                    return;
                }

                default -> {System.out.println();
                    System.out.println("❌ Invalid option.");
                }
            }
        }
    }
}
