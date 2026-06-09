package com.lautamgioi.service;

import com.lautamgioi.model.Booking;
import com.lautamgioi.model.RestaurantTable;
import com.lautamgioi.model.TableType;

import java.util.List;

public interface BookingUseCase {
    int create(Booking booking);
    void update(Booking booking);
    List<Booking> history(int accountId);
    List<Booking> allBookings();
    List<Booking> staffBookings();
    Booking booking(int id);
    List<RestaurantTable> tables();
    List<RestaurantTable> availableTables(int tableTypeId);
    List<TableType> tableTypes();
    void confirmByAdmin(int bookingId);
    void seatByStaff(int bookingId, int tableId);
    void markPaid(int bookingId, String paymentMethod);
}
