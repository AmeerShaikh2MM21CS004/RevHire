package com.revhire.model;

public class JobSeeker {
    private int seekerId;
    private int userId;
    private String fullName;
    private String phone;
    private String location;
    private int totalExperience;
    private char profileCompleted;
    private int profileCompletion;

    public int getSeekerId() { return seekerId; }
    public void setSeekerId(int seekerId) { this.seekerId = seekerId; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public int getTotalExperience() { return totalExperience; }
    public void setTotalExperience(int totalExperience) { this.totalExperience = totalExperience; }

    public char getProfileCompleted() { return profileCompleted; }
    public void setProfileCompleted(char profileCompleted) { this.profileCompleted = profileCompleted; }

    public int getProfileCompletion() { return profileCompletion; }
    public void setProfileCompletion(int profileCompletion) { this.profileCompletion = profileCompletion; }
}
