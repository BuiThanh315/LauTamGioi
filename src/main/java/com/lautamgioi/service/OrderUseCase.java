package com.lautamgioi.service;

import com.lautamgioi.model.RevenueRow;

import java.time.LocalDate;
import java.util.List;

public interface OrderUseCase {
    List<RevenueRow> revenue(String type, LocalDate from, LocalDate to);
}
