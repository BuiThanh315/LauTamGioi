package com.lautamgioi.dao;

import com.lautamgioi.config.Database;
import com.lautamgioi.model.Dish;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DishDao {
    public List<Dish> search(String keyword, Integer categoryId, boolean onlyActive) {
        StringBuilder sql = new StringBuilder("""
                SELECT d.*, c.name AS category_name
                FROM dishes d
                JOIN categories c ON c.id = d.category_id
                WHERE 1 = 1
                """);
        List<Object> params = new ArrayList<>();
        if (onlyActive) {
            sql.append(" AND d.status = 'AVAILABLE'");
        }
        if (keyword != null && !keyword.isBlank()) {
            sql.append(" AND (d.name LIKE ? OR d.description LIKE ?)");
            params.add("%" + keyword.trim() + "%");
            params.add("%" + keyword.trim() + "%");
        }
        if (categoryId != null && categoryId > 0) {
            sql.append(" AND d.category_id = ?");
            params.add(categoryId);
        }
        sql.append(" ORDER BY d.id DESC");
        try (Connection connection = Database.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql.toString())) {
            bind(statement, params);
            try (ResultSet rs = statement.executeQuery()) {
                List<Dish> dishes = new ArrayList<>();
                while (rs.next()) {
                    dishes.add(map(rs));
                }
                return dishes;
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot load dishes", e);
        }
    }

    public Optional<Dish> findById(int id) {
        String sql = """
                SELECT d.*, c.name AS category_name
                FROM dishes d
                JOIN categories c ON c.id = d.category_id
                WHERE d.id = ?
                """;
        try (Connection connection = Database.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            try (ResultSet rs = statement.executeQuery()) {
                return rs.next() ? Optional.of(map(rs)) : Optional.empty();
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot find dish", e);
        }
    }

    public void save(Dish dish) {
        if (dish.getId() == 0) {
            insert(dish);
        } else {
            update(dish);
        }
    }

    public void delete(int id) {
        try (Connection connection = Database.getConnection();
             PreparedStatement statement = connection.prepareStatement("UPDATE dishes SET status = 'OUT_OF_STOCK' WHERE id = ?")) {
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot delete dish", e);
        }
    }

    private void insert(Dish dish) {
        String sql = "INSERT INTO dishes(category_id, name, description, price, image_url, status) VALUES (?, ?, ?, ?, ?, 'AVAILABLE')";
        try (Connection connection = Database.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, dish.getCategoryId());
            statement.setString(2, dish.getName());
            statement.setString(3, dish.getDescription());
            statement.setBigDecimal(4, dish.getPrice());
            statement.setString(5, dish.getImageUrl());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot insert dish", e);
        }
    }

    private void update(Dish dish) {
        String sql = "UPDATE dishes SET category_id = ?, name = ?, description = ?, price = ?, image_url = ?, status = ? WHERE id = ?";
        try (Connection connection = Database.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, dish.getCategoryId());
            statement.setString(2, dish.getName());
            statement.setString(3, dish.getDescription());
            statement.setBigDecimal(4, dish.getPrice());
            statement.setString(5, dish.getImageUrl());
            statement.setString(6, dish.isActive() ? "AVAILABLE" : "OUT_OF_STOCK");
            statement.setInt(7, dish.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot update dish", e);
        }
    }

    private void bind(PreparedStatement statement, List<Object> params) throws SQLException {
        for (int i = 0; i < params.size(); i++) {
            statement.setObject(i + 1, params.get(i));
        }
    }

    private Dish map(ResultSet rs) throws SQLException {
        Dish dish = new Dish();
        dish.setId(rs.getInt("id"));
        dish.setCategoryId(rs.getInt("category_id"));
        dish.setCategoryName(rs.getString("category_name"));
        dish.setName(rs.getString("name"));
        dish.setDescription(rs.getString("description"));
        dish.setPrice(rs.getBigDecimal("price"));
        dish.setImageUrl(rs.getString("image_url"));
        dish.setStatus(rs.getString("status"));
        return dish;
    }
}
