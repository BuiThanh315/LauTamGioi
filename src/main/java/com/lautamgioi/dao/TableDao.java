package com.lautamgioi.dao;

import com.lautamgioi.config.Database;
import com.lautamgioi.model.RestaurantTable;
import com.lautamgioi.model.TableType;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TableDao implements TableRepository {
    @Override
    public List<RestaurantTable> findAll() {
        String sql = """
                SELECT t.*, tt.capacity, tt.class AS table_class
                FROM restaurant_tables t
                JOIN table_types tt ON tt.id = t.table_type_id
                ORDER BY t.table_number
                """;
        return queryTables(sql);
    }

    @Override
    public List<RestaurantTable> findAvailableForType(int tableTypeId) {
        String sql = """
                SELECT t.*, tt.capacity, tt.class AS table_class
                FROM restaurant_tables t
                JOIN table_types tt ON tt.id = t.table_type_id
                WHERE t.table_type_id = ? AND t.status = 'EMPTY'
                ORDER BY t.table_number
                """;
        try (Connection connection = Database.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, tableTypeId);
            try (ResultSet rs = statement.executeQuery()) {
                List<RestaurantTable> tables = new ArrayList<>();
                while (rs.next()) {
                    tables.add(map(rs));
                }
                return tables;
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot load available tables", e);
        }
    }

    @Override
    public void updateStatus(int tableId, String status) {
        try (Connection connection = Database.getConnection();
             PreparedStatement statement = connection.prepareStatement("UPDATE restaurant_tables SET status = ? WHERE id = ?")) {
            statement.setString(1, status);
            statement.setInt(2, tableId);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot update table", e);
        }
    }

    @Override
    public List<TableType> findTypes() {
        String sql = "SELECT * FROM table_types ORDER BY capacity, class";
        try (Connection connection = Database.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet rs = statement.executeQuery()) {
            List<TableType> types = new ArrayList<>();
            while (rs.next()) {
                TableType type = new TableType();
                type.setId(rs.getInt("id"));
                type.setCapacity(rs.getInt("capacity"));
                type.setTableClass(rs.getString("class"));
                types.add(type);
            }
            return types;
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot load table types", e);
        }
    }

    private List<RestaurantTable> queryTables(String sql) {
        try (Connection connection = Database.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet rs = statement.executeQuery()) {
            List<RestaurantTable> tables = new ArrayList<>();
            while (rs.next()) {
                tables.add(map(rs));
            }
            return tables;
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot load tables", e);
        }
    }

    private RestaurantTable map(ResultSet rs) throws SQLException {
        RestaurantTable table = new RestaurantTable();
        table.setId(rs.getInt("id"));
        table.setCode(rs.getString("table_number"));
        table.setTypeId(rs.getInt("table_type_id"));
        table.setTypeName(rs.getString("table_class"));
        table.setCapacity(rs.getInt("capacity"));
        table.setStatus(rs.getString("status"));
        return table;
    }
}
