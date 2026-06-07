package com.lautamgioi.service;

import com.lautamgioi.dao.OrderDao;
import com.lautamgioi.model.RevenueRow;

import java.time.LocalDate;
import java.util.List;

public class OrderService {
    private final OrderDao orderDao = new OrderDao();

    public int createOrder(int bookingId, int tableId) {
        Validators.positiveInt(String.valueOf(bookingId), "Mã booking", 1_000_000);
        Validators.positiveInt(String.valueOf(tableId), "Mã bàn", 1_000_000);
        return orderDao.createOrderFromBooking(bookingId, tableId);
    }

    public void addItem(int orderId, int dishId, int quantity) {
        Validators.positiveInt(String.valueOf(orderId), "Mã order", 1_000_000);
        Validators.positiveInt(String.valueOf(dishId), "Mã món", 1_000_000);
        Validators.positiveInt(String.valueOf(quantity), "Số lượng", 100);
        orderDao.addItem(orderId, dishId, quantity);
    }

    public void pay(int orderId, String method) {
        Validators.positiveInt(String.valueOf(orderId), "Mã order", 1_000_000);
        if (!"CASH".equals(method) && !"BANK_TRANSFER".equals(method)) {
            throw new ValidationException("Phương thức thanh toán không hợp lệ.");
        }
        orderDao.pay(orderId, method);
    }

    public List<RevenueRow> revenue(String type, LocalDate from, LocalDate to) {
        if (from.isAfter(to)) {
            throw new ValidationException("Ngày bắt đầu không được sau ngày kết thúc.");
        }
        if ("monthly".equals(type)) {
            return orderDao.monthlyRevenue(from, to);
        }
        return orderDao.dailyRevenue(from, to);
    }
}
