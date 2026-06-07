package com.lautamgioi.web;

import com.lautamgioi.model.Account;
import com.lautamgioi.model.Role;
import com.lautamgioi.service.AuthService;
import com.lautamgioi.service.ValidationException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(urlPatterns = {"/login", "/register", "/logout"})
public class AuthServlet extends HttpServlet {
    private final AuthService authService = new AuthService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = request.getServletPath();
        if ("/logout".equals(path)) {
            request.getSession().invalidate();
            response.sendRedirect(WebUtil.context(request, "/home"));
            return;
        }
        request.getRequestDispatcher("/WEB-INF/views/auth/" + path.substring(1) + ".jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        if ("/register".equals(request.getServletPath())) {
            register(request, response);
        } else {
            login(request, response);
        }
    }

    private void login(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        try {
            authService.login(request.getParameter("username"), request.getParameter("password"))
                    .ifPresentOrElse(account -> {
                        try {
                            request.getSession().setAttribute("account", account);
                            response.sendRedirect(redirectFor(request, account.getRole()));
                        } catch (IOException e) {
                            throw new IllegalStateException(e);
                        }
                    }, () -> {
                        try {
                            request.setAttribute("error", "Sai tài khoản hoặc mật khẩu.");
                            request.getRequestDispatcher("/WEB-INF/views/auth/login.jsp").forward(request, response);
                        } catch (ServletException | IOException e) {
                            throw new IllegalStateException(e);
                        }
                    });
        } catch (ValidationException e) {
            request.setAttribute("error", e.getMessage());
            request.getRequestDispatcher("/WEB-INF/views/auth/login.jsp").forward(request, response);
        }
    }

    private void register(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        try {
            Account account = new Account();
            account.setUsername(request.getParameter("username"));
            account.setPassword(request.getParameter("password"));
            account.setFullName(request.getParameter("fullName"));
            account.setEmail(request.getParameter("email"));
            account.setPhone(request.getParameter("phone"));
            authService.register(account);
            WebUtil.flash(request, "Đăng ký thành công. Mời bạn đăng nhập.");
            response.sendRedirect(WebUtil.context(request, "/login"));
        } catch (ValidationException | IllegalStateException e) {
            request.setAttribute("error", e instanceof ValidationException ? e.getMessage() : "Tài khoản đã tồn tại hoặc dữ liệu không hợp lệ.");
            request.getRequestDispatcher("/WEB-INF/views/auth/register.jsp").forward(request, response);
        }
    }

    private String redirectFor(HttpServletRequest request, Role role) {
        if (role == Role.ADMIN) {
            return WebUtil.context(request, "/admin/dishes");
        }
        if (role == Role.STAFF) {
            return WebUtil.context(request, "/staff/bookings");
        }
        return WebUtil.context(request, "/menu");
    }
}
