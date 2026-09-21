package org.example;

import org.example.User.UserService;
import org.example.account.AccountService;
import org.example.infra.SetupDatabase;
import org.example.transaction.TransactionService;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Scanner;

public class App {

    UserService userService;
    AccountService accountService;
    TransactionService transactionService;

    public App() {
        this.accountService = new AccountService();
        this.userService = new UserService();
        this.transactionService = new TransactionService();
    }


    public static void main(String[] args) throws SQLException {
        App app = new App();

        app.main();
    }
    public void main() throws SQLException {

        // Create tables
        SetupDatabase database = new SetupDatabase();

        database.createUserTable();
        database.createAccountTable();
        database.createTransactionTable();

        // Create service objects


        Scanner scanner = new Scanner(System.in);

        boolean running = true;

        while (running) {

            // =========================
            // MAIN MENU
            // =========================

            System.out.println("\n===== BANK MANAGEMENT SYSTEM =====");
            System.out.println("1. User");
            System.out.println("2. Account");
            System.out.println("3. Transaction");
            System.out.println("4. Exit");

            System.out.print("Enter your choice: ");

            int mainChoice = scanner.nextInt();
            scanner.nextLine();

            switch (mainChoice) {

                // =====================================
                // USER
                // =====================================

                case 1:

                    boolean userMenu = true;

                    while (userMenu) {

                        System.out.println("\n===== USER MENU =====");
                        System.out.println("1. Create User");
                        System.out.println("2. Get User");
                        System.out.println("3. Get All Users");
                        System.out.println("4. Update User");
                        System.out.println("5. Delete User");
                        System.out.println("6. Back");

                        System.out.print("Enter your choice: ");

                        int userChoice = scanner.nextInt();
                        scanner.nextLine();

                        switch (userChoice) {

                            case 1:

                                System.out.print("Enter name: ");
                                String name = scanner.nextLine();

                                System.out.print("Enter email: ");
                                String email = scanner.nextLine();

                                System.out.print("Enter phone: ");
                                String phone = scanner.nextLine();

                                System.out.print("Enter address: ");
                                String address = scanner.nextLine();

                                userService.createUser(
                                        name,
                                        email,
                                        phone,
                                        address
                                );

                                break;


                            case 2:

                                System.out.print("Enter User ID: ");
                                int userId = scanner.nextInt();

                                userService.getUserById(userId);

                                break;


                            case 3:

                                userService.getAllUsers();

                                break;


                            case 4:

                                System.out.print("Enter User ID: ");
                                int updateUserId = scanner.nextInt();
                                scanner.nextLine();

                                System.out.print("Enter new name: ");
                                String updateName =
                                        scanner.nextLine();

                                System.out.print("Enter new email: ");
                                String updateEmail =
                                        scanner.nextLine();

                                System.out.print("Enter new phone: ");
                                String updatePhone =
                                        scanner.nextLine();

                                System.out.print("Enter new address: ");
                                String updateAddress =
                                        scanner.nextLine();

                                userService.updateUser(
                                        updateUserId,
                                        updateName,
                                        updateEmail,
                                        updatePhone,
                                        updateAddress
                                );

                                break;


                            case 5:

                                System.out.print("Enter User ID: ");
                                int deleteUserId =
                                        scanner.nextInt();

                                userService.deleteUser(
                                        deleteUserId
                                );

                                break;


                            case 6:

                                userMenu = false;

                                break;


                            default:

                                System.out.println(
                                        "Invalid choice."
                                );
                        }
                    }

                    break;


                // =====================================
                // ACCOUNT
                // =====================================

                case 2:

                    boolean accountMenu = true;

                    while (accountMenu) {

                        System.out.println("\n===== ACCOUNT MENU =====");
                        System.out.println("1. Create Account");
                        System.out.println("2. Get Account");
                        System.out.println("3. Check Balance");
                        System.out.println("4. Update PIN");
                        System.out.println("5. Delete Account");
                        System.out.println("6. Back");

                        System.out.print("Enter your choice: ");

                        int accountChoice =
                                scanner.nextInt();
                        scanner.nextLine();

                        switch (accountChoice) {

                            case 1:

                                System.out.print("Enter User ID: ");
                                int accountUserId =
                                        scanner.nextInt();
                                scanner.nextLine();

                                System.out.print(
                                        "Enter Account Number: "
                                );

                                String accountNumber =
                                        scanner.nextLine();

                                System.out.print(
                                        "Enter Account Type: "
                                );

                                String accountType =
                                        scanner.nextLine();

                                System.out.print("Enter PIN: ");
                                String pin =
                                        scanner.nextLine();

                                accountService.createAccount(
                                        accountUserId,
                                        accountNumber,
                                        accountType,
                                        pin
                                );

                                break;


                            case 2:

                                System.out.print(
                                        "Enter Account Number: "
                                );

                                String getAccountNumber =
                                        scanner.nextLine();

                                accountService
                                        .getAccountByNumber(
                                                getAccountNumber
                                        );

                                break;


                            case 3:

                                System.out.print(
                                        "Enter Account Number: "
                                );

                                String balanceAccountNumber =
                                        scanner.nextLine();

                                accountService.checkBalance(
                                        balanceAccountNumber
                                );

                                break;


                            case 4:

                                System.out.print(
                                        "Enter Account Number: "
                                );

                                String pinAccountNumber =
                                        scanner.nextLine();

                                System.out.print(
                                        "Enter Old PIN: "
                                );

                                String oldPin =
                                        scanner.nextLine();

                                System.out.print(
                                        "Enter New PIN: "
                                );

                                String newPin =
                                        scanner.nextLine();

                                accountService.updatePin(
                                        pinAccountNumber,
                                        oldPin,
                                        newPin
                                );

                                break;


                            case 5:

                                System.out.print(
                                        "Enter Account Number: "
                                );

                                String deleteAccountNumber =
                                        scanner.nextLine();

                                accountService.deleteAccount(
                                        deleteAccountNumber
                                );

                                break;


                            case 6:

                                accountMenu = false;

                                break;


                            default:

                                System.out.println(
                                        "Invalid choice."
                                );
                        }
                    }

                    break;


                // =====================================
                // TRANSACTION
                // =====================================

                case 3:

                    boolean transactionMenu = true;

                    while (transactionMenu) {

                        System.out.println(
                                "\n===== TRANSACTION MENU ====="
                        );

                        System.out.println("1. Deposit");
                        System.out.println("2. Withdraw");
                        System.out.println(
                                "3. Get Transactions"
                        );
                        System.out.println("4. Back");

                        System.out.print(
                                "Enter your choice: "
                        );

                        int transactionChoice =
                                scanner.nextInt();
                        scanner.nextLine();

                        switch (transactionChoice) {

                            case 1:

                                System.out.print(
                                        "Enter Account Number: "
                                );

                                String depositAccountNumber =
                                        scanner.nextLine();

                                System.out.print(
                                        "Enter Amount: "
                                );

                                BigDecimal depositAmount =
                                        scanner.nextBigDecimal();

                                scanner.nextLine();

                                transactionService.deposit(
                                        depositAccountNumber,
                                        depositAmount
                                );

                                break;


                            case 2:

                                System.out.print(
                                        "Enter Account Number: "
                                );

                                String withdrawAccountNumber =
                                        scanner.nextLine();

                                System.out.print("Enter PIN: ");

                                String withdrawPin =
                                        scanner.nextLine();

                                System.out.print(
                                        "Enter Amount: "
                                );

                                BigDecimal withdrawAmount =
                                        scanner.nextBigDecimal();

                                scanner.nextLine();

                                transactionService.withdraw(
                                        withdrawAccountNumber,
                                        withdrawPin,
                                        withdrawAmount
                                );

                                break;


                            case 3:

                                System.out.print(
                                        "Enter Account Number: "
                                );

                                String transactionAccountNumber =
                                        scanner.nextLine();

                                transactionService
                                        .getTransactions(
                                                transactionAccountNumber
                                        );

                                break;


                            case 4:

                                transactionMenu = false;

                                break;


                            default:

                                System.out.println(
                                        "Invalid choice."
                                );
                        }
                    }

                    break;


                // =====================================
                // EXIT
                // =====================================

                case 4:

                    running = false;

                    System.out.println(
                            "Thank you for using " +
                                    "Bank Management System."
                    );

                    break;


                default:

                    System.out.println(
                            "Invalid choice. Please try again."
                    );
            }
        }

        scanner.close();
    }
}