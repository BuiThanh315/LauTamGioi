package com.lautamgioi.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class Booking {
    private int id;
    private Integer accountId;
    private String customerName;
    private String phone;
    private int tableTypeId;
    private String tableTypeLabel;
    private int capacity;
    private String tableClass;
    private Integer assignedTableId;
    private String assignedTableNumber;
    private LocalDate bookingDate;
    private LocalTime bookingTime;
    private String note;
    private String status;
    private LocalDateTime createdAt;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Integer getAccountId() {
        return accountId;
    }

    public void setAccountId(Integer accountId) {
        this.accountId = accountId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getTableTypeId() {
        return tableTypeId;
    }

    public void setTableTypeId(int tableTypeId) {
        this.tableTypeId = tableTypeId;
    }

    public String getTableTypeLabel() {
        return tableTypeLabel;
    }

    public void setTableTypeLabel(String tableTypeLabel) {
        this.tableTypeLabel = tableTypeLabel;
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

    public Integer getAssignedTableId() {
        return assignedTableId;
    }

    public void setAssignedTableId(Integer assignedTableId) {
        this.assignedTableId = assignedTableId;
    }

    public String getAssignedTableNumber() {
        return assignedTableNumber;
    }

    public void setAssignedTableNumber(String assignedTableNumber) {
        this.assignedTableNumber = assignedTableNumber;
    }

    public LocalDate getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(LocalDate bookingDate) {
        this.bookingDate = bookingDate;
    }

    public LocalTime getBookingTime() {
        return bookingTime;
    }

    public void setBookingTime(LocalTime bookingTime) {
        this.bookingTime = bookingTime;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public boolean isPending() {
        return "PENDING".equals(status);
    }

    public boolean isConfirmed() {
        return "CONFIRMED".equals(status);
    }

    public boolean isSeated() {
        return "SEATED".equals(status);
    }

    public boolean isPaid() {
        return "PAID".equals(status);
    }
}
