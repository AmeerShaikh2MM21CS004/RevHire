package com.revhire.service;

import com.revhire.dao.EmployersDAO;
import com.revhire.service.impl.EmployersServiceimpl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class EmployersService implements EmployersServiceimpl {

    private static final Logger logger =
            LogManager.getLogger(EmployersService.class);

    private final EmployersDAO employersDAO = new EmployersDAO();

    @Override
    public int getEmployerIdByUserId(int userId) {

        logger.info("Fetching employerId for userId={}", userId);

        Integer employerId = employersDAO.getEmployerIdByUserId(userId);

        if (employerId == null) {
            logger.warn("Employer profile not found for userId={}", userId);
            throw new RuntimeException("Employer profile not found");
        }

        logger.info("EmployerId retrieved | userId={}, employerId={}", userId, employerId);
        return employerId;
    }

    @Override
    public void updateCompanyProfile(
            int employerId,
            String company,
            String industry,
            Integer companySize,
            String description,
            String website,
            String location) {

        logger.info("Updating company profile | employerId={}", employerId);

        int rows = employersDAO.updateCompanyProfile(
                employerId,
                company,
                industry,
                companySize,
                description,
                website,
                location
        );

        if (rows > 0) {
            logger.info("Company profile updated successfully | employerId={}", employerId);
        } else {
            logger.warn("No fields updated | employerId={}", employerId);
        }
    }

    @Override
    public void createEmployer(int userId) {

        logger.info("Creating employer profile | userId={}", userId);

        employersDAO.addEmployer(
                userId,
                "Not Provided",
                null,
                0,
                null,
                null,
                null
        );

        logger.info("Employer profile created successfully | userId={}", userId);
    }
}
