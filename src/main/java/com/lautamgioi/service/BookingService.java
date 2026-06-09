package com.lautamgioi.service;

import com.lautamgioi.dao.BookingDao;
import com.lautamgioi.dao.BookingRepository;
import com.lautamgioi.dao.OrderDao;
import com.lautamgioi.dao.OrderRepository;
import com.lautamgioi.dao.TableDao;
import com.lautamgioi.dao.TableRepository;
import com.lautamgioi.model.Booking;
import com.lautamgioi.model.RestaurantTable;
import com.lautamgioi.model.TableType;

import java.util.List;

public class BookingService implements BookingUseCase {
    private final BookingRepository bookingRepository;
    private final TableRepository tableRepository;
    private final OrderRepository orderRepository;

    public BookingService() {
        this(new BookingDao(), new TableDao(), new OrderDao());
    }

    public BookingService(BookingRepository bookingRepository, TableRepository tableRepository, OrderRepository orderRepository) {
        this.bookingRepository = bookingRepository;
        this.tableRepository = tableRepository;
        this.orderRepository = orderRepository;
    }

    @Override
    public int create(Booking booking) {
        validateEditableBooking(booking);
        return bookingRepository.create(booking);
    }

    @Override
    public void update(Booking booking) {
        validateEditableBooking(booking);
        Booking current = booking(booking.getId());
        if (!current.isPending() && !current.isConfirmed()) {
            throw new ValidationException("Chỉ có thể chỉnh sửa đơn đang chờ duyệt hoặc đã xác nhận.");
        }
        bookingRepository.update(booking);
    }

    @Override
    public List<Booking> history(int accountId) {
        return bookingRepository.findByAccount(accountId);
    }

    @Override
    public List<Booking> allBookings() {
        return bookingRepository.findAll();
    }

    @Override
    public List<Booking> staffBookings() {
        return bookingRepository.findByStatuses("CONFIRMED", "SEATED");
    }

    @Override
    public Booking booking(int id) {
        return bookingRepository.findById(id)
                .orElseThrow(() -> new ValidationException("Không tìm thấy đơn đặt bàn."));
    }

    @Override
    public List<RestaurantTable> tables() {
        return tableRepository.findAll();
    }

    @Override
    public List<RestaurantTable> availableTables(int tableTypeId) {
        return tableRepository.findAvailableForType(tableTypeId);
    }

    @Override
    public List<TableType> tableTypes() {
        return tableRepository.findTypes();
    }

    @Override
    public void confirmByAdmin(int bookingId) {
        Booking booking = booking(bookingId);
        if (!booking.isPending()) {
            throw new ValidationException("Chỉ có thể xác nhận đơn đang chờ duyệt.");
        }
        bookingRepository.confirm(bookingId);
    }

    @Override
    public void seatByStaff(int bookingId, int tableId) {
        Booking booking = booking(bookingId);
        if (!booking.isConfirmed()) {
            throw new ValidationException("Chỉ có thể nhận bàn cho đơn đã được admin xác nhận.");
        }
        boolean allowed = tableRepository.findAvailableForType(booking.getTableTypeId()).stream()
                .anyMatch(table -> table.getId() == tableId);
        if (!allowed) {
            throw new ValidationException("Bàn được chọn không còn trống hoặc không đúng loại bàn của booking.");
        }
        bookingRepository.seat(bookingId, tableId);
        tableRepository.updateStatus(tableId, "SERVING");
        int orderId = orderRepository.ensureOrderForBooking(bookingId, tableId);
        orderRepository.importPreorders(bookingId, orderId);
    }

    @Override
    public void markPaid(int bookingId, String paymentMethod) {
        Booking booking = booking(bookingId);
        if (!booking.isSeated()) {
            throw new ValidationException("Chỉ có thể thanh toán khi khách đã nhận bàn.");
        }
        if (!"CASH".equals(paymentMethod) && !"BANK_TRANSFER".equals(paymentMethod)) {
            throw new ValidationException("Phương thức thanh toán không hợp lệ.");
        }
        orderRepository.payByBooking(bookingId, paymentMethod);
        bookingRepository.markPaid(bookingId);
        if (booking.getAssignedTableId() != null) {
            tableRepository.updateStatus(booking.getAssignedTableId(), "DIRTY");
        }
    }

    private void validateEditableBooking(Booking booking) {
        booking.setCustomerName(Validators.required(booking.getCustomerName(), "Tên khách", 100));
        booking.setPhone(Validators.phone(booking.getPhone()));
        Validators.positiveInt(String.valueOf(booking.getTableTypeId()), "Loại bàn", 1_000_000);
        if (booking.getBookingDate() == null || booking.getBookingTime() == null) {
            throw new ValidationException("Ngày giờ đặt bàn không hợp lệ.");
        }
    }
}
