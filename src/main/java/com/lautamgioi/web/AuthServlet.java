package com.lautamgioi.web;

import com.lautamgioi.model.Account;
import com.lautamgioi.model.Role;
import com.lautamgioi.service.AuthService;
import com.lautamgioi.service.AuthUseCase;
import com.lautamgioi.service.ValidationException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(urlPatterns = {"/login", "/register", "/logout"})
public class AuthServlet extends HttpServlet {
    private final AuthUseCase authService = new AuthService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if ("/logout".equals(request.getServletPath())) {
            request.getSession().invalidate();
            response.sendRedirect(WebUtil.context(request, "/home"));
            return;
        }
        request.getRequestDispatcher("/WEB-INF/views/auth/" + request.getServletPath().substring(1) + ".jsp").forward(request, response);
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
            Account account = authService.login(request.getParameter("username"), request.getParameter("password")).orElse(null);
            if (account == null) {
                forwardWithError(request, response, "/WEB-INF/views/auth/login.jsp", "Sai tài khoản hoặc mật khẩu.");
                return;
            }
            redirectByRole(request, response, account);
        } catch (ValidationException e) {
            forwardWithError(request, response, "/WEB-INF/views/auth/login.jsp", e.getMessage());
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
            forwardWithError(request, response, "/WEB-INF/views/auth/register.jsp",
                    e instanceof ValidationException ? e.getMessage() : "Tài khoản đã tồn tại hoặc dữ liệu không hợp lệ.");
        }
    }

    private void redirectByRole(HttpServletRequest request, HttpServletResponse response, Account account) {
        try {
            request.getSession().setAttribute("account", account);
            if (account.getRole() == Role.ADMIN) {
                response.sendRedirect(WebUtil.context(request, "/admin/bookings"));
            } else if (account.getRole() == Role.STAFF) {
                response.sendRedirect(WebUtil.context(request, "/staff/bookings"));
            } else {
                response.sendRedirect(WebUtil.context(request, "/menu"));
            }
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    private void forwardWithError(HttpServletRequest request, HttpServletResponse response, String view, String message)
            throws ServletException, IOException {
        request.setAttribute("error", message);
        request.getRequestDispatcher(view).forward(request, response);
    }
}
