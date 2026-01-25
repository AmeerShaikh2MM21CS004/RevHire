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
            System.out.println("1. Add User");
            System.out.println("2. View Users");
            System.out.println("3. Add Job Seeker Profile");
            System.out.println("4. Add Employer Profile");
            System.out.println("5. Post Job");
            System.out.println("6. Apply for Job");
            System.out.println("7. View All Jobs");
            System.out.println("8. Exit");
            System.out.println();
            System.out.print("Enter your choice: ");

            int choice = sc.nextInt();
            sc.nextLine(); // clear buffer
            System.out.println();

            try {
                switch (choice) {

                    // 1️⃣ ADD USER
                    case 1 -> {
                        System.out.print("Email: ");
                        String email = sc.nextLine();

                        System.out.print("Password Hash: ");
                        String password = sc.nextLine();

                        System.out.print("Role (JOB_SEEKER / EMPLOYER): ");
                        String role = sc.nextLine();

                        userDAO.addUser(email, password, role);
                        System.out.println("✅ User added successfully!");
                    }

                    // 2️⃣ VIEW USERS
                    case 2 -> {
                        List<String> users = userDAO.getAllUsers();
                        users.forEach(System.out::println);
                    }

                    // 3️⃣ ADD JOB SEEKER PROFILE
                    case 3 -> {
                        System.out.print("User ID (JOB_SEEKER): ");
                        int userId = sc.nextInt();
                        sc.nextLine();

                        System.out.print("Full Name: ");
                        String name = sc.nextLine();

                        System.out.print("Phone: ");
                        String phone = sc.nextLine();

                        System.out.print("Location: ");
                        String location = sc.nextLine();

                        System.out.print("Total Experience: ");
                        int exp = sc.nextInt();

                        seekersDAO.addJobSeeker(userId, name, phone, location, exp, 'Y');
                        System.out.println("✅ Job seeker profile created!");
                    }

                    // 4️⃣ ADD EMPLOYER PROFILE
                    case 4 -> {
                        System.out.print("User ID (EMPLOYER): ");
                        int userId = sc.nextInt();
                        sc.nextLine();

                        System.out.print("Company Name: ");
                        String company = sc.nextLine();

                        System.out.print("Industry: ");
                        String industry = sc.nextLine();

                        System.out.print("Company Size: ");
                        int size = sc.nextInt();
                        sc.nextLine();

                        System.out.print("Description: ");
                        String desc = sc.nextLine();

                        System.out.print("Website: ");
                        String website = sc.nextLine();

                        System.out.print("Location: ");
                        String location = sc.nextLine();

                        employersDAO.addEmployer(userId, company, industry, size, desc, website, location);
                        System.out.println("✅ Employer profile created!");
                    }

                    // 5️⃣ POST JOB
                    case 5 -> {
                        System.out.print("Employer ID: ");
                        int employerId = sc.nextInt();
                        sc.nextLine();

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

                        jobsDAO.addJob(employerId, title, desc, skills, exp, edu, location, salary, type, deadline);
                        System.out.println("✅ Job posted successfully!");
                    }

                    // 6️⃣ APPLY FOR JOB
                    case 6 -> {
                        System.out.print("Job ID: ");
                        int jobId = sc.nextInt();

                        System.out.print("Seeker ID: ");
                        int seekerId = sc.nextInt();
                        sc.nextLine();

                        applicationsDAO.addApplication(jobId, seekerId);
                        System.out.println("✅ Application submitted!");
                    }

                    // 7️⃣ VIEW JOBS
                    case 7 -> {
                        List<String> jobs = jobsDAO.getAllJobs();
                        jobs.forEach(System.out::println);
                    }

                    // 8️⃣ EXIT
                    case 8 -> {
                        System.out.println("👋 Exiting RevHire. Goodbye!");
                        sc.close();
                        return;
                    }

                    default -> System.out.println("❌ Invalid choice. Try again.");
                }
            } catch (Exception e) {
                System.out.println("⚠️ Error: " + e.getMessage());
            }
        }
    }
}
