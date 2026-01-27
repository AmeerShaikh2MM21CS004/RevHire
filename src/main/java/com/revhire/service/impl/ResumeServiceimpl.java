package com.revhire.service.impl;

public interface  ResumeServiceimpl {

    void saveOrUpdateResume(
            int seekerId,
            String objective,
            String education,
            String experience,
            String skills,
            String projects
    );

}
