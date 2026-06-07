package com.lautamgioi.web;

import com.lautamgioi.service.BookingService;
import com.lautamgioi.service.MenuService;
import com.lautamgioi.service.OrderService;
import com.lautamgioi.service.ValidationException;
import com.lautamgioi.service.Validators;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(urlPatterns = {"/staff/bookings", "/staff/confirm", "/staff/order", "/staff/pay"})
public class StaffServlet extends HttpServlet {
    private final BookingService bookingService = new BookingService();
    private final MenuService menuService = new MenuService();
    private final OrderService orderService = new OrderService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        show(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        try {
            String path = request.getServletPath();
            if ("/staff/confirm".equals(path)) {
                bookingService.confirm(
                        Validators.positiveInt(request.getParameter("bookingId"), "Mã booking", 1_000_000),
                        Validators.positiveInt(request.getParameter("tableId"), "Mã bàn", 1_000_000));
                WebUtil.flash(request, "Đã xác nhận booking và giữ bàn.");
            }
            if ("/staff/order".equals(path)) {
                int orderId = orderService.createOrder(
                        Validators.positiveInt(request.getParameter("bookingId"), "Mã booking", 1_000_000),
                        Validators.positiveInt(request.getParameter("tableId"), "Mã bàn", 1_000_000));
                orderService.addItem(orderId,
                        Validators.positiveInt(request.getParameter("dishId"), "Mã món", 1_000_000),
                        Validators.positiveInt(request.getParameter("quantity"), "Số lượng", 100));
                WebUtil.flash(request, "Đã tạo order #" + orderId + ".");
            }
            if ("/staff/pay".equals(path)) {
                orderService.pay(
                        Validators.positiveInt(request.getParameter("orderId"), "Mã order", 1_000_000),
                        request.getParameter("paymentMethod"));
                WebUtil.flash(request, "Thanh toán thành công.");
            }
            response.sendRedirect(WebUtil.context(request, "/staff/bookings"));
        } catch (ValidationException | IllegalStateException e) {
            request.setAttribute("error", e.getMessage());
            show(request, response);
        }
    }

    private void show(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("bookings", bookingService.allBookings());
        request.setAttribute("tables", bookingService.tables());
        request.setAttribute("dishes", menuService.dishes(null, null));
        request.getRequestDispatcher("/WEB-INF/views/staff/bookings.jsp").forward(request, response);
    }
}
