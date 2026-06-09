package com.lautamgioi.dao;

import com.lautamgioi.model.Dish;

import java.util.List;
import java.util.Optional;

public interface DishRepository {
    List<Dish> search(String keyword, Integer categoryId, boolean onlyActive);
    Optional<Dish> findById(int id);
    void save(Dish dish);
    void delete(int id);
}
