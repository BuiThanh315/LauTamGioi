package com.lautamgioi.service;

import com.lautamgioi.dao.BookingDao;
import com.lautamgioi.dao.BookingRepository;
import com.lautamgioi.dao.CategoryDao;
import com.lautamgioi.dao.CategoryRepository;
import com.lautamgioi.dao.DishDao;
import com.lautamgioi.dao.DishRepository;
import com.lautamgioi.model.Booking;
import com.lautamgioi.model.Category;
import com.lautamgioi.model.Dish;

import java.util.List;

public class AdminService implements AdminUseCase {
    private final CategoryRepository categoryRepository;
    private final DishRepository dishRepository;
    private final BookingRepository bookingRepository;

    public AdminService() {
        this(new CategoryDao(), new DishDao(), new BookingDao());
    }

    public AdminService(CategoryRepository categoryRepository, DishRepository dishRepository, BookingRepository bookingRepository) {
        this.categoryRepository = categoryRepository;
        this.dishRepository = dishRepository;
        this.bookingRepository = bookingRepository;
    }

    @Override
    public List<Category> categories() {
        return categoryRepository.findAll(false);
    }

    @Override
    public Category category(int id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new ValidationException("Không tìm thấy danh mục."));
    }

    @Override
    public List<Dish> dishes() {
        return dishRepository.search(null, null, false);
    }

    @Override
    public Dish dish(int id) {
        return dishRepository.findById(id)
                .orElseThrow(() -> new ValidationException("Không tìm thấy món ăn."));
    }

    @Override
    public List<Booking> bookings() {
        return bookingRepository.findAll();
    }

    @Override
    public Booking booking(int id) {
        return bookingRepository.findById(id)
                .orElseThrow(() -> new ValidationException("Không tìm thấy đơn đặt bàn."));
    }

    @Override
    public void saveCategory(Category category) {
        category.setName(Validators.required(category.getName(), "Tên danh mục", 100));
        category.setDescription(Validators.optional(category.getDescription(), "Mô tả", 1000));
        categoryRepository.save(category);
    }

    @Override
    public void deleteCategory(int id) {
        categoryRepository.delete(id);
    }

    @Override
    public void saveDish(Dish dish) {
        dish.setName(Validators.required(dish.getName(), "Tên món", 150));
        Validators.positiveInt(String.valueOf(dish.getCategoryId()), "Danh mục", 1_000_000);
        dish.setDescription(Validators.optional(dish.getDescription(), "Mô tả", 1000));
        dish.setPrice(Validators.money(String.valueOf(dish.getPrice()), "Giá món"));
        dish.setImageUrl(Validators.imageUrl(dish.getImageUrl()));
        if (dish.getStatus() == null || dish.getStatus().isBlank()) {
            dish.setStatus("AVAILABLE");
        }
        dishRepository.save(dish);
    }

    @Override
    public void deleteDish(int id) {
        dishRepository.delete(id);
    }
}
