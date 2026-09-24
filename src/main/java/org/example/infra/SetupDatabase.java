package org.example.infra;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SetupDatabase
{

    private final Connection connection;

    public SetupDatabase() {
        connection = CreateConnection.getConnection();
    }


    public void createUserTable() {

        String query = "CREATE TABLE IF NOT EXISTS users (" +
                "user_id INT AUTO_INCREMENT PRIMARY KEY, " +
                "name VARCHAR(100) NOT NULL, " +
                "email VARCHAR(100) UNIQUE NOT NULL, " +
                "phone VARCHAR(15), " +
                "address VARCHAR(200))";

        try (PreparedStatement preparedStatement =
                     connection.prepareStatement(query)) {

            preparedStatement.execute();

            System.out.println("User Table created successfully");

        } catch (SQLException e) {

            System.out.println(
                    "Error while creating User Table: "
                            + e.getMessage()
            );
        }
    }



    public void createTransactionTable() throws SQLException {
        String transactionQuery = "CREATE TABLE IF NOT EXISTS transactions (" +
                                  "transaction_id INT AUTO_INCREMENT PRIMARY KEY," +
                                    "account_id INT NOT NULL, " +
                                   "transaction_type VARCHAR(20) NOT NULL," +
                                   "amount DECIMAL(12,2) NOT NULL," +
                                  "transaction_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                "FOREIGN KEY (account_id) REFERENCES accounts(account_id))";

        try (PreparedStatement preparedStatement = connection.prepareStatement(transactionQuery)) {

            preparedStatement.execute();
            System.out.println("Transaction Table Created successfully");
        }
        catch (SQLException e) {

            System.out.println(
                    "Error while creating Transaction Table: "
                            + e.getMessage()
            );
        }

    }


    public void createAccountTable() {

        String query = "CREATE TABLE IF NOT EXISTS accounts (" +
                "account_id INT AUTO_INCREMENT PRIMARY KEY, " +
                "user_id INT NOT NULL, " +
                "account_number VARCHAR(20) UNIQUE NOT NULL, " +
                "account_type VARCHAR(20) NOT NULL, " +
                "pin VARCHAR(6) NOT NULL, " +
                "balance DECIMAL(12,2) DEFAULT 0.00, " +
                "FOREIGN KEY (user_id) REFERENCES users(user_id))";

        try (PreparedStatement preparedStatement =
                     connection.prepareStatement(query)) {

            preparedStatement.execute();

            System.out.println("Account Table created successfully");

        } catch (SQLException e) {

            System.out.println(
                    "Error while creating Account Table: "
                            + e.getMessage()
            );
        }
    }
}
