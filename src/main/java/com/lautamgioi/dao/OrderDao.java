package com.lautamgioi.dao;

import com.lautamgioi.config.Database;
import com.lautamgioi.model.RevenueRow;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class OrderDao {
    public int createOrderFromBooking(int bookingId, int tableId) {
        String sql = """
                INSERT INTO orders(account_id, table_id, order_type, status)
                SELECT account_id, ?, 'DINE_IN', 'PROCESSING' FROM bookings WHERE id = ?
                """;
        try (Connection connection = Database.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setInt(1, tableId);
            statement.setInt(2, bookingId);
            statement.executeUpdate();
            try (ResultSet keys = statement.getGeneratedKeys()) {
                return keys.next() ? keys.getInt(1) : 0;
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot create order", e);
        }
    }

    public void addItem(int orderId, int dishId, int quantity) {
        String sql = """
                INSERT INTO order_items(order_id, dish_id, quantity, actual_price)
                SELECT ?, id, ?, price FROM dishes WHERE id = ? AND status = 'AVAILABLE'
                """;
        try (Connection connection = Database.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, orderId);
            statement.setInt(2, quantity);
            statement.setInt(3, dishId);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot add order item", e);
        }
    }

    public BigDecimal total(int orderId, Connection connection) throws SQLException {
        String sql = "SELECT COALESCE(SUM(quantity * actual_price), 0) AS total FROM order_items WHERE order_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, orderId);
            try (ResultSet rs = statement.executeQuery()) {
                return rs.next() ? rs.getBigDecimal("total") : BigDecimal.ZERO;
            }
        }
    }

    public void pay(int orderId, String method) {
        String invoiceSql = """
                INSERT INTO invoices(order_id, total_amount, final_amount, payment_method, payment_status)
                VALUES (?, ?, ?, ?, 'PAID')
                """;
        String orderSql = "UPDATE orders SET status = 'COMPLETED' WHERE id = ?";
        try (Connection connection = Database.getConnection()) {
            connection.setAutoCommit(false);
            try (PreparedStatement invoice = connection.prepareStatement(invoiceSql);
                 PreparedStatement order = connection.prepareStatement(orderSql)) {
                BigDecimal total = total(orderId, connection);
                invoice.setInt(1, orderId);
                invoice.setBigDecimal(2, total);
                invoice.setBigDecimal(3, total);
                invoice.setString(4, method);
                invoice.executeUpdate();
                order.setInt(1, orderId);
                order.executeUpdate();
                connection.commit();
            } catch (SQLException e) {
                connection.rollback();
                throw e;
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot pay order", e);
        }
    }

    public List<RevenueRow> dailyRevenue(LocalDate from, LocalDate to) {
        String sql = """
                SELECT DATE(created_at) AS period, COUNT(*) AS invoice_count, SUM(final_amount) AS total
                FROM invoices
                WHERE payment_status = 'PAID' AND DATE(created_at) BETWEEN ? AND ?
                GROUP BY DATE(created_at)
                ORDER BY period DESC
                """;
        return revenue(sql, from, to);
    }

    public List<RevenueRow> monthlyRevenue(LocalDate from, LocalDate to) {
        String sql = """
                SELECT DATE_FORMAT(created_at, '%Y-%m') AS period, COUNT(*) AS invoice_count, SUM(final_amount) AS total
                FROM invoices
                WHERE payment_status = 'PAID' AND DATE(created_at) BETWEEN ? AND ?
                GROUP BY DATE_FORMAT(created_at, '%Y-%m')
                ORDER BY period DESC
                """;
        return revenue(sql, from, to);
    }

    private List<RevenueRow> revenue(String sql, LocalDate from, LocalDate to) {
        try (Connection connection = Database.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setDate(1, Date.valueOf(from));
            statement.setDate(2, Date.valueOf(to));
            try (ResultSet rs = statement.executeQuery()) {
                List<RevenueRow> rows = new ArrayList<>();
                while (rs.next()) {
                    rows.add(new RevenueRow(rs.getString("period"), rs.getInt("invoice_count"), rs.getBigDecimal("total")));
                }
                return rows;
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot load revenue", e);
        }
    }
}
