package com.lautamgioi.service;

import com.lautamgioi.dao.CategoryDao;
import com.lautamgioi.dao.DishDao;
import com.lautamgioi.model.Category;
import com.lautamgioi.model.Dish;

import java.util.List;
import java.util.Optional;

public class MenuService {
    private final CategoryDao categoryDao = new CategoryDao();
    private final DishDao dishDao = new DishDao();

    public List<Category> categories() {
        return categoryDao.findAll(true);
    }

    public List<Dish> dishes(String keyword, Integer categoryId) {
        return dishDao.search(keyword, categoryId, true);
    }

    public Optional<Dish> dish(int id) {
        return dishDao.findById(id);
    }
}
