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

//    public void insertIntoUser(String name, String email, String phone, String address ) throws SQLException {
//        String insertSQLQuery = "Insert into User(name,email,phone,address) Values(?,?,?,?)";
//      PreparedStatement  preparedStatement = connection.prepareStatement(insertSQLQuery);
//      preparedStatement.setString(1,"John");
//      preparedStatement.setString(2,"john@gmail.com");
//      preparedStatement.setString(3,"8800009500");
//      preparedStatement.setString(4,"Kotla Mevatiyan Hapur");
//
//    }

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

//public void insertIntoAccount(int user_id,String account_number,String account_type, String pin,Double balance) throws SQLException {
//        String insertSQLQuery2 = "Insert Into Account(user_id,account_number,account_type,pin,balance) Values (?,?,?,?,?)";
//        PreparedStatement preparedStatement = connection.prepareStatement(insertSQLQuery2);
//        preparedStatement.setInt(1,1001);
//        preparedStatement.setString(2,"000100010012");
//        preparedStatement.setString(3,"Current Account ");
//        preparedStatement.setString(4,"0205");
//        preparedStatement.setDouble(5,500000.11);
//
//}

//    transaction_id INT AUTO_INCREMENT PRIMARY KEY,
//    account_id INT NOT NULL,
//    transaction_type VARCHAR(20) NOT NULL,
//    amount DECIMAL(12,2) NOT NULL,
//    transaction_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
//
//    FOREIGN KEY (account_id) REFERENCES account(account_id)
//        );


    public void createTransactionTable() throws SQLException {
        String transactionQuery = "CREATE TABLE IF NOT EXISTS TRANSACTION (" +
                                  "transaction_id INT AUTO_INCREMENT PRIMARY KEY," +
                                   "transaction_type VARCHAR(20) NOT NULL," +
                                   "amount DECIMAL(12,2) NOT NULL," +
                                  "transaction_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                "FOREIGN KEY (account_id) REFERENCES account(account_id))";

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
