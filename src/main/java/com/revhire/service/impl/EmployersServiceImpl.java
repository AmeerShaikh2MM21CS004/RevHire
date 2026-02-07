package com.revhire.service.impl;

import com.revhire.dao.impl.EmployersDAOImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class EmployersServiceImpl implements com.revhire.service.EmployersService {

    private static final Logger logger =
            LogManager.getLogger(EmployersServiceImpl.class);

    private final EmployersDAOImpl employersDAOImpl;

    // Default constructor
    public EmployersServiceImpl() {
        this.employersDAOImpl = new EmployersDAOImpl();
    }

    // Constructor for unit testing
    public EmployersServiceImpl(EmployersDAOImpl employersDAOImpl) {
        this.employersDAOImpl = employersDAOImpl;
    }

    @Override
    public int getEmployerIdByUserId(int userId) {

        logger.info("Fetching employerId for userId={}", userId);

        Integer employerId = employersDAOImpl.getEmployerIdByUserId(userId);

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

        int rows = employersDAOImpl.updateCompanyProfile(
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

        employersDAOImpl.addEmployer(
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
