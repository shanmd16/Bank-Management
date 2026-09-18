package org.example.User;

import org.example.infra.CreateConnection;

import java.sql.Connection;


import org.example.infra.CreateConnection;

import java.sql.*;

public class UserService {

    private final Connection connection;

    public UserService() {
        this.connection = CreateConnection.getConnection();
    }

    // 1. Create User // ask
    public void createUser(String name, String email,
                           String phone, String address) {

        String query = "INSERT INTO users (name, email, phone, address)" +
                "VALUES (?, ?, ?, ?)";


        try (PreparedStatement ps = connection.prepareStatement(query)) { // ask

            ps.setString(1, name);
            ps.setString(2, email);
            ps.setString(3, phone);
            ps.setString(4, address);

            ps.executeUpdate();

            System.out.println("User created successfully.");

        } catch (SQLException e) {
            System.out.println("Error while creating user: "
                    + e.getMessage());
        }
    }

    // 2. Get User By ID
    public void getUserById(int userId) {

        String query = "SELECT * FROM users WHERE user_id = ?";

        try (PreparedStatement ps = connection.prepareStatement(query)) {

            ps.setInt(1, userId); //

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                System.out.println("User ID: "
                        + rs.getInt("user_id"));

                System.out.println("Name: "
                        + rs.getString("name"));

                System.out.println("Email: "
                        + rs.getString("email"));

                System.out.println("Phone: "
                        + rs.getString("phone"));

                System.out.println("Address: "
                        + rs.getString("address"));

            } else {
                System.out.println("User not found.");
            }

        } catch (SQLException e) {
            System.out.println("Error while getting user: "
                    + e.getMessage());
        }
    }

    // 3. Get All Users
    public void getAllUsers() {

        String query = "SELECT * FROM users";

        try (PreparedStatement ps = connection.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                System.out.println(
                        "ID: " + rs.getInt("user_id")
                                + ", Name: " + rs.getString("name")
                                + ", Email: " + rs.getString("email")
                                + ", Phone: " + rs.getString("phone")
                                + ", Address: " + rs.getString("address")
                );
            }

        } catch (SQLException e) {
            System.out.println("Error while getting users: "
                    + e.getMessage());
        }
    }

    // 4. Update User
    public void updateUser(int userId, String name,
                           String email, String phone,
                           String address) {

        String query = "UPDATE users " +
                "SET name = ?, email = ?, phone = ?, address = ? " +
                "WHERE user_id = ?";

        try (PreparedStatement ps = connection.prepareStatement(query)) {

            ps.setString(1, name);
            ps.setString(2, email);
            ps.setString(3, phone);
            ps.setString(4, address);
            ps.setInt(5, userId);

            int rows = ps.executeUpdate();

            if (rows > 0) {
                System.out.println("User updated successfully.");
            } else {
                System.out.println("User not found.");
            }

        } catch (SQLException e) {
            System.out.println("Error while updating user: "
                    + e.getMessage());
        }
    }

    // 5. Delete User
    public void deleteUser(int userId) {

        String query = "DELETE FROM users WHERE user_id = ?";

        try (PreparedStatement ps = connection.prepareStatement(query)) {

            ps.setInt(1, userId);

            int rows = ps.executeUpdate();

            if (rows > 0) {
                System.out.println("User deleted successfully.");
            } else {
                System.out.println("User not found.");
            }

        } catch (SQLException e) {
            System.out.println("Error while deleting user: "
                    + e.getMessage());
        }
    }

    // 6. Check User Exists
    public boolean existsById(int userId) {

        String query = "SELECT EXISTS(SELECT 1 FROM users WHERE user_id = ?)";

        try (PreparedStatement ps = connection.prepareStatement(query)) {

            ps.setInt(1, userId);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getBoolean(1);
            }

        } catch (SQLException e) {
            System.out.println("Error while checking user: "
                    + e.getMessage());
        }

        return false;
    }
}