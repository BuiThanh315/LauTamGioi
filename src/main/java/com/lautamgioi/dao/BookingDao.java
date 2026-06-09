package com.lautamgioi.dao;

import com.lautamgioi.config.Database;
import com.lautamgioi.model.Booking;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class BookingDao implements BookingRepository {
    @Override
    public int create(Booking booking) {
        String sql = """
                INSERT INTO bookings(account_id, customer_name, customer_phone, booking_date, booking_time, table_type_id, special_notes, status)
                VALUES (?, ?, ?, ?, ?, ?, ?, 'PENDING')
                """;
        try (Connection connection = Database.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            if (booking.getAccountId() == null) {
                statement.setNull(1, java.sql.Types.INTEGER);
            } else {
                statement.setInt(1, booking.getAccountId());
            }
            statement.setString(2, booking.getCustomerName());
            statement.setString(3, booking.getPhone());
            statement.setDate(4, Date.valueOf(booking.getBookingDate()));
            statement.setTime(5, Time.valueOf(booking.getBookingTime()));
            statement.setInt(6, booking.getTableTypeId());
            statement.setString(7, booking.getNote());
            statement.executeUpdate();
            try (ResultSet keys = statement.getGeneratedKeys()) {
                return keys.next() ? keys.getInt(1) : 0;
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot create booking", e);
        }
    }

    @Override
    public void update(Booking booking) {
        String sql = """
                UPDATE bookings
                SET customer_name = ?, customer_phone = ?, booking_date = ?, booking_time = ?, table_type_id = ?, special_notes = ?
                WHERE id = ?
                """;
        try (Connection connection = Database.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, booking.getCustomerName());
            statement.setString(2, booking.getPhone());
            statement.setDate(3, Date.valueOf(booking.getBookingDate()));
            statement.setTime(4, Time.valueOf(booking.getBookingTime()));
            statement.setInt(5, booking.getTableTypeId());
            statement.setString(6, booking.getNote());
            statement.setInt(7, booking.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot update booking", e);
        }
    }

    @Override
    public List<Booking> findByAccount(int accountId) {
        return query(baseSql() + " WHERE b.account_id = ? ORDER BY b.booking_date DESC, b.booking_time DESC", accountId);
    }

    @Override
    public List<Booking> findAll() {
        return query(baseSql() + " ORDER BY b.created_at DESC, b.booking_date DESC, b.booking_time DESC");
    }

    @Override
    public List<Booking> findByStatuses(String... statuses) {
        String placeholders = Arrays.stream(statuses).map(s -> "?").collect(Collectors.joining(","));
        String sql = baseSql() + " WHERE b.status IN (" + placeholders + ") ORDER BY b.booking_date ASC, b.booking_time ASC";
        return query(sql, (Object[]) statuses);
    }

    @Override
    public Optional<Booking> findById(int id) {
        List<Booking> bookings = query(baseSql() + " WHERE b.id = ?", id);
        return bookings.isEmpty() ? Optional.empty() : Optional.of(bookings.getFirst());
    }

    @Override
    public void confirm(int bookingId) {
        updateStatus("UPDATE bookings SET status = 'CONFIRMED' WHERE id = ? AND status = 'PENDING'", bookingId);
    }

    @Override
    public void seat(int bookingId, int tableId) {
        String sql = "UPDATE bookings SET status = 'SEATED', table_id = ? WHERE id = ? AND status = 'CONFIRMED'";
        try (Connection connection = Database.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, tableId);
            statement.setInt(2, bookingId);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot seat booking", e);
        }
    }

    @Override
    public void markPaid(int bookingId) {
        updateStatus("UPDATE bookings SET status = 'PAID' WHERE id = ? AND status = 'SEATED'", bookingId);
    }

    private void updateStatus(String sql, int bookingId) {
        try (Connection connection = Database.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, bookingId);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot update booking status", e);
        }
    }

    private String baseSql() {
        return """
                SELECT b.*, tt.capacity, tt.class AS table_class, rt.table_number
                FROM bookings b
                JOIN table_types tt ON tt.id = b.table_type_id
                LEFT JOIN restaurant_tables rt ON rt.id = b.table_id
                """;
    }

    private List<Booking> query(String sql, Object... params) {
        try (Connection connection = Database.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            for (int i = 0; i < params.length; i++) {
                statement.setObject(i + 1, params[i]);
            }
            try (ResultSet rs = statement.executeQuery()) {
                List<Booking> bookings = new ArrayList<>();
                while (rs.next()) {
                    bookings.add(map(rs));
                }
                return bookings;
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot load bookings", e);
        }
    }

    private Booking map(ResultSet rs) throws SQLException {
        Booking booking = new Booking();
        booking.setId(rs.getInt("id"));
        int accountId = rs.getInt("account_id");
        booking.setAccountId(rs.wasNull() ? null : accountId);
        booking.setCustomerName(rs.getString("customer_name"));
        booking.setPhone(rs.getString("customer_phone"));
        booking.setTableTypeId(rs.getInt("table_type_id"));
        booking.setCapacity(rs.getInt("capacity"));
        booking.setTableClass(rs.getString("table_class"));
        booking.setTableTypeLabel(rs.getString("table_class") + " · " + rs.getInt("capacity") + " khách");
        int tableId = rs.getInt("table_id");
        booking.setAssignedTableId(rs.wasNull() ? null : tableId);
        booking.setAssignedTableNumber(rs.getString("table_number"));
        booking.setBookingDate(rs.getDate("booking_date").toLocalDate());
        booking.setBookingTime(rs.getTime("booking_time").toLocalTime());
        booking.setNote(rs.getString("special_notes"));
        booking.setStatus(rs.getString("status"));
        Timestamp createdAt = rs.getTimestamp("created_at");
        if (createdAt != null) {
            booking.setCreatedAt(createdAt.toLocalDateTime());
        }
        return booking;
    }
}
