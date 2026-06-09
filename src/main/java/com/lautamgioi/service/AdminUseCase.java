package com.lautamgioi.service;

import com.lautamgioi.model.Booking;
import com.lautamgioi.model.Category;
import com.lautamgioi.model.Dish;

import java.util.List;

public interface AdminUseCase {
    List<Category> categories();
    Category category(int id);
    List<Dish> dishes();
    Dish dish(int id);
    List<Booking> bookings();
    Booking booking(int id);
    void saveCategory(Category category);
    void deleteCategory(int id);
    void saveDish(Dish dish);
    void deleteDish(int id);
}
