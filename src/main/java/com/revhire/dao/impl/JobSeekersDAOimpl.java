package com.revhire.dao.impl;

import java.sql.SQLException;

public interface  JobSeekersDAOimpl {

    void insertJobSeeker(
            int userId,
            String fullName,
            String phone,
            String location,
            int totalExperience,
            char profileCompleted
    ) throws SQLException;

    int findSeekerIdByUserId(int userId) throws SQLException;

    int updateJobSeeker(
            int seekerId,
            String fullName,
            String phone,
            String location,
            Integer totalExperience
    ) throws SQLException;

}
