package com.revhire.service.impl;

public interface  JobSeekerServiceimpl {

    void createProfile(
            int userId,
            String fullName,
            String phone,
            String location,
            int totalExperience
    );

    void updateJobSeekerProfile(
            int seekerId,
            String fullName,
            String phone,
            String location,
            Integer totalExperience
    );

    int getSeekerIdByUserId(int userId);

    void createJobSeeker(int userId);

}
