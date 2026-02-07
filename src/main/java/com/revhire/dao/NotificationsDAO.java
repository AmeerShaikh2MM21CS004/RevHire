package com.revhire.dao;

import com.revhire.model.Notification;

import java.sql.SQLException;
import java.util.List;

public interface NotificationsDAO {

    void insertNotification(int userId, String message) throws SQLException;

    List<Notification> fetchUnreadNotifications(int userId) throws SQLException;

    List<Notification> fetchAllNotifications(int userId) throws SQLException;

    int markAllRead(int userId) throws SQLException;

    int countUnreadNotifications(int userId) throws SQLException;

}
