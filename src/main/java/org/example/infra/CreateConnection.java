package org.example.infra;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class CreateConnection {

    private static final String DB_URL = "jdbc:mysql://localhost:3306/Bank";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "root";


    public static Connection getConnection() {
        try {
            Connection connection = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
            return connection;
        } catch (SQLException e) {
            System.out.println("Error in establishing connection: " + e.getMessage());
        }
        return null;
    }


}
