package com.revhire.menu;

import com.revhire.model.User;
import com.revhire.service.impl.*;
import com.revhire.util.HashUtil;
import com.revhire.util.ValidationUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Scanner;

public class MainMenu {

    private static final Logger logger = LogManager.getLogger(MainMenu.class);

    private static final String[] SECURITY_QUESTIONS = {
            "What is your favorite color?",
            "What is your birth city?",
            "What is your mother’s maiden name?",
            "What was the name of your first pet?",
            "What is your favorite movie?"
    };

    public static void start() {

        try (Scanner sc = new Scanner(System.in)) {

            // ================= Service Initialization =================
            ApplicationServiceImpl applicationService = new ApplicationServiceImpl();
            EmployersServiceImpl employerService = new EmployersServiceImpl();
            JobServiceImpl jobService = new JobServiceImpl();
            JobSeekerServiceImpl jobSeekerService = new JobSeekerServiceImpl();
            NotificationsServiceImpl notificationService = new NotificationsServiceImpl();
            UserServiceImpl userService = new UserServiceImpl();
            ResumeServiceImpl resumeService = new ResumeServiceImpl();

            logger.info("RevHire Application Started");

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
                    System.out.println(" Invalid input.");
                    logger.warn("Invalid menu input", e);
                    continue;
                }

                switch (choice) {

                    // ================= LOGIN =================
                    case 1 -> handleLogin(
                            sc, userService, jobSeekerService, employerService,
                            jobService, applicationService, notificationService, resumeService
                    );

                    // ================= REGISTER =================
                    case 2 -> handleRegistration(
                            sc, userService, jobSeekerService, employerService
                    );

                    // ================= FORGOT PASSWORD =================
                    case 3 -> handleForgotPassword(sc, userService);

                    // ================= EXIT =================
                    case 4 -> {
                        System.out.println(" Goodbye!");
                        logger.info("Application exited by user");
                        return;
                    }

                    default -> {
                        System.out.println(" Invalid choice.");
                        logger.warn("Invalid main menu choice: {}", choice);
                    }
                }
            }

        } catch (Exception e) {
            logger.error("Unexpected error in MainMenu", e);
        } finally {
            logger.info("Application shutdown complete");
        }
    }

    // ===================== LOGIN HANDLER ========================
    private static void handleLogin(
            Scanner sc,
            UserServiceImpl userService,
            JobSeekerServiceImpl jobSeekerService,
            EmployersServiceImpl employerService,
            JobServiceImpl jobService,
            ApplicationServiceImpl applicationService,
            NotificationsServiceImpl notificationService,
            ResumeServiceImpl resumeService
    ) {

        System.out.print("Email: ");
        String email = sc.nextLine();

        if (!ValidationUtil.isValidEmail(email)) {
            System.out.println(" Invalid email format.");
            logger.warn("Invalid email during login: {}", email);
            return;
        }

        System.out.print("Password: ");
        String password = sc.nextLine();

        User user = userService.login(email, password);
        if (user == null) {
            System.out.println(" Invalid credentials.");
            logger.warn("Login failed | email={}", email);
            return;
        }

        int userId = user.getUserId();
        logger.info("User logged in | userId={}, role={}", userId, user.getRole());

        if ("JOB_SEEKER".equals(user.getRole())) {

            int seekerId = jobSeekerService.getSeekerIdByUserId(userId);
            logger.info("Redirecting to Job Seeker Menu | seekerId={}", seekerId);

            JobSeekerMenu.start(
                    sc, userId, seekerId,
                    jobService, applicationService,
                    notificationService, userService,
                    jobSeekerService, resumeService
            );

        } else {

            int employerId = employerService.getEmployerIdByUserId(userId);
            logger.info("Redirecting to Employer Menu | employerId={}", employerId);

            EmployerMenu.start(
                    sc, userId, employerId,
                    jobService, applicationService,
                    employerService, notificationService,
                    userService
            );
        }
    }


    // =================== REGISTRATION HANDLER ===================
    private static void handleRegistration(
            Scanner sc,
            UserServiceImpl userService,
            JobSeekerServiceImpl jobSeekerService,
            EmployersServiceImpl employerService
    ) {

        String email;
        do {
            System.out.print("Email: ");
            email = sc.nextLine();
            if (!ValidationUtil.isValidEmail(email)) {
                System.out.println(" Enter a valid email.");
            }
        } while (!ValidationUtil.isValidEmail(email));

        String password;
        do {
            System.out.print("Password: ");
            password = sc.nextLine();
            if (!ValidationUtil.isValidPassword(password)) {
                System.out.println("""
                         Weak password.
                        - 8+ characters
                        - Uppercase
                        - Lowercase
                        - Digit
                        - Special character
                        """);
            }
        } while (!ValidationUtil.isValidPassword(password));

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

        if ("JOB_SEEKER".equals(role)) {
            jobSeekerService.createJobSeeker(userId);
        } else {
            employerService.createEmployer(userId);
        }

        System.out.println(" Registration successful!");
        logger.info("New user registered | userId={}, role={}", userId, role);
    }

    // ================= FORGOT PASSWORD HANDLER ==================
    private static void handleForgotPassword(
            Scanner sc,
            UserServiceImpl userService
    ) {

        System.out.print("Email: ");
        String email = sc.nextLine();

        if (!ValidationUtil.isValidEmail(email)) {
            System.out.println(" Invalid email format.");
            return;
        }

        String question = userService.getSecurityQuestionByEmail(email);
        if (question == null) {
            System.out.println(" Email not found.");
            logger.warn("Forgot password failed | email={}", email);
            return;
        }

        System.out.println(question);
        System.out.print("Answer: ");
        String ans = sc.nextLine();

        if (!userService.verifySecurityAnswer(email, ans)) {
            System.out.println(" Incorrect answer.");
            logger.warn("Incorrect security answer | email={}", email);
            return;
        }

        String newPwd;
        do {
            System.out.print("New Password: ");
            newPwd = sc.nextLine();
        } while (!ValidationUtil.isValidPassword(newPwd));

        userService.resetPassword(email, ans, newPwd);
        System.out.println(" Password reset successful.");
        logger.info("Password reset successful | email={}", email);
    }
}
