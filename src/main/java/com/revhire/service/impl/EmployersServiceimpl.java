package com.revhire.service.impl;

import com.revhire.dao.EmployersDAO;

public interface  EmployersServiceimpl {

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
