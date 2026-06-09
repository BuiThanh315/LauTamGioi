package com.lautamgioi.dao;

import com.lautamgioi.model.Booking;

import java.util.List;
import java.util.Optional;

public interface BookingRepository {
    int create(Booking booking);
    void update(Booking booking);
    List<Booking> findByAccount(int accountId);
    List<Booking> findAll();
    List<Booking> findByStatuses(String... statuses);
    Optional<Booking> findById(int id);
    void confirm(int bookingId);
    void seat(int bookingId, int tableId);
    void markPaid(int bookingId);
}
