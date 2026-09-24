package org.example.transaction;

import org.example.infra.CreateConnection;

import java.math.BigDecimal;
import java.sql.*;

public class TransactionService {

    private final Connection connection;

    public TransactionService() {
        this.connection = CreateConnection.getConnection();
    }


    // =========================================================
    // 1. DEPOSIT
    // =========================================================

    public void deposit(String accountNumber, BigDecimal amount) {

        if (amount == null || amount.compareTo(new BigDecimal("500")) <  0) {
            System.out.println("Deposit amount must be greater than 1000.");
            return;
        }

        String updateBalanceQuery =
                "UPDATE accounts " +
                        "SET balance = balance + ? " +
                        "WHERE account_number = ?";

        // ask
        String transactionQuery =
                "INSERT INTO transactions " +
                        "(account_id, transaction_type, amount) " +
                        "SELECT account_id, ?, ? " + //
                        "FROM accounts " +
                        "WHERE account_number = ?";


        try {

            connection.setAutoCommit(false); // skip still


            // Step 1: Update balance
            try (PreparedStatement ps =
                         connection.prepareStatement(updateBalanceQuery)) {

                ps.setString(2, accountNumber);
                ps.setBigDecimal(1, amount);

                int rows = ps.executeUpdate();

                if (rows == 0) {
                    System.out.println("Account not found.");
                    connection.rollback();
                    return;
                }
            }

            // Step 2: Save transaction history
            try (PreparedStatement ps =
                         connection.prepareStatement(transactionQuery)) {

                ps.setString(1, "DEPOSIT"); // ask
                ps.setBigDecimal(2, amount);
                ps.setString(3, accountNumber);

                ps.executeUpdate();
            }

            // Step 3: Everything successful
            connection.commit();

            System.out.println("Deposit successful.");
            System.out.println("Amount deposited: " + amount);

        } catch (SQLException e) {       // ask

            try {
                connection.rollback();
            } catch (SQLException rollbackException) {
                System.out.println(
                        "Rollback failed: "
                                + rollbackException.getMessage()
                );
            }

            System.out.println(
                    "Deposit failed: " + e.getMessage()
            );

        } finally {

            try {
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                System.out.println(
                        "Could not reset auto commit: "
                                + e.getMessage()
                );
            }
        }
    }


    // =========================================================
    // 2. WITHDRAW
    // =========================================================

    public void withdraw(
            String accountNumber,
            String pin,
            BigDecimal amount) {

        if (amount == null || amount.compareTo(new BigDecimal("500")) < 0) {
            System.out.println("Withdrawal amount must be greater than 500.");
            return;
        }

        String accountQuery =
                "SELECT account_id, balance " +
                        "FROM accounts " +
                        "WHERE account_number = ? AND pin = ? " +
                        "FOR UPDATE";

        String updateBalanceQuery =
                "UPDATE accounts " +
                        "SET balance = balance - ? " +      //   update balance
                        "WHERE account_id = ?";

        String transactionQuery =
                "INSERT INTO transactions " +
                        "(account_id, transaction_type, amount) " +
                        "VALUES (?, ?, ?)";


        try {

            connection.setAutoCommit(false);

            int accountId;
            BigDecimal currentBalance;

            // Step 1: Check account + PIN
            try (PreparedStatement ps =
                         connection.prepareStatement(accountQuery)) {

                ps.setString(1, accountNumber);
                ps.setString(2, pin);

                try (ResultSet rs = ps.executeQuery()) {

                    if (!rs.next()) {
                        System.out.println(
                                "Invalid account number or PIN."
                        );
                        connection.rollback();
                        return;
                    }

                    accountId = rs.getInt("account_id");
                    currentBalance = rs.getBigDecimal("balance");
                }
            }

            // Step 2: Check sufficient balance
            if (currentBalance.compareTo(amount) < 0) {

                System.out.println("Insufficient balance.");

                connection.rollback();
                return;
            }

            // Step 3: Deduct money
            try (PreparedStatement ps =
                         connection.prepareStatement(updateBalanceQuery)) {

                ps.setBigDecimal(1, amount);
                ps.setInt(2, accountId);

                int rows = ps.executeUpdate(); // ask

                if (rows == 0) {
                    connection.rollback();
                    System.out.println("Withdrawal failed.");
                    return;
                }
            }

            // Step 4: Save transaction history
            try (PreparedStatement ps =
                         connection.prepareStatement(transactionQuery)) {

                ps.setInt(1, accountId);
                ps.setString(2, "WITHDRAW");
                ps.setBigDecimal(3, amount);

                ps.executeUpdate();
            }

            // Step 5: Commit everything
            connection.commit();

            System.out.println("Withdrawal successful.");
            System.out.println("Amount withdrawn: " + amount);



        } catch (SQLException e) {

            try {
                connection.rollback();
            } catch (SQLException rollbackException) {
                System.out.println(
                        "Rollback failed: "
                                + rollbackException.getMessage()
                );
            }

            System.out.println(
                    "Withdrawal failed: " + e.getMessage()
            );

        } finally {

            try {
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                System.out.println(
                        "Could not reset auto commit: "
                                + e.getMessage()
                );
            }
        }
    }


    // =========================================================
    // 3. GET ALL TRANSACTIONS OF AN ACCOUNT
    // =========================================================

    public void getTransactions(String accountNumber) {

        String query =
                "SELECT bt.transaction_id, " +       // ask
                        "bt.transaction_type, " +
                        "bt.amount, " +
                        "bt.transaction_date " +
                        "FROM transactions bt " +
                        "JOIN accounts a " +
                        "ON bt.account_id = a.account_id " +
                        "WHERE a.account_number = ? " +
                        "ORDER BY bt.transaction_date DESC";

        try (PreparedStatement ps =
                     connection.prepareStatement(query)) {

            ps.setString(1, accountNumber);

            try (ResultSet rs = ps.executeQuery()) {

                boolean found = false;

                while (rs.next()) {

                    found = true;

                    System.out.println(
                            "Transaction ID: "
                                    + rs.getInt("transaction_id")
                    );

                    System.out.println(
                            "Type: "
                                    + rs.getString("transaction_type")
                    );

                    System.out.println(
                            "Amount: "
                                    + rs.getBigDecimal("amount")
                    );

                    System.out.println(
                            "Date: "
                                    + rs.getTimestamp("transaction_date")
                    );

                    System.out.println("-----------------------");
                }

                if (!found) {
                    System.out.println(
                            "No transactions found."
                    );
                }
            }

        } catch (SQLException e) {

            System.out.println(
                    "Error while getting transactions: "
                            + e.getMessage()
            );
        }
    }


    // =========================================================
    // 4. GET TRANSACTION BY ID
    // =========================================================

    public void getTransactionById(int transactionId) {

        String query =
                "SELECT transaction_id, account_id, " +
                        "transaction_type, amount, transaction_date " +
                        "FROM transactions " +
                        "WHERE transaction_id = ?";

        try (PreparedStatement ps =
                     connection.prepareStatement(query)) {

            ps.setInt(1, transactionId);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {

                    System.out.println(
                            "Transaction ID: "
                                    + rs.getInt("transaction_id")
                    );

                    System.out.println(
                            "Account ID: "
                                    + rs.getInt("account_id")
                    );

                    System.out.println(
                            "Transaction Type: "
                                    + rs.getString("transaction_type")
                    );

                    System.out.println(
                            "Amount: "
                                    + rs.getBigDecimal("amount")
                    );

                    System.out.println(
                            "Transaction Date: "
                                    + rs.getTimestamp(
                                    "transaction_date")
                    );

                } else {

                    System.out.println(
                            "Transaction not found."
                    );
                }
            }

        } catch (SQLException e) {

            System.out.println(
                    "Error while getting transaction: "
                            + e.getMessage()
            );
        }
    }
}