package com.lautamgioi.web;

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

@WebServlet(urlPatterns = {"/staff/bookings", "/staff/checkin", "/staff/pay"})
public class StaffServlet extends HttpServlet {
    private final BookingUseCase bookingService = new BookingService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        show(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        try {
            String path = request.getServletPath();
            if ("/staff/checkin".equals(path)) {
                int bookingId = Validators.positiveInt(request.getParameter("bookingId"), "Mã booking", 1_000_000);
                int tableId = Validators.positiveInt(request.getParameter("tableId"), "Mã bàn", 1_000_000);
                bookingService.seatByStaff(bookingId, tableId);
                WebUtil.flash(request, "Đã xác nhận nhận bàn cho khách.");
            }
            if ("/staff/pay".equals(path)) {
                int bookingId = Validators.positiveInt(request.getParameter("bookingId"), "Mã booking", 1_000_000);
                bookingService.markPaid(bookingId, request.getParameter("paymentMethod"));
                WebUtil.flash(request, "Đã xác nhận thanh toán thành công.");
            }
            response.sendRedirect(WebUtil.context(request, "/staff/bookings"));
        } catch (ValidationException | IllegalStateException e) {
            request.setAttribute("error", e.getMessage());
            show(request, response);
        }
    }

    private void show(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("bookings", bookingService.staffBookings());
        request.setAttribute("tables", bookingService.tables());
        request.getRequestDispatcher("/WEB-INF/views/staff/bookings.jsp").forward(request, response);
    }
}
