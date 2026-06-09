package com.lautamgioi.service;

import com.lautamgioi.dao.CategoryDao;
import com.lautamgioi.dao.CategoryRepository;
import com.lautamgioi.dao.DishDao;
import com.lautamgioi.dao.DishRepository;
import com.lautamgioi.model.Category;
import com.lautamgioi.model.Dish;

import java.util.List;
import java.util.Optional;

public class MenuService implements MenuUseCase {
    private final CategoryRepository categoryRepository;
    private final DishRepository dishRepository;

    public MenuService() {
        this(new CategoryDao(), new DishDao());
    }

    public MenuService(CategoryRepository categoryRepository, DishRepository dishRepository) {
        this.categoryRepository = categoryRepository;
        this.dishRepository = dishRepository;
    }

    @Override
    public List<Category> categories() {
        return categoryRepository.findAll(true);
    }

    @Override
    public List<Dish> dishes(String keyword, Integer categoryId) {
        return dishRepository.search(keyword, categoryId, true);
    }

    @Override
    public Optional<Dish> dish(int id) {
        return dishRepository.findById(id);
    }
}
