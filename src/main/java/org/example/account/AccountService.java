package org.example.account;

import org.example.User.UserService;
import org.example.infra.CreateConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AccountService {


    private Connection connection;
    private UserService userService;

    public AccountService () {
        this.connection = CreateConnection.getConnection();
        this.userService = new UserService();
    }

    // 1. Create account
    public void createAccount(
            int userId,
            String accountNumber,
            String accountType,
            String pin) {
        if(!userService.existsById(userId)) {
            System.out.println("User does not exist.");
            return;
        }

        if (existsByAccountNumber(accountNumber)) {
            System.out.println("Account already exists.");
            return;
        }


        String query = "INSERT INTO accounts " +
                "(user_id, account_number, account_type, pin, balance) " +
                "VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement ps = connection.prepareStatement(query)) {

            ps.setInt(1, userId);
            ps.setString(2, accountNumber);
            ps.setString(3, accountType);
            ps.setString(4, pin);
            ps.setDouble(5, 0.0);

            ps.executeUpdate();

            System.out.println("Account created successfully.");

        } catch (SQLException e) {
            System.out.println("Error while creating account: "
                    + e.getMessage());
        }
    }

    // 2. Get account by account number
    public void getAccountByNumber(String accountNumber) {

        String query = "SELECT * FROM accounts " +
                "WHERE account_number = ?";

        try (PreparedStatement ps = connection.prepareStatement(query)) {

            ps.setString(1, accountNumber);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                System.out.println("Account ID: "
                        + rs.getInt("account_id"));

                System.out.println("User ID: "
                        + rs.getInt("user_id"));

                System.out.println("Account Number: "
                        + rs.getString("account_number"));

                System.out.println("Account Type: "
                        + rs.getString("account_type"));

                System.out.println("Balance: "
                        + rs.getBigDecimal("balance"));

            } else {
                System.out.println("Account not found.");
            }

        } catch (SQLException e) {
            System.out.println("Error while getting account: "
                    + e.getMessage());
        }
    }

    // 3. Check balance
    public void checkBalance(String accountNumber) {

        String query = "SELECT balance FROM accounts " +
                "WHERE account_number = ?";

        try (PreparedStatement ps = connection.prepareStatement(query)) {

            ps.setString(1, accountNumber);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                System.out.println("Balance: "
                        + rs.getBigDecimal("balance"));

            } else {
                System.out.println("Account not found.");
            }

        } catch (SQLException e) {
            System.out.println("Error while checking balance: "
                    + e.getMessage());
        }
    }

    // 4. Update PIN
    public void updatePin(
            String accountNumber,
            String oldPin,
            String newPin) {

        String query = "UPDATE accounts " +
                "SET pin = ? " +
                "WHERE account_number = ? AND pin = ?";

        try (PreparedStatement ps = connection.prepareStatement(query)) {

            ps.setString(1, newPin);
            ps.setString(2, accountNumber);
            ps.setString(3, oldPin);

            int rows = ps.executeUpdate();

            if (rows > 0) {
                System.out.println("PIN updated successfully.");
            } else {
                System.out.println(
                        "Account not found or old PIN is incorrect.");
            }

        } catch (SQLException e) {
            System.out.println("Error while updating PIN: "
                    + e.getMessage());
        }
    }

    // 5. Delete account
    public void deleteAccount(String accountNumber) {

        String query = "DELETE FROM accounts " +
                "WHERE account_number = ?";

        try (PreparedStatement ps = connection.prepareStatement(query)) {

            ps.setString(1, accountNumber);

            int rows = ps.executeUpdate();

            if (rows > 0) {
                System.out.println("Account deleted successfully.");
            } else {
                System.out.println("Account not found.");
            }

        } catch (SQLException e) {
            System.out.println("Error while deleting account: "
                    + e.getMessage());
        }
    }


    public boolean existsByAccountNumber(String accountNumber) {

        String query = "SELECT EXISTS(" +
                "SELECT 1 FROM accounts WHERE account_number = ?)";

        try (PreparedStatement ps = connection.prepareStatement(query)) {

            ps.setString(1, accountNumber);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getBoolean(1);
            }

        } catch (SQLException e) {
            System.out.println("Error while checking account: "
                    + e.getMessage());
        }

        return false;
    }

}