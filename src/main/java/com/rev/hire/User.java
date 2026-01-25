package com.rev.hire;

public class User {

    private int userId;
    private String email;
    private String role;

    public User(int userId, String email, String role) {
        this.userId = userId;
        this.email = email;
        this.role = role;
    }

    public int getUserId() {
        return userId;
    }

    public String getRole() {
        return role;
    }
}
