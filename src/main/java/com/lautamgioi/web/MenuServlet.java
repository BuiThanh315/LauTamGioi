package com.lautamgioi.web;

import com.lautamgioi.service.MenuService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(urlPatterns = {"/menu", "/dish"})
public class MenuServlet extends HttpServlet {
    private final MenuService menuService = new MenuService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if ("/dish".equals(request.getServletPath())) {
            Integer id = WebUtil.intParam(request, "id");
            request.setAttribute("dish", id == null ? null : menuService.dish(id).orElse(null));
            request.getRequestDispatcher("/WEB-INF/views/menu/detail.jsp").forward(request, response);
            return;
        }
        request.setAttribute("categories", menuService.categories());
        request.setAttribute("dishes", menuService.dishes(request.getParameter("q"), WebUtil.intParam(request, "categoryId")));
        request.getRequestDispatcher("/WEB-INF/views/menu/list.jsp").forward(request, response);
    }
}
