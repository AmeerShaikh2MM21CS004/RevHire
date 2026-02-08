package com.revhire.dao;

import java.sql.SQLException;
import java.util.List;

public interface JobSeekersDAO {

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

    List<Integer> findMatchingSeekerUserIds(
            String skills,
            Integer experience,
            String location
    ) throws SQLException;


}
