package com.revhire.model;

import java.sql.Timestamp;

public class Notification {

    private int notificationId;
    private int userId;
    private String message;
    private char isRead; // 'Y' or 'N'
    private Timestamp createdAt;

    // Constructor for creating a new notification (auto-generated ID)
    public Notification(int userId, String message) {
        this.userId = userId;
        this.message = message;
        this.isRead = 'N';
        this.createdAt = new Timestamp(System.currentTimeMillis());
    }

    // Full constructor (from DB)
    public Notification(int notificationId, int userId, String message, char isRead, Timestamp createdAt) {
        this.notificationId = notificationId;
        this.userId = userId;
        this.message = message;
        this.isRead = isRead;
        this.createdAt = createdAt;
    }

    // Getters
    public int getNotificationId() { return notificationId; }
    public int getUserId() { return userId; }
    public String getMessage() { return message; }
    public char getIsRead() { return isRead; }
    public Timestamp getCreatedAt() { return createdAt; }

    // Setters (optional)
    public void setNotificationId(int notificationId) { this.notificationId = notificationId; }
    public void setUserId(int userId) { this.userId = userId; }
    public void setMessage(String message) { this.message = message; }
    public void setIsRead(char isRead) { this.isRead = isRead; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }
}
