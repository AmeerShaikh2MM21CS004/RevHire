package com.revhire.service;

import com.revhire.dao.EmployersDAO;
import com.revhire.service.impl.EmployersServiceimpl;

public class EmployersService implements EmployersServiceimpl {

    private final EmployersDAO employersDAO = new EmployersDAO();

    public int getEmployerIdByUserId(int userId) {
        Integer employerId = EmployersDAO.getEmployerIdByUserId(userId);

        if (employerId == null) {
            throw new RuntimeException("Employer profile not found");
        }
        return employerId;
    }

    public void updateCompanyProfile(
            int employerId,
            String company,
            String industry,
            Integer companySize,
            String description,
            String website,
            String location) {

        int rows = employersDAO.updateCompanyProfile(
                employerId, company,industry, companySize,
                description, website, location
        );

        if (rows > 0) {
            System.out.println("✅ Company profile updated.");
        } else {
            System.out.println("⚠️ Nothing to update.");
        }
    }

    public void createEmployer(int userId) {
        employersDAO.addEmployer(
                userId,
                "Not Provided",
                null,
                0,
                null,
                null,
                null
        );
    }

}
