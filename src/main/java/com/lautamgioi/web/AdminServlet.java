package com.lautamgioi.web;

import com.lautamgioi.model.Category;
import com.lautamgioi.model.Dish;
import com.lautamgioi.service.AdminService;
import com.lautamgioi.service.AdminUseCase;
import com.lautamgioi.service.ValidationException;
import com.lautamgioi.service.Validators;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(urlPatterns = {
        "/admin/categories", "/admin/category/save", "/admin/category/delete",
        "/admin/dishes", "/admin/dish/save", "/admin/dish/delete"
})
public class AdminServlet extends HttpServlet {
    private final AdminUseCase adminService = new AdminService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String view = request.getServletPath().contains("categor") ? "categories" : "dishes";
        show(request, response, view);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        try {
            String path = request.getServletPath();
            if (path.contains("category/save")) {
                Category category = new Category();
                Integer id = WebUtil.intParam(request, "id");
                category.setId(id == null ? 0 : id);
                category.setName(request.getParameter("name"));
                category.setDescription(request.getParameter("description"));
                adminService.saveCategory(category);
                WebUtil.flash(request, "Đã lưu danh mục.");
                response.sendRedirect(WebUtil.context(request, "/admin/categories"));
                return;
            }
            if (path.contains("category/delete")) {
                adminService.deleteCategory(Validators.positiveInt(request.getParameter("id"), "Mã danh mục", 1_000_000));
                WebUtil.flash(request, "Đã xóa danh mục.");
                response.sendRedirect(WebUtil.context(request, "/admin/categories"));
                return;
            }
            if (path.contains("dish/save")) {
                Dish dish = new Dish();
                Integer id = WebUtil.intParam(request, "id");
                dish.setId(id == null ? 0 : id);
                dish.setCategoryId(Validators.positiveInt(request.getParameter("categoryId"), "Danh mục", 1_000_000));
                dish.setName(request.getParameter("name"));
                dish.setDescription(request.getParameter("description"));
                dish.setPrice(Validators.money(request.getParameter("price"), "Giá món"));
                dish.setImageUrl(request.getParameter("imageUrl"));
                dish.setStatus(request.getParameter("status"));
                adminService.saveDish(dish);
                WebUtil.flash(request, "Đã lưu món ăn.");
                response.sendRedirect(WebUtil.context(request, "/admin/dishes"));
                return;
            }
            adminService.deleteDish(Validators.positiveInt(request.getParameter("id"), "Mã món", 1_000_000));
            WebUtil.flash(request, "Đã xóa món ăn.");
            response.sendRedirect(WebUtil.context(request, "/admin/dishes"));
        } catch (ValidationException | IllegalStateException e) {
            request.setAttribute("error", e.getMessage());
            show(request, response, request.getServletPath().contains("category") ? "categories" : "dishes");
        }
    }

    private void show(HttpServletRequest request, HttpServletResponse response, String view) throws ServletException, IOException {
        request.setAttribute("categories", adminService.categories());
        request.setAttribute("dishes", adminService.dishes());

        Integer editCategoryId = WebUtil.intParam(request, "editCategoryId");
        if (editCategoryId != null) {
            request.setAttribute("editingCategory", adminService.category(editCategoryId));
        }
        Integer editDishId = WebUtil.intParam(request, "editDishId");
        if (editDishId != null) {
            request.setAttribute("editingDish", adminService.dish(editDishId));
        }
        request.getRequestDispatcher("/WEB-INF/views/admin/" + view + ".jsp").forward(request, response);
    }
}
