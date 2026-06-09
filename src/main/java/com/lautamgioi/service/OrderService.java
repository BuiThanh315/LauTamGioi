package com.lautamgioi.service;

import com.lautamgioi.dao.OrderDao;
import com.lautamgioi.dao.OrderRepository;
import com.lautamgioi.model.RevenueRow;

import java.time.LocalDate;
import java.util.List;

public class OrderService implements OrderUseCase {
    private final OrderRepository orderRepository;

    public OrderService() {
        this(new OrderDao());
    }

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public List<RevenueRow> revenue(String type, LocalDate from, LocalDate to) {
        if (from.isAfter(to)) {
            throw new ValidationException("Ngày bắt đầu không được sau ngày kết thúc.");
        }
        return "monthly".equals(type)
                ? orderRepository.monthlyRevenue(from, to)
                : orderRepository.dailyRevenue(from, to);
    }
}
