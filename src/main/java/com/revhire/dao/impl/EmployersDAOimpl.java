package com.revhire.dao.impl;

public interface  EmployersDAOimpl {

    void addEmployer(
            int userId,
            String companyName,
            String industry,
            int companySize,
            String description,
            String website,
            String location
    );

    Integer getEmployerIdByUserId(int userId);

    int updateCompanyProfile(
            int employerId,
            String company,
            String industry,
            Integer companySize,
            String description,
            String website,
            String location
    );

}