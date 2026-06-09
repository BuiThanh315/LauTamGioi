package com.lautamgioi.web;

import com.lautamgioi.model.Booking;
import com.lautamgioi.service.AdminService;
import com.lautamgioi.service.AdminUseCase;
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

@WebServlet(urlPatterns = {"/admin/bookings", "/admin/booking/confirm", "/admin/booking/save"})
public class AdminBookingServlet extends HttpServlet {
    private final AdminUseCase adminService = new AdminService();
    private final BookingUseCase bookingService = new BookingService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        show(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        try {
            String path = request.getServletPath();
            if ("/admin/booking/confirm".equals(path)) {
                bookingService.confirmByAdmin(Validators.positiveInt(request.getParameter("bookingId"), "Mã booking", 1_000_000));
                WebUtil.flash(request, "Đã xác nhận đơn đặt bàn.");
            }
            if ("/admin/booking/save".equals(path)) {
                Booking booking = bookingService.booking(Validators.positiveInt(request.getParameter("id"), "Mã booking", 1_000_000));
                booking.setCustomerName(request.getParameter("customerName"));
                booking.setPhone(request.getParameter("customerPhone"));
                booking.setTableTypeId(Validators.positiveInt(request.getParameter("tableTypeId"), "Loại bàn", 1_000_000));
                booking.setBookingDate(Validators.bookingDate(request.getParameter("bookingDate")));
                booking.setBookingTime(Validators.bookingTime(request.getParameter("bookingTime")));
                booking.setNote(Validators.optional(request.getParameter("note"), "Ghi chú", 1000));
                bookingService.update(booking);
                WebUtil.flash(request, "Đã cập nhật đơn đặt bàn.");
            }
            response.sendRedirect(WebUtil.context(request, "/admin/bookings"));
        } catch (ValidationException | IllegalStateException e) {
            request.setAttribute("error", e.getMessage());
            show(request, response);
        }
    }

    private void show(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("bookings", adminService.bookings());
        request.setAttribute("tableTypes", bookingService.tableTypes());
        Integer editBookingId = WebUtil.intParam(request, "editBookingId");
        if (editBookingId != null) {
            request.setAttribute("editingBooking", adminService.booking(editBookingId));
        }
        request.getRequestDispatcher("/WEB-INF/views/admin/bookings.jsp").forward(request, response);
    }
}
