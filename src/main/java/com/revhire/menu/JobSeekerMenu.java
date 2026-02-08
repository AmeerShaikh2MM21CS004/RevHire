package com.revhire.menu;

import com.revhire.service.impl.*;
import com.revhire.util.ProfileUtil;
import com.revhire.util.ValidationUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Scanner;

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
                    4. View All Jobs
                    5. Search Jobs with Filters
                    6. Apply for Job
                    7. View My Applications
                    8. Withdraw Application
                    9. Change Password
                    10. Logout
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

                // ================= VIEW ALL JOBS =================
                case 4 -> {
                    logger.info("Viewing all jobs | userId={}", userId);
                    jobService.getAllJobs().forEach(System.out::println);
                    System.out.println("\nPress Enter to return to dashboard...");
                    sc.nextLine();
                }

                // ================= SEARCH JOBS =================
                case 5 -> {
                    System.out.println("=== Job Search Filters ===");

                    System.out.print("Job Title: ");
                    String title = sc.nextLine();
                    System.out.print("Location: ");
                    String loc = sc.nextLine();
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
                            title, loc, maxExp, company, salary, jobType
                    );

                    logger.info(
                            "Job search performed | userId={}, results={}",
                            userId, results.size()
                    );

                    if (results.isEmpty()) {
                        System.out.println("\n No jobs found.\n");
                    } else {
                        results.forEach(System.out::println);
                    }
                }

                // ================= APPLY FOR JOB =================
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

                    logger.info(
                            "Applied for job | seekerId={}, jobId={}",
                            seekerId, jobId
                    );
                }

                // ================= VIEW APPLICATIONS =================
                case 7 -> {
                    List<String> apps =
                            applicationService.viewMyApplications(seekerId);

                    logger.info(
                            "Viewed applications | seekerId={}, count={}",
                            seekerId, apps.size()
                    );

                    apps.forEach(System.out::println);
                    System.out.println("\nPress Enter to return to dashboard...");
                    sc.nextLine();
                }

                // ================= WITHDRAW APPLICATION =================
                case 8 -> {
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
                case 9 -> {
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
                case 10 -> {
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
}
