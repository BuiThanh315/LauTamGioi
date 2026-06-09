package com.lautamgioi.web;

import com.lautamgioi.service.OrderService;
import com.lautamgioi.service.OrderUseCase;
import com.lautamgioi.service.ValidationException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.LocalDate;

@WebServlet("/admin/reports")
public class ReportServlet extends HttpServlet {
    private final OrderUseCase orderService = new OrderService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String type = request.getParameter("type");
        LocalDate to = parseDate(request.getParameter("to"), LocalDate.now());
        LocalDate from = parseDate(request.getParameter("from"), to.minusDays(30));
        request.setAttribute("type", type == null ? "daily" : type);
        request.setAttribute("from", from);
        request.setAttribute("to", to);
        try {
            request.setAttribute("rows", orderService.revenue(type, from, to));
        } catch (ValidationException e) {
            request.setAttribute("error", e.getMessage());
        }
        request.getRequestDispatcher("/WEB-INF/views/admin/reports.jsp").forward(request, response);
    }

    private LocalDate parseDate(String value, LocalDate fallback) {
        try {
            return value == null || value.isBlank() ? fallback : LocalDate.parse(value);
        } catch (RuntimeException e) {
            return fallback;
        }
    }
}
