package com.revhire.main;

import com.revhire.model.Application;
import com.revhire.model.Notification;
import com.revhire.service.*;
import com.revhire.model.User;
import com.revhire.util.HashUtil;
import com.revhire.util.ProfileUtil;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static final Logger logger = LogManager.getLogger(Main.class);

    static final String[] SECURITY_QUESTIONS = {
            "What is your favorite color?",
            "What is your birth city?",
            "What is your mother’s maiden name?",
            "What was the name of your first pet?",
            "What is your favorite movie?"
    };


    public static void main(String[] args) {
        logger.info("RevHire Job Portal Application Started");

        try (Scanner sc = new Scanner(System.in)) {
            ApplicationService applicationService = new ApplicationService();
            EmployersService employerService = new EmployersService();
            JobService jobService = new JobService();
            JobSeekerService jobSeekerService = new JobSeekerService();
            NotificationsService notificationService = new NotificationsService();
            UserService userService = new UserService();
            ResumeService resumeService = new ResumeService();
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
                    logger.warn("Invalid input in main menu", e);
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
                            logger.warn("Failed login attempt | email={}", email);
                            continue;
                        }

                        int userId = user.getUserId();
                        logger.info("User logged in | userId={}, role={}", userId, user.getRole());

                        if (user.getRole().equals("JOB_SEEKER")) {
                            int seekerId = jobSeekerService.getSeekerIdByUserId(userId);
                            logger.info("Redirecting to Job Seeker Dashboard | userId={}, seekerId={}", userId, seekerId);
                            jobSeekerMenu(sc, userId, seekerId,
                                    jobService, applicationService,
                                    notificationService, userService,
                                    jobSeekerService, resumeService);
                        } else {
                            int employerId = employerService.getEmployerIdByUserId(userId);
                            logger.info("Redirecting to Employer Dashboard | userId={}, employerId={}", userId, employerId);
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
                        logger.info("New user registered | userId={}, role={}", userId, role);
                    }

                    // ================= FORGOT PASSWORD =================
                    case 3 -> {
                        System.out.print("Email: ");
                        String email = sc.nextLine();

                        String question = userService.getSecurityQuestionByEmail(email);
                        if (question == null) {
                            System.out.println("❌ Email not found.");
                            logger.warn("Forgot password attempt failed | email={}", email);
                            continue;
                        }

                        System.out.println(question);
                        System.out.print("Answer: ");
                        String ans = sc.nextLine();

                        if (userService.verifySecurityAnswer(email, ans)) {
                            System.out.print("New Password: ");
                            userService.resetPassword(email, ans, sc.nextLine());
                            System.out.println("✅ Password reset successful.");
                            logger.info("Password reset successful | email={}", email);
                        } else {
                            System.out.println("❌ Incorrect answer.");
                            logger.warn("Password reset failed due to wrong answer | email={}", email);
                        }
                    }

                    case 4 -> {
                        System.out.println("👋 Goodbye!");
                        logger.info("Application exited by user");
                        return;
                    }

                    default -> {
                        System.out.println("❌ Invalid choice.");
                        logger.warn("Invalid main menu choice entered: {}", choice);
                    }
                }
            }
        } catch (Exception e) {
            logger.error("Unexpected error in main execution", e);
        } finally {
            logger.info("Scanner closed, application ended");
        }
    }

    // ==============EMPLOYER MENU==============
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
    ) {
        logger.info("Job Seeker Menu started | userId={}", userId);

        while (true) {
            // Calculate profile completion dynamically
            int completion = ProfileUtil.calculateJobSeekerCompletion(seekerId);
            int unreadCount = notificationService.getUnreadNotificationCount(userId);

            if (unreadCount > 0) {
                logger.info("User has {} unread notifications | userId={}", unreadCount, userId);
                System.out.println("🔔 You have " + unreadCount + " new notifications");
            }

            logger.info("Displaying Job Seeker Dashboard | userId={}, profileCompletion={}", userId, completion);

            // Dashboard display (still prints to console for user input)
            System.out.println("\n--- Job Seeker Dashboard ---");
            if (completion < 100) {
                System.out.println("📊 Profile Completion: " + completion + "%");
            }
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
                        logger.info("No new notifications | userId={}", userId);
                        System.out.println("📭 No new notifications.");
                    } else {
                        logger.info("Showing {} notifications | userId={}", notes.size(), userId);
                        for (String note : notes) {
                            System.out.println(note);
                        }
                        notificationService.markAllAsRead(userId);
                        logger.info("Marked all notifications as read | userId={}", userId);
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
                    logger.info("Resume updated | seekerId={}", seekerId);
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
                            logger.warn("Invalid experience input | userId={}", userId);
                            System.out.println("\n❌ Invalid number. Experience not updated.\n");
                        }
                    }

                    jobSeekerService.updateJobSeekerProfile(seekerId, name, phone, location, totalExp);
                    logger.info("Profile updated | seekerId={}", seekerId);
                }

                case 4 -> {
                    logger.info("Viewing all jobs | userId={}", userId);
                    jobService.getAllJobs().forEach(System.out::println);
                }

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

                    List<String> results = jobService.searchJobs(title, location, maxExp, company, salary, jobType);
                    logger.info("Job search performed | userId={}, results={}", userId, results.size());

                    if (results.isEmpty()) {
                        System.out.println("\n⚠️ No jobs found.\n");
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

                    logger.info("Applied for job | seekerId={}, jobId={}", seekerId, jobId);
                }

                case 7 -> {
                    List<String> applications = applicationService.viewMyApplications(seekerId);
                    logger.info("Viewed applications | seekerId={}, count={}", seekerId, applications.size());
                    applications.forEach(System.out::println);
                }

                case 8 -> {
                    System.out.print("Application ID: ");
                    int applicationId = sc.nextInt();
                    sc.nextLine();
                    System.out.print("Reason for withdrawal (optional): ");
                    String reason = sc.nextLine();
                    if (reason.isBlank()) reason = "Withdrawn by candidate";

                    applicationService.withdrawApplication(applicationId, "WITHDRAWN", reason);
                    logger.info("Application withdrawn | applicationId={}, seekerId={}", applicationId, seekerId);
                }

                case 9 -> {
                    System.out.print("Current Password: ");
                    String current = sc.nextLine();
                    System.out.print("New Password: ");
                    String newPwd = sc.nextLine();

                    boolean changed = userService.changePassword(userId, current, newPwd);
                    if (changed) {
                        logger.info("Password changed successfully | userId={}", userId);
                        System.out.println("\n✅ Password changed successfully.\n");
                    } else {
                        logger.warn("Password change failed (incorrect current) | userId={}", userId);
                        System.out.println("\n❌ Current password incorrect.\n");
                    }
                }

                case 10 -> {
                    logger.info("User logged out | userId={}", userId);
                    System.out.println("\nLogged out.");
                    return;
                }

                default -> {
                    logger.warn("Invalid menu option selected | userId={}, choice={}", userId, ch);
                    System.out.println("\n❌ Invalid option.");
                }
            }
        }
    }

// ==============EMPLOYER MENU==============
    static void employerMenu(
            Scanner sc,
            int userId,
            int employerId,
            JobService jobService,
            ApplicationService applicationService,
            EmployersService employerService,
            NotificationsService notificationService,
            UserService userService
    ) {
        logger.info("Employer Menu started | userId={}, employerId={}", userId, employerId);

        while (true) {
            int completion = ProfileUtil.calculateEmployerCompletion(employerId);
            List<Notification> unread = notificationService.fetchUnreadNotifications(userId);
            int unreadCount = notificationService.getUnreadNotificationCount(userId);

            if (!unread.isEmpty()) {
                logger.info("User has {} unread notifications | userId={}", unread.size(), userId);
                System.out.println("🔔 You have " + unreadCount + " new notifications");
            }

            System.out.println("\n--- Employer Dashboard ---");
            if (completion < 100) {
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
            System.out.println("10. Change Password");
            System.out.println("11. Logout");
            System.out.print("Choice: ");

            int ch = sc.nextInt();
            sc.nextLine();

            switch (ch) {

                case 1 -> {
                    List<String> notes = notificationService.showUnreadNotifications(userId);
                    if (notes.isEmpty()) {
                        logger.info("No new notifications | userId={}", userId);
                        System.out.println("📭 No new notifications.");
                    } else {
                        logger.info("Showing {} notifications | userId={}", notes.size(), userId);
                        for (String note : notes) {
                            System.out.println(note);
                        }
                        notificationService.markAllAsRead(userId);
                        logger.info("Marked all notifications as read | userId={}", userId);
                    }
                    System.out.println("\nPress Enter to return to dashboard...");
                    sc.nextLine();
                }

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

                    jobService.addJob(employerId, title, desc, skills, exp, edu, location, salary, type, deadline);
                    logger.info("Job posted | employerId={}, title={}", employerId, title);
                    System.out.println();
                }

                case 3 -> {
                    List<String> jobs = jobService.getAllJobsOfEmployer(employerId);
                    logger.info("Viewing all jobs | employerId={}, count={}", employerId, jobs.size());
                    jobs.forEach(System.out::println);
                    System.out.println();
                }

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
                    Date deadline = deadlineInput.isBlank() ? null : Date.valueOf(deadlineInput);

                    jobService.updateJob(
                            jobId, employerId,
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
                    logger.info("Job updated | employerId={}, jobId={}", employerId, jobId);
                    System.out.println();
                }

                case 5 -> {
                    System.out.print("Job ID: ");
                    int jobId = sc.nextInt();
                    sc.nextLine();
                    System.out.print("Status (OPEN / CLOSED): ");
                    String status = sc.nextLine().toUpperCase();
                    jobService.updateJobStatus(jobId, employerId, status);
                    logger.info("Job status updated | employerId={}, jobId={}, status={}", employerId, jobId, status);
                    System.out.println();
                }

                case 6 -> {
                    System.out.print("Job ID to Delete: ");
                    int jobId = sc.nextInt();
                    sc.nextLine();
                    jobService.deleteJob(jobId, employerId);
                    logger.info("Job deleted | employerId={}, jobId={}", employerId, jobId);
                    System.out.println();
                }

                case 7 -> {
                    System.out.print("Job ID: ");
                    int jobId = sc.nextInt();
                    sc.nextLine();
                    List<Application> applicants = applicationService.getApplicantsForJob(jobId);
                    logger.info("Viewing applicants | employerId={}, jobId={}, count={}", employerId, jobId, applicants.size());
                    applicants.forEach(System.out::println);
                    System.out.println();
                }

                case 8 -> {
                    System.out.print("Application ID: ");
                    int appId = sc.nextInt();
                    sc.nextLine();
                    System.out.print("Status (SHORTLISTED / REJECTED): ");
                    String status = sc.nextLine().toUpperCase();
                    applicationService.updateApplicationStatus(appId, status);

                    int seekerUserId = applicationService.getSeekerUserIdByApplicationId(appId);
                    int jobId = applicationService.getJobIdByApplicationId(appId);

                    if (seekerUserId != -1) {
                        notificationService.addNotification(
                                seekerUserId,
                                "Your application for Job ID " + jobId + " is now " + status
                        );
                    }
                    logger.info("Application status updated | employerId={}, appId={}, status={}", employerId, appId, status);
                    System.out.println();
                }

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

                    employerService.updateCompanyProfile(employerId, company, industry, companySize, description, website, location);
                    logger.info("Company profile updated | employerId={}", employerId);
                    System.out.println();
                }

                case 10 -> {
                    System.out.print("Current Password: ");
                    String current = sc.nextLine();
                    System.out.print("New Password: ");
                    String newPwd = sc.nextLine();

                    boolean changed = userService.changePassword(userId, current, newPwd);
                    if (changed) {
                        logger.info("Password changed successfully | userId={}", userId);
                        System.out.println("\n✅ Password changed successfully.\n");
                    } else {
                        logger.warn("Password change failed (incorrect current) | userId={}", userId);
                        System.out.println("\n❌ Current password incorrect.\n");
                    }
                }

                case 11 -> {
                    logger.info("User logged out | userId={}", userId);
                    System.out.println("\n🔓 Logged out.");
                    return;
                }

                default -> {
                    logger.warn("Invalid menu option selected | userId={}, choice={}", userId, ch);
                    System.out.println("\n❌ Invalid option.");
                }
            }
        }
    }
}
