package com.lautamgioi.dao;

import com.lautamgioi.model.Category;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository {
    List<Category> findAll(boolean onlyActive);
    Optional<Category> findById(int id);
    void save(Category category);
    void delete(int id);
}
