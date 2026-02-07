package com.revhire.service;

public interface EmployersService {

    int getEmployerIdByUserId(int userId);

    void updateCompanyProfile(
            int employerId,
            String company,
            String industry,
            Integer companySize,
            String description,
            String website,
            String location
    );

    void createEmployer(int userId);

}
