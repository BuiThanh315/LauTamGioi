package com.lautamgioi.service;

import com.lautamgioi.dao.BookingDao;
import com.lautamgioi.dao.TableDao;
import com.lautamgioi.model.Booking;
import com.lautamgioi.model.RestaurantTable;
import com.lautamgioi.model.TableType;

import java.util.List;

public class BookingService {
    private final BookingDao bookingDao = new BookingDao();
    private final TableDao tableDao = new TableDao();

    public int create(Booking booking) {
        booking.setCustomerName(Validators.required(booking.getCustomerName(), "Tên khách", 100));
        booking.setPhone(Validators.phone(booking.getPhone()));
        Validators.positiveInt(String.valueOf(booking.getTableTypeId()), "Loại bàn", 1_000_000);
        if (booking.getBookingDate() == null || booking.getBookingTime() == null) {
            throw new ValidationException("Ngày giờ đặt bàn không hợp lệ.");
        }
        return bookingDao.create(booking);
    }

    public List<Booking> history(int accountId) {
        return bookingDao.findByAccount(accountId);
    }

    public List<Booking> allBookings() {
        return bookingDao.findAll();
    }

    public List<RestaurantTable> tables() {
        return tableDao.findAll();
    }

    public List<TableType> tableTypes() {
        return tableDao.findTypes();
    }

    public void confirm(int bookingId, int tableId) {
        Validators.positiveInt(String.valueOf(bookingId), "Mã booking", 1_000_000);
        Validators.positiveInt(String.valueOf(tableId), "Mã bàn", 1_000_000);
        bookingDao.confirm(bookingId);
        tableDao.updateStatus(tableId, "RESERVED");
    }
}
