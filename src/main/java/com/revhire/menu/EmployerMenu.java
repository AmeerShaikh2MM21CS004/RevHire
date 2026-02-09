package com.revhire.menu;

import com.revhire.model.Application;
import com.revhire.model.Job;
import com.revhire.model.Notification;
import com.revhire.service.impl.*;
import com.revhire.util.ProfileUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Date;
import java.util.List;
import java.util.Scanner;

public class EmployerMenu {

    private static final Logger logger =
            LogManager.getLogger(EmployerMenu.class);

    public static void start(
            Scanner sc,
            int userId,
            int employerId,
            JobServiceImpl jobService,
            ApplicationServiceImpl applicationService,
            EmployersServiceImpl employerService,
            NotificationsServiceImpl notificationService,
            UserServiceImpl userService
    ) {

        logger.info("Employer Menu started | userId={}, employerId={}", userId, employerId);

        while (true) {

            int completion = ProfileUtil.calculateEmployerCompletion(employerId);
            List<Notification> unread = notificationService.fetchUnreadNotifications(userId);
            int unreadCount = notificationService.getUnreadNotificationCount(userId);

            if (!unread.isEmpty()) {
                logger.info("User has {} unread notifications | userId={}", unread.size(), userId);
                System.out.println();
                System.out.println("🔔 You have " + unreadCount + " new notifications");
            }

            System.out.println("\n--- Employer Dashboard ---");
            if (completion < 100) {
                System.out.println(" Profile Completion: " + completion + "%");
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
                    List<String> notes =
                            notificationService.showUnreadNotifications(userId);

                    if (notes.isEmpty()) {
                        logger.info("No new notifications | userId={}", userId);
                        System.out.println(" No new notifications.");
                    } else {
                        logger.info("Showing {} notifications | userId={}", notes.size(), userId);
                        notes.forEach(System.out::println);
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

                    jobService.addJob(
                            employerId, title, desc, skills,
                            exp, edu, location, salary, type, deadline
                    );

                    logger.info("Job posted | employerId={}, title={}", employerId, title);
                }

                case 3 -> {
                    List<Job> jobs = jobService.getAllJobsOfEmployer(employerId);
                    logger.info("Viewing all jobs | employerId={}, count={}", employerId, jobs.size());

                    if (jobs.isEmpty()) {
                        System.out.println("\n[!] No jobs posted yet.");
                    } else {
                        System.out.println("\n" + "=".repeat(130));
                        System.out.printf(
                                "%-5s | %-25s | %-15s | %-5s | %-12s | %-12s | %-10s | %-12s | %-8s%n",
                                "ID", "JOB TITLE", "LOCATION", "EXP", "EDU",
                                "SALARY", "TYPE", "DEADLINE", "STATUS"
                        );
                        System.out.println("=".repeat(130));

                        jobs.forEach(job -> {
                            String title = job.getTitle().length() > 25 ? job.getTitle().substring(0, 22) + "..." : job.getTitle();

                            System.out.printf(
                                    "%-5d | %-25s | %-15s | %-5d | %-12s | %-12s | %-10s | %-12s | %-8s%n",
                                    job.getJobId(),
                                    title,
                                    job.getLocation(),
                                    job.getExperienceRequired(),
                                    job.getEducationRequired(),
                                    job.getSalary(),
                                    job.getJobType(),
                                    job.getDeadline(),
                                    job.getStatus()
                            );
                        });
                        System.out.println("-".repeat(130));
                    }
                    System.out.println("\nPress Enter to return to dashboard...");
                    sc.nextLine();
                }

                case 4 -> {
                    System.out.print("Job ID to Edit: ");
                    int jobId = sc.nextInt();
                    sc.nextLine();

                    System.out.println("️Leave any field empty to keep it unchanged");

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
                }

                case 5 -> {
                    System.out.print("Job ID: ");
                    int jobId = sc.nextInt();
                    sc.nextLine();
                    System.out.print("Status (OPEN / CLOSED): ");
                    String status = sc.nextLine().toUpperCase();

                    jobService.updateJobStatus(jobId, employerId, status);
                    logger.info("Job status updated | employerId={}, jobId={}, status={}",
                            employerId, jobId, status);
                }

                case 6 -> {
                    System.out.print("Job ID to Delete: ");
                    int jobId = sc.nextInt();
                    sc.nextLine();

                    jobService.deleteJob(jobId, employerId);
                    logger.info("Job deleted | employerId={}, jobId={}", employerId, jobId);
                }

                case 7 -> {
                    System.out.print("Job ID: ");
                    int jobId = sc.nextInt();
                    sc.nextLine();

                    List<Application> apps =
                            applicationService.getApplicantsForJob(jobId);

                    logger.info(
                            "Viewing applicants | employerId={}, jobId={}, count={}",
                            employerId, jobId, apps.size()
                    );

                    if (apps.isEmpty()) {
                        System.out.println("\n[!] No applications found.");
                    } else {

                        System.out.println("\n" + "=".repeat(120));
                        System.out.printf(
                                "%-8s | %-8s | %-20s | %-12s | %-23s | %-40s%n",
                                "APP ID", "JOB ID", "SEEKER NAME",
                                "STATUS", "APPLIED DATE", "WITHDRAW REASON"
                        );
                        System.out.println("-".repeat(120));

                        apps.forEach(app -> {

                            String reason =
                                    app.getWithdrawReason() != null
                                            ? app.getWithdrawReason()
                                            : "N/A";

                            String formattedReason =
                                    reason.length() > 40
                                            ? reason.substring(0, 37) + "..."
                                            : reason;

                            System.out.printf(
                                    "%-8d | %-8d | %-20s | %-12s | %-20s | %-40s%n",
                                    app.getApplicationId(),
                                    app.getJobId(),
                                    app.getSeekerName(),
                                    app.getStatus(),
                                    app.getAppliedAt(),
                                    formattedReason
                            );
                        });

                        System.out.println("=".repeat(120));
                    }

                    System.out.println("\nPress Enter to return to dashboard...");
                    sc.nextLine();
                }

                case 8 -> {
                    System.out.print("Application ID: ");
                    int appId = sc.nextInt();
                    sc.nextLine();

                    System.out.print("Status (SHORTLISTED / REJECTED): ");
                    String status = sc.nextLine().toUpperCase();

                    applicationService.updateApplicationStatus(appId, status);

                    int seekerId =
                            applicationService.getSeekerIdByApplicationId(appId);

                    int seekerUserId =
                            userService.getUserIdBySeekerId(seekerId);

                    int jobId =
                            applicationService.getJobIdByApplicationId(appId);

                    if (seekerUserId != -1) {
                        notificationService.addNotification(
                                seekerUserId,
                                "Your application for Job ID " + jobId + " is now " + status
                        );
                    }

                    logger.info(
                            "Application status updated | employerId={}, appId={}, status={}",
                            employerId, appId, status
                    );
                }

                case 9 -> {
                    System.out.println("Leave blank to keep existing value");

                    System.out.print("Company Name: ");
                    String company = sc.nextLine().trim();
                    System.out.print("Industry: ");
                    String industry = sc.nextLine().trim();
                    System.out.print("Company Size: ");
                    String sizeInput = sc.nextLine();
                    Integer size = sizeInput.isBlank() ? null : Integer.parseInt(sizeInput);
                    System.out.print("Company Description: ");
                    String desc = sc.nextLine().trim();
                    System.out.print("Website: ");
                    String website = sc.nextLine().trim();
                    System.out.print("Location: ");
                    String location = sc.nextLine().trim();

                    employerService.updateCompanyProfile(
                            employerId,
                            company.isBlank() ? null : company,
                            industry.isBlank() ? null : industry,
                            size,
                            desc.isBlank() ? null : desc,
                            website.isBlank() ? null : website,
                            location.isBlank() ? null : location
                    );

                    logger.info("Company profile updated | employerId={}", employerId);
                }

                case 10 -> {
                    try {
                        System.out.print("Enter Current Password: ");
                        String current = sc.nextLine().trim();
                        System.out.print("Enter New Password: ");
                        String next = sc.nextLine().trim();

                        if (current.isEmpty() || next.isEmpty()) {
                            System.out.println(" Password fields cannot be empty.");
                            break;
                        }

                        if (current.equals(next)) {
                            System.out.println(" New password cannot be same as current.");
                            break;
                        }

                        boolean changed =
                                userService.changePassword(userId, current, next);

                        System.out.println(
                                changed
                                        ? " Password changed successfully."
                                        : " Current password is incorrect."
                        );

                    } catch (Exception e) {
                        logger.error("Password change failed | userId={}", userId, e);
                        System.out.println(" Something went wrong.");
                    }
                }

                case 11 -> {
                    logger.info("User logged out | userId={}", userId);
                    System.out.println(" Logged out.");
                    return;
                }

                default -> {
                    logger.warn("Invalid option | userId={}, choice={}", userId, ch);
                    System.out.println(" Invalid option.");
                }
            }
        }
    }
}
