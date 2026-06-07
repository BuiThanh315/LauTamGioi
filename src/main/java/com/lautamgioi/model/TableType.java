package com.lautamgioi.model;

public class TableType {
    private int id;
    private int capacity;
    private String tableClass;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public String getTableClass() {
        return tableClass;
    }

    public void setTableClass(String tableClass) {
        this.tableClass = tableClass;
    }

    public String getLabel() {
        return tableClass + " · " + capacity + " khách";
    }
}
