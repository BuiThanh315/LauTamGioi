package com.lautamgioi.web;

import com.lautamgioi.config.Database;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

@WebServlet(urlPatterns = {"/health", "/health/db"})
public class HealthServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/plain; charset=UTF-8");

        if ("/health".equals(request.getServletPath())) {
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().write("OK");
            return;
        }

        try (Connection ignored = Database.getConnection()) {
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().write("DB_OK");
        } catch (SQLException | RuntimeException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("DB_ERROR: " + e.getClass().getSimpleName() + ": " + safeMessage(e));
            e.printStackTrace();
        }
    }

    private String safeMessage(Throwable throwable) {
        String message = throwable.getMessage();
        return message == null ? "(no message)" : message;
    }
}
