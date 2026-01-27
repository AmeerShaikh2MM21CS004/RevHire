package com.revhire.model;

import java.sql.Timestamp;

public class User {
    private int userId;
    private String email;
    private String role;
    private Timestamp createdAt;

    public User(int userId, String email, String role, Timestamp createdAt) {
        this.userId = userId;
        this.email = email;
        this.role = role;
        this.createdAt = createdAt;
    }

    // For login (without createdAt)
    public User(int userId, String email, String role) {
        this(userId, email, role, null);
    }

    // Getters
    public int getUserId() { return userId; }
    public String getEmail() { return email; }
    public String getRole() { return role; }
    public Timestamp getCreatedAt() { return createdAt; }

    // Setters if needed
    public void setUserId(int userId) { this.userId = userId; }
    public void setEmail(String email) { this.email = email; }
    public void setRole(String role) { this.role = role; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }
}
