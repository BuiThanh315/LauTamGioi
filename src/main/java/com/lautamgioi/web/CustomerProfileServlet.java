package com.lautamgioi.web;

import com.lautamgioi.model.Account;
import com.lautamgioi.service.AuthService;
import com.lautamgioi.service.AuthUseCase;
import com.lautamgioi.service.ValidationException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/customer/profile")
public class CustomerProfileServlet extends HttpServlet {
    private final AuthUseCase authService = new AuthService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Account sessionAccount = (Account) request.getSession().getAttribute("account");
        request.setAttribute("profile", authService.account(sessionAccount.getId()));
        request.getRequestDispatcher("/WEB-INF/views/customer/profile.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        Account sessionAccount = (Account) request.getSession().getAttribute("account");
        try {
            Account updated = authService.updateCustomerContact(
                    sessionAccount.getId(),
                    request.getParameter("email"),
                    request.getParameter("phone")
            );
            request.getSession().setAttribute("account", updated);
            WebUtil.flash(request, "Đã cập nhật thông tin tài khoản.");
            response.sendRedirect(WebUtil.context(request, "/customer/profile"));
        } catch (ValidationException | IllegalStateException e) {
            request.setAttribute("error", e.getMessage());
            request.setAttribute("profile", authService.account(sessionAccount.getId()));
            request.getRequestDispatcher("/WEB-INF/views/customer/profile.jsp").forward(request, response);
        }
    }
}
