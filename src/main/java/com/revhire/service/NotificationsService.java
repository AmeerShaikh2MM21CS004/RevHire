package com.revhire.service;

import com.revhire.model.Notification;

import java.util.List;

public interface NotificationsService {

    void addNotification(int userId, String message);

    List<Notification> fetchUnreadNotifications(int userId);

    List<String> showUnreadNotifications(int userId);

    void markAllAsRead(int userId);

    int getUnreadNotificationCount(int userId);

}
