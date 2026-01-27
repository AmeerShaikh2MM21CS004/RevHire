package com.revhire.model;

import java.sql.Timestamp;

public class Resume {

    private int resumeId;
    private int seekerId;
    private String objective;
    private String education;
    private String experience;
    private String skills;
    private String projects;
    private Timestamp updatedAt;

    // Full constructor
    public Resume(int resumeId, int seekerId, String objective, String education,
                  String experience, String skills, String projects, Timestamp updatedAt) {
        this.resumeId = resumeId;
        this.seekerId = seekerId;
        this.objective = objective;
        this.education = education;
        this.experience = experience;
        this.skills = skills;
        this.projects = projects;
        this.updatedAt = updatedAt;
    }

    // Getters
    public int getResumeId() { return resumeId; }
    public int getSeekerId() { return seekerId; }
    public String getObjective() { return objective; }
    public String getEducation() { return education; }
    public String getExperience() { return experience; }
    public String getSkills() { return skills; }
    public String getProjects() { return projects; }
    public Timestamp getUpdatedAt() { return updatedAt; }

    // Setters
    public void setResumeId(int resumeId) { this.resumeId = resumeId; }
    public void setSeekerId(int seekerId) { this.seekerId = seekerId; }
    public void setObjective(String objective) { this.objective = objective; }
    public void setEducation(String education) { this.education = education; }
    public void setExperience(String experience) { this.experience = experience; }
    public void setSkills(String skills) { this.skills = skills; }
    public void setProjects(String projects) { this.projects = projects; }
    public void setUpdatedAt(Timestamp updatedAt) { this.updatedAt = updatedAt; }
}
