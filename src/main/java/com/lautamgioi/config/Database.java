package com.lautamgioi.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class Database {
    private static final String DEFAULT_URL = "jdbc:mysql://localhost:3306/lautamgioi_db?useUnicode=true&characterEncoding=utf8&connectionCollation=utf8mb4_unicode_ci&serverTimezone=Asia/Ho_Chi_Minh";
    private static final String DEFAULT_USER = "root";
    private static final String DEFAULT_PASSWORD = "123456";

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException("MySQL JDBC driver is missing", e);
        }
    }

    private Database() {
    }

    public static Connection getConnection() throws SQLException {
        String url = env("DB_URL", DEFAULT_URL);
        String user = env("DB_USER", DEFAULT_USER);
        String password = env("DB_PASSWORD", DEFAULT_PASSWORD);
        return DriverManager.getConnection(url, user, password);
    }

    private static String env(String key, String fallback) {
        String value = System.getenv(key);
        return value == null || value.isBlank() ? fallback : value;
    }
}
