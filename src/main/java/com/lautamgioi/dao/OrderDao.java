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
import java.util.Optional;

public class OrderDao implements OrderRepository {
    @Override
    public int ensureOrderForBooking(int bookingId, int tableId) {
        Optional<Integer> existing = findOrderIdByBooking(bookingId);
        if (existing.isPresent()) {
            return existing.get();
        }
        String sql = """
                INSERT INTO orders(account_id, table_id, order_type, status)
                SELECT account_id, ?, 'DINE_IN', 'PROCESSING'
                FROM bookings
                WHERE id = ?
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

    @Override
    public void importPreorders(int bookingId, int orderId) {
        String checkSql = "SELECT COUNT(*) AS total FROM order_items WHERE order_id = ?";
        String importSql = """
                INSERT INTO order_items(order_id, dish_id, quantity, actual_price)
                SELECT ?, bp.dish_id, bp.quantity, d.price
                FROM booking_preorders bp
                JOIN dishes d ON d.id = bp.dish_id
                WHERE bp.booking_id = ?
                """;
        try (Connection connection = Database.getConnection();
             PreparedStatement check = connection.prepareStatement(checkSql)) {
            check.setInt(1, orderId);
            try (ResultSet rs = check.executeQuery()) {
                rs.next();
                if (rs.getInt("total") > 0) {
                    return;
                }
            }
            try (PreparedStatement insert = connection.prepareStatement(importSql)) {
                insert.setInt(1, orderId);
                insert.setInt(2, bookingId);
                insert.executeUpdate();
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot import preorder items", e);
        }
    }

    @Override
    public Optional<Integer> findOrderIdByBooking(int bookingId) {
        String sql = """
                SELECT o.id
                FROM orders o
                JOIN bookings b ON b.account_id = o.account_id AND b.table_id = o.table_id
                WHERE b.id = ? AND o.status = 'PROCESSING'
                ORDER BY o.created_at DESC
                LIMIT 1
                """;
        try (Connection connection = Database.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, bookingId);
            try (ResultSet rs = statement.executeQuery()) {
                return rs.next() ? Optional.of(rs.getInt("id")) : Optional.empty();
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot find order by booking", e);
        }
    }

    @Override
    public void payByBooking(int bookingId, String method) {
        Optional<Integer> orderId = findOrderIdByBooking(bookingId);
        if (orderId.isEmpty()) {
            throw new IllegalStateException("Booking chưa có order để thanh toán.");
        }
        pay(orderId.get(), method);
    }

    private void pay(int orderId, String method) {
        String invoiceSql = """
                INSERT INTO invoices(order_id, total_amount, final_amount, payment_method, payment_status)
                VALUES (?, ?, ?, ?, 'PAID')
                ON DUPLICATE KEY UPDATE
                    total_amount = VALUES(total_amount),
                    final_amount = VALUES(final_amount),
                    payment_method = VALUES(payment_method),
                    payment_status = 'PAID'
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

    @Override
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

    @Override
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

    private BigDecimal total(int orderId, Connection connection) throws SQLException {
        String sql = "SELECT COALESCE(SUM(quantity * actual_price), 0) AS total FROM order_items WHERE order_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, orderId);
            try (ResultSet rs = statement.executeQuery()) {
                return rs.next() ? rs.getBigDecimal("total") : BigDecimal.ZERO;
            }
        }
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
