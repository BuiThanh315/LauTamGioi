package com.lautamgioi.dao;

import com.lautamgioi.model.RestaurantTable;
import com.lautamgioi.model.TableType;

import java.util.List;

public interface TableRepository {
    List<RestaurantTable> findAll();
    List<RestaurantTable> findAvailableForType(int tableTypeId);
    void updateStatus(int tableId, String status);
    List<TableType> findTypes();
}
