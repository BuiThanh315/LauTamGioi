package com.lautamgioi.service;

import com.lautamgioi.dao.CategoryDao;
import com.lautamgioi.dao.DishDao;
import com.lautamgioi.model.Category;
import com.lautamgioi.model.Dish;

import java.util.List;

public class AdminService {
    private final CategoryDao categoryDao = new CategoryDao();
    private final DishDao dishDao = new DishDao();

    public List<Category> categories() {
        return categoryDao.findAll(false);
    }

    public List<Dish> dishes() {
        return dishDao.search(null, null, false);
    }

    public void saveCategory(Category category) {
        category.setName(Validators.required(category.getName(), "Tên danh mục", 100));
        category.setDescription(Validators.optional(category.getDescription(), "Mô tả", 1000));
        categoryDao.save(category);
    }

    public void deleteCategory(int id) {
        Validators.positiveInt(String.valueOf(id), "Mã danh mục", 1_000_000);
        categoryDao.delete(id);
    }

    public void saveDish(Dish dish) {
        dish.setName(Validators.required(dish.getName(), "Tên món", 150));
        Validators.positiveInt(String.valueOf(dish.getCategoryId()), "Danh mục", 1_000_000);
        dish.setDescription(Validators.optional(dish.getDescription(), "Mô tả", 1000));
        dish.setPrice(Validators.money(String.valueOf(dish.getPrice()), "Giá món"));
        dish.setImageUrl(Validators.imageUrl(dish.getImageUrl()));
        dishDao.save(dish);
    }

    public void deleteDish(int id) {
        Validators.positiveInt(String.valueOf(id), "Mã món", 1_000_000);
        dishDao.delete(id);
    }
}
