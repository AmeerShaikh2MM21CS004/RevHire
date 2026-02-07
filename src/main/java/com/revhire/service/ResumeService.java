package com.revhire.service;

public interface ResumeService {

    void saveOrUpdateResume(
            int seekerId,
            String objective,
            String education,
            String experience,
            String skills,
            String projects
    );

}
