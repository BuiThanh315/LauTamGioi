package com.lautamgioi.dao;

import com.lautamgioi.config.Database;
import com.lautamgioi.model.Account;
import com.lautamgioi.model.Role;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Optional;

public class AccountDao implements AccountRepository {
    @Override
    public Optional<Account> findById(int id) {
        String sql = "SELECT * FROM accounts WHERE id = ? AND status = 'ACTIVE'";
        try (Connection connection = Database.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            try (ResultSet rs = statement.executeQuery()) {
                return rs.next() ? Optional.of(map(rs)) : Optional.empty();
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot find account by id", e);
        }
    }

    @Override
    public Optional<Account> findByUsername(String username) {
        String sql = "SELECT * FROM accounts WHERE username = ? AND status = 'ACTIVE'";
        try (Connection connection = Database.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, username);
            try (ResultSet rs = statement.executeQuery()) {
                return rs.next() ? Optional.of(map(rs)) : Optional.empty();
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot find account", e);
        }
    }

    @Override
    public int createCustomer(Account account) {
        String sql = """
                INSERT INTO accounts(username, password, full_name, email, phone, role)
                VALUES (?, ?, ?, ?, ?, 'CUSTOMER')
                """;
        try (Connection connection = Database.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, account.getUsername());
            statement.setString(2, account.getPassword());
            statement.setString(3, account.getFullName());
            statement.setString(4, account.getEmail());
            statement.setString(5, account.getPhone());
            statement.executeUpdate();
            try (ResultSet keys = statement.getGeneratedKeys()) {
                return keys.next() ? keys.getInt(1) : 0;
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot create account", e);
        }
    }

    @Override
    public void updateCustomerContact(Account account) {
        String sql = """
                UPDATE accounts
                SET email = ?, phone = ?
                WHERE id = ? AND role = 'CUSTOMER' AND status = 'ACTIVE'
                """;
        try (Connection connection = Database.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, account.getEmail());
            statement.setString(2, account.getPhone());
            statement.setInt(3, account.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot update account", e);
        }
    }

    private Account map(ResultSet rs) throws SQLException {
        Account account = new Account();
        account.setId(rs.getInt("id"));
        account.setUsername(rs.getString("username"));
        account.setPassword(rs.getString("password"));
        account.setFullName(rs.getString("full_name"));
        account.setEmail(rs.getString("email"));
        account.setPhone(rs.getString("phone"));
        account.setRole(Role.valueOf(rs.getString("role")));
        account.setStatus(rs.getString("status"));
        Timestamp createdAt = rs.getTimestamp("created_at");
        if (createdAt != null) {
            account.setCreatedAt(createdAt.toLocalDateTime());
        }
        return account;
    }
}
