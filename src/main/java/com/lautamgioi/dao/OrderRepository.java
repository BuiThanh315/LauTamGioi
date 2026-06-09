package com.lautamgioi.dao;

import com.lautamgioi.model.RevenueRow;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface OrderRepository {
    int ensureOrderForBooking(int bookingId, int tableId);
    void importPreorders(int bookingId, int orderId);
    Optional<Integer> findOrderIdByBooking(int bookingId);
    void payByBooking(int bookingId, String method);
    List<RevenueRow> dailyRevenue(LocalDate from, LocalDate to);
    List<RevenueRow> monthlyRevenue(LocalDate from, LocalDate to);
}
