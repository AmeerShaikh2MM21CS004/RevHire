package com.revhire.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    private static final Logger logger = LogManager.getLogger(DBConnection.class);

    private static final String URL = "jdbc:oracle:thin:@localhost:1521:XE"; // replace with your DB host/port/service
    private static final String USER = "system";
    private static final String PASSWORD = "system";

    public static Connection getConnection() {
        try {
            Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
            logger.info("Database connection established successfully | URL={}", URL);
            return connection;
        } catch (SQLException e) {
            logger.error("Failed to establish database connection | URL={}", URL, e);
            throw new RuntimeException("Unable to connect to the database", e);
        }
    }
}
