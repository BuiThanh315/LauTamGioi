package com.lautamgioi.web;

import com.lautamgioi.model.Account;
import com.lautamgioi.model.Role;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebFilter(urlPatterns = {"/booking/*", "/staff/*", "/admin/*"})
public class AuthFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        Account account = (Account) httpRequest.getSession().getAttribute("account");
        if (account == null) {
            httpResponse.sendRedirect(WebUtil.context(httpRequest, "/login"));
            return;
        }
        String path = httpRequest.getRequestURI().substring(httpRequest.getContextPath().length());
        if (path.startsWith("/admin") && account.getRole() != Role.ADMIN) {
            httpResponse.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }
        if (path.startsWith("/staff") && account.getRole() != Role.STAFF && account.getRole() != Role.ADMIN) {
            httpResponse.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }
        chain.doFilter(request, response);
    }
}
