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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BookingDao {
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

    public List<Booking> findByAccount(int accountId) {
        return query("WHERE b.account_id = ? ORDER BY b.booking_date DESC, b.booking_time DESC", accountId);
    }

    public List<Booking> findAll() {
        return query("ORDER BY b.booking_date DESC, b.booking_time DESC");
    }

    public Optional<Booking> findById(int id) {
        List<Booking> bookings = query("WHERE b.id = ?", id);
        return bookings.isEmpty() ? Optional.empty() : Optional.of(bookings.getFirst());
    }

    public void confirm(int bookingId) {
        String sql = "UPDATE bookings SET status = 'CONFIRMED' WHERE id = ?";
        try (Connection connection = Database.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, bookingId);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot confirm booking", e);
        }
    }

    private List<Booking> query(String suffix, Object... params) {
        String sql = """
                SELECT b.*, tt.capacity, tt.class AS table_class
                FROM bookings b
                JOIN table_types tt ON tt.id = b.table_type_id
                """ + " " + suffix;
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
        booking.setBookingDate(rs.getDate("booking_date").toLocalDate());
        booking.setBookingTime(rs.getTime("booking_time").toLocalTime());
        booking.setNote(rs.getString("special_notes"));
        booking.setStatus(rs.getString("status"));
        return booking;
    }
}
