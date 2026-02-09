package com.revhire.menu;

import com.revhire.model.Application;
import com.revhire.model.Job;
import com.revhire.service.impl.*;
import com.revhire.util.ProfileUtil;
import com.revhire.util.ValidationUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Scanner;

import static java.awt.SystemColor.text;

public class JobSeekerMenu {

    private static final Logger logger = LogManager.getLogger(JobSeekerMenu.class);

    public static void start(
            Scanner sc,
            int userId,
            int seekerId,
            JobServiceImpl jobService,
            ApplicationServiceImpl applicationService,
            NotificationsServiceImpl notificationService,
            UserServiceImpl userService,
            JobSeekerServiceImpl jobSeekerService,
            ResumeServiceImpl resumeService
    ) {

        logger.info("Job Seeker Menu started | userId={}", userId);

        while (true) {

            int completion = ProfileUtil.calculateJobSeekerCompletion(seekerId);
            int unreadCount = notificationService.getUnreadNotificationCount(userId);

            if (unreadCount > 0) {
                logger.info("User has {} unread notifications | userId={}", unreadCount, userId);
                System.out.println("🔔 You have " + unreadCount + " new notifications");
            }

            logger.info(
                    "Displaying Job Seeker Dashboard | userId={}, profileCompletion={}",
                    userId, completion
            );

            System.out.println("\n--- Job Seeker Dashboard ---");
            if (completion < 100) {
                System.out.println(" Profile Completion: " + completion + "%");
            }

            System.out.println("""
                    1. View Notifications
                    2. Create / Update Resume
                    3. Manage Profile
                    4. Search Jobs
                    5. Apply for Job
                    6. View My Applications
                    7. Withdraw Application
                    8. Change Password
                    9. Logout
                    """);
            System.out.print("Choice: ");

            int ch = sc.nextInt();
            sc.nextLine();
            System.out.println();

            switch (ch) {

                // ================= VIEW NOTIFICATIONS =================
                case 1 -> {
                    List<String> notes = notificationService.showUnreadNotifications(userId);

                    if (notes.isEmpty()) {
                        logger.info("No new notifications | userId={}", userId);
                        System.out.println(" No new notifications.");
                    } else {
                        logger.info("Showing {} notifications | userId={}", notes.size(), userId);
                        notes.forEach(System.out::println);
                        notificationService.markAllAsRead(userId);
                        logger.info("Marked notifications as read | userId={}", userId);
                    }

                    System.out.println("\nPress Enter to return to dashboard...");
                    sc.nextLine();
                }

                // ================= RESUME =================
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

                    resumeService.saveOrUpdateResume(
                            seekerId, obj, edu, exp, skills, projects
                    );
                    logger.info("Resume updated | seekerId={}", seekerId);
                }

                // ================= MANAGE PROFILE =================
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
                            System.out.println("\n Invalid number. Experience not updated.\n");
                        }
                    }

                    jobSeekerService.updateJobSeekerProfile(
                            seekerId, name, phone, location, totalExp
                    );
                    logger.info("Profile updated | seekerId={}", seekerId);
                }

                // ================= SEARCH JOBS =================
                case 4 -> {
                    System.out.println("\n=== Job Search Filters (Leave blank to skip) ===");
                    System.out.print("Job Title: ");
                    String title = sc.nextLine();
                    System.out.print("Location: ");
                    String loc = sc.nextLine();
                    System.out.print("Max Experience: ");
                    String expInput = sc.nextLine();
                    Integer maxExp = expInput.isBlank() ? null : Integer.parseInt(expInput);
                    System.out.print("Company Name: ");
                    String company = sc.nextLine();
                    System.out.print("Salary: ");
                    String salary = sc.nextLine();
                    System.out.print("Job Type: ");
                    String jobType = sc.nextLine();

                    List<Job> results = jobService.searchJobs(title, loc, maxExp, company, salary, jobType);

                    if (results.isEmpty()) {
                        System.out.println("\n[!] No jobs found matching those criteria.");
                    } else {
                        System.out.println("\n" + "=".repeat(110));
                        System.out.printf("%-5s | %-25s | %-20s | %-15s | %-12s | %-10s%n", "ID", "JOB TITLE", "COMPANY", "LOCATION", "SALARY", "TYPE");
                        System.out.println("-".repeat(110));

                        results.forEach(job -> {
                            System.out.printf("%-5d | %-25s | %-20s | %-15s | %-12s | %-10s%n",
                                    job.getJobId(),
                                    (job.getTitle().length() > 25 ? job.getTitle().substring(0, 22) + "..." : job.getTitle()),
                                    (job.getCompanyName().length() > 20 ? job.getCompanyName().substring(0, 17) + "..." : job.getCompanyName()),
                                    job.getLocation(),
                                    job.getSalary(),
                                    job.getJobType()
                            );
                        });
                        System.out.println("=".repeat(110));
                    }
                    System.out.println("\nPress Enter to return...");
                    sc.nextLine();
                }

                // ================= APPLY FOR JOB =================
                case 5 -> {
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

                    logger.info(
                            "Applied for job | seekerId={}, jobId={}",
                            seekerId, jobId
                    );
                }

                // ================= VIEW APPLICATIONS =================
                case 6 -> {
                    List<Application> apps = applicationService.viewMyApplications(seekerId);

                    // Original logging preserved
                    logger.info(
                            "Viewed applications | seekerId={}, count={}",
                            seekerId, apps.size()
                    );

                    if (apps.isEmpty()) {
                        System.out.println("\n[!] No applications found.");
                    } else {
                        // Professional Formatting Logic
                        System.out.println("\n" + "=".repeat(90));
                        System.out.printf("║ %-10s │ %-10s │ %-15s │ %-30s%n",
                                "APP ID", "JOB ID", "STATUS", "APPLIED DATE");
                        System.out.println("╟" + "─".repeat(12) + "┼" + "─".repeat(12) + "┼" + "─".repeat(17) + "┼" + "─".repeat(32) + "╢");

                        apps.forEach(app -> {
                            System.out.printf("║ %-10d │ %-10d │ %-15s │ %-30s ║%n",
                                    app.getApplicationId(),
                                    app.getJobId(),
                                    app.getStatus(),
                                    app.getAppliedAt()
                            );
                        });
                        System.out.println("=".repeat(90));
                    }

                    System.out.println("\nPress Enter to return to dashboard...");
                    sc.nextLine();
                }

                // ================= WITHDRAW APPLICATION =================
                case 7 -> {
                    System.out.print("Application ID: ");
                    int applicationId = sc.nextInt();
                    sc.nextLine();

                    System.out.print("Reason for withdrawal (optional): ");
                    String reason = sc.nextLine();
                    if (reason.isBlank()) reason = "Withdrawn by candidate";

                    applicationService.withdrawApplication(
                            applicationId, "WITHDRAWN", reason
                    );

                    logger.info(
                            "Application withdrawn | applicationId={}, seekerId={}",
                            applicationId, seekerId
                    );
                }

                // ================= CHANGE PASSWORD =================
                case 8 -> {
                    try {
                        System.out.print("Enter Current Password: ");
                        String currentPassword = sc.nextLine().trim();

                        if (currentPassword.isEmpty()) {
                            System.out.println("\n Current password cannot be empty.\n");
                            break;
                        }

                        String newPassword;
                        while (true) {
                            System.out.print("Enter New Password: ");
                            newPassword = sc.nextLine().trim();

                            if (newPassword.isEmpty()) {
                                System.out.println("\n New password cannot be empty.\n");
                                continue;
                            }

                            if (currentPassword.equals(newPassword)) {
                                System.out.println(
                                        "\n New password cannot be the same as current.\n"
                                );
                                continue;
                            }

                            if (!ValidationUtil.isValidPassword(newPassword)) {
                                System.out.println("""
                                         Weak password.
                                        - 8+ characters
                                        - Uppercase
                                        - Lowercase
                                        - Digit
                                        - Special character
                                        """);
                                continue;
                            }
                            break;
                        }

                        boolean changed =
                                userService.changePassword(
                                        userId, currentPassword, newPassword
                                );

                        if (changed) {
                            System.out.println("\n Password changed successfully.\n");
                        } else {
                            System.out.println("\n Current password incorrect.\n");
                        }

                    } catch (Exception e) {
                        logger.error(
                                "Exception while changing password | userId={}",
                                userId, e
                        );
                        System.out.println(
                                "\n  Error while changing password.\n"
                        );
                    }
                }

                // ================= LOGOUT =================
                case 9 -> {
                    logger.info("User logged out | userId={}", userId);
                    System.out.println("\nLogged out.");
                    return;
                }

                default -> {
                    logger.warn(
                            "Invalid Job Seeker menu option | userId={}, choice={}",
                            userId, ch
                    );
                    System.out.println("\n Invalid option.");
                }
            }
        }
    }

    private static String truncate(String text, int size) {
        if (text != null && text.length() > size) {
            return text.substring(0, size - 3) + "...";
        }
        return text;
    }

}
