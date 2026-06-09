package com.lautamgioi.web;

import com.lautamgioi.model.Account;
import com.lautamgioi.model.Booking;
import com.lautamgioi.service.BookingService;
import com.lautamgioi.service.BookingUseCase;
import com.lautamgioi.service.ValidationException;
import com.lautamgioi.service.Validators;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(urlPatterns = {"/booking/new", "/booking/history"})
public class BookingServlet extends HttpServlet {
    private final BookingUseCase bookingService = new BookingService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Account account = (Account) request.getSession().getAttribute("account");
        if ("/booking/history".equals(request.getServletPath())) {
            request.setAttribute("bookings", bookingService.history(account.getId()));
            request.getRequestDispatcher("/WEB-INF/views/booking/history.jsp").forward(request, response);
            return;
        }
        request.setAttribute("tableTypes", bookingService.tableTypes());
        request.getRequestDispatcher("/WEB-INF/views/booking/form.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        Account account = (Account) request.getSession().getAttribute("account");
        try {
            Booking booking = new Booking();
            booking.setAccountId(account.getId());
            booking.setCustomerName(request.getParameter("customerName"));
            booking.setPhone(request.getParameter("customerPhone"));
            booking.setTableTypeId(Validators.positiveInt(request.getParameter("tableTypeId"), "Loại bàn", 1_000_000));
            booking.setBookingDate(Validators.bookingDate(request.getParameter("bookingDate")));
            booking.setBookingTime(Validators.bookingTime(request.getParameter("bookingTime")));
            booking.setNote(Validators.optional(request.getParameter("note"), "Ghi chú", 1000));
            bookingService.create(booking);
            WebUtil.flash(request, "Đơn đặt bàn đã được gửi tới quản trị viên để xác nhận.");
            response.sendRedirect(WebUtil.context(request, "/booking/history"));
        } catch (ValidationException e) {
            request.setAttribute("error", e.getMessage());
            request.setAttribute("tableTypes", bookingService.tableTypes());
            request.getRequestDispatcher("/WEB-INF/views/booking/form.jsp").forward(request, response);
        }
    }
}
