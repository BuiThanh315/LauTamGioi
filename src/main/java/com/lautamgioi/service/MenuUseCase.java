package com.lautamgioi.service;

import com.lautamgioi.model.Category;
import com.lautamgioi.model.Dish;

import java.util.List;
import java.util.Optional;

public interface MenuUseCase {
    List<Category> categories();
    List<Dish> dishes(String keyword, Integer categoryId);
    Optional<Dish> dish(int id);
}
