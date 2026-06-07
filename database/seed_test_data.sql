USE lautamgioi_db;

SET FOREIGN_KEY_CHECKS = 0;
TRUNCATE TABLE invoices;
TRUNCATE TABLE order_items;
TRUNCATE TABLE orders;
TRUNCATE TABLE booking_preorders;
TRUNCATE TABLE bookings;
TRUNCATE TABLE restaurant_tables;
TRUNCATE TABLE table_types;
TRUNCATE TABLE dishes;
TRUNCATE TABLE categories;
TRUNCATE TABLE accounts;
SET FOREIGN_KEY_CHECKS = 1;

INSERT INTO accounts(id, username, password, full_name, email, phone, role, status, created_at) VALUES
(1, 'admin', 'admin123', 'System Administrator', 'admin@lautamgioi.com', '0901000001', 'ADMIN', 'ACTIVE', '2026-05-01 09:00:00'),
(2, 'staff01', 'staff123', 'Restaurant Staff', 'staff@lautamgioi.com', '0901000002', 'STAFF', 'ACTIVE', '2026-05-01 09:05:00'),
(3, 'khach01', '123456', 'Nguyễn Minh An', 'an.nguyen@example.com', '0901234567', 'CUSTOMER', 'ACTIVE', '2026-05-02 10:00:00'),
(4, 'khach02', '123456', 'Trần Bảo Ngọc', 'ngoc.tran@example.com', '0912345678', 'CUSTOMER', 'ACTIVE', '2026-05-03 11:00:00'),
(5, 'khach03', '123456', 'Lê Hoàng Nam', 'nam.le@example.com', '0923456789', 'CUSTOMER', 'ACTIVE', '2026-05-04 12:00:00'),
(6, 'locked01', '123456', 'Tài Khoản Khóa', 'locked@example.com', '0934567890', 'CUSTOMER', 'LOCKED', '2026-05-05 13:00:00');

INSERT INTO categories(id, name, description) VALUES
(1, 'Thiên Giới', 'Các set lẩu thanh nhã, nước dùng trong và nguyên liệu cao cấp.'),
(2, 'Nhân Giới', 'Món lẩu cân bằng, dễ dùng cho gia đình và nhóm bạn.'),
(3, 'Ma Giới', 'Hương vị cay nồng, đậm vị, dành cho thực khách thích trải nghiệm mạnh.'),
(4, 'Món ăn kèm', 'Đồ nhúng, khai vị và món phụ.'),
(5, 'Đồ uống', 'Trà, nước ép và thức uống dùng kèm lẩu.');

INSERT INTO dishes(id, category_id, name, image_url, price, description, status) VALUES
(1, 1, 'Lẩu Thiên Sơn Tuyết Liên', 'https://images.unsplash.com/photo-1560963689-b5682b6440f8?auto=format&fit=crop&w=900&q=80', 489000, 'Nước dùng nấm tuyết, gà ác, kỷ tử và rau thanh vị.', 'AVAILABLE'),
(2, 1, 'Lẩu Linh Chi Dược Thiện', 'https://images.unsplash.com/photo-1617093727343-374698b1b08d?auto=format&fit=crop&w=900&q=80', 529000, 'Nước lẩu thảo mộc, nấm linh chi, táo đỏ và thịt gà ta.', 'AVAILABLE'),
(3, 2, 'Lẩu Nhân Gian Sum Họp', 'https://images.unsplash.com/photo-1547592166-23ac45744acd?auto=format&fit=crop&w=900&q=80', 399000, 'Set lẩu bò, hải sản và rau theo mùa cho nhóm 3-4 người.', 'AVAILABLE'),
(4, 2, 'Lẩu Đồng Dao Hải Vị', 'https://images.unsplash.com/photo-1604908176997-125f25cc6f3d?auto=format&fit=crop&w=900&q=80', 459000, 'Tôm, mực, nghêu, cá viên và nước dùng chua ngọt.', 'AVAILABLE'),
(5, 3, 'Lẩu Ma Vực Hỏa Diệm', 'https://images.unsplash.com/photo-1582007100398-e695aef75f99?auto=format&fit=crop&w=900&q=80', 459000, 'Nước lẩu cay tê, sa tế, bò Mỹ và viên thả tổng hợp.', 'AVAILABLE'),
(6, 3, 'Lẩu Huyết Nguyệt Cay Tê', 'https://images.unsplash.com/photo-1625944525533-473f1a3d54e7?auto=format&fit=crop&w=900&q=80', 499000, 'Vị cay nồng, tiêu Tứ Xuyên và topping bò cuộn.', 'OUT_OF_STOCK'),
(7, 4, 'Đĩa Nấm Tam Sinh', 'https://images.unsplash.com/photo-1518977676601-b53f82aba655?auto=format&fit=crop&w=900&q=80', 99000, 'Nấm kim châm, nấm đùi gà, nấm hương và nấm linh chi trắng.', 'AVAILABLE'),
(8, 4, 'Bò Cuộn Linh Khí', 'https://images.unsplash.com/photo-1603048297172-c92544798d5a?auto=format&fit=crop&w=900&q=80', 159000, 'Ba chỉ bò Mỹ thái lát mỏng dùng kèm nước chấm đặc chế.', 'AVAILABLE'),
(9, 4, 'Viên Thả Cửu Châu', 'https://images.unsplash.com/photo-1604909052743-94e838986d24?auto=format&fit=crop&w=900&q=80', 119000, 'Tổng hợp viên tôm, bò, cá và phô mai.', 'AVAILABLE'),
(10, 5, 'Trà Hoa Mộc', 'https://images.unsplash.com/photo-1544787219-7f47ccb76574?auto=format&fit=crop&w=900&q=80', 39000, 'Trà hoa nhẹ vị, dùng lạnh hoặc nóng.', 'AVAILABLE'),
(11, 5, 'Nước Mơ Tiên Sơn', 'https://images.unsplash.com/photo-1622597467836-f3285f2131b8?auto=format&fit=crop&w=900&q=80', 45000, 'Nước mơ chua ngọt, cân vị cay.', 'AVAILABLE');

INSERT INTO table_types(id, capacity, class) VALUES
(1, 2, 'REGULAR'),
(2, 4, 'REGULAR'),
(3, 6, 'VIP'),
(4, 8, 'VIP');

INSERT INTO restaurant_tables(id, table_number, table_type_id, status) VALUES
(1, 'T01', 1, 'EMPTY'),
(2, 'T02', 1, 'RESERVED'),
(3, 'N01', 2, 'EMPTY'),
(4, 'N02', 2, 'SERVING'),
(5, 'V01', 3, 'EMPTY'),
(6, 'V02', 4, 'DIRTY'),
(7, 'V03', 4, 'EMPTY');

INSERT INTO bookings(id, account_id, customer_name, customer_phone, booking_date, booking_time, table_type_id, special_notes, status, created_at) VALUES
(1, 3, 'Nguyễn Minh An', '0901234567', '2026-06-08', '18:30:00', 2, 'Ưu tiên bàn gần cửa sổ.', 'PENDING', '2026-06-07 09:00:00'),
(2, 4, 'Trần Bảo Ngọc', '0912345678', '2026-06-09', '19:00:00', 3, 'Sinh nhật, cần không gian yên tĩnh.', 'CONFIRMED', '2026-06-07 10:30:00'),
(3, 5, 'Lê Hoàng Nam', '0923456789', '2026-06-07', '20:00:00', 2, 'Đã đến nhà hàng.', 'SEATED', '2026-06-06 15:00:00'),
(4, 3, 'Nguyễn Minh An', '0901234567', '2026-06-12', '12:00:00', 1, 'Ăn trưa 2 khách.', 'PENDING', '2026-06-07 11:10:00'),
(5, 4, 'Trần Bảo Ngọc', '0912345678', '2026-06-01', '18:00:00', 4, 'Khách hủy trước giờ đến.', 'CANCELLED', '2026-05-30 14:00:00');

INSERT INTO booking_preorders(booking_id, dish_id, quantity) VALUES
(1, 3, 1),
(1, 7, 2),
(2, 1, 1),
(2, 8, 2),
(3, 5, 1),
(3, 9, 1),
(4, 10, 2);

INSERT INTO orders(id, account_id, table_id, order_type, status, created_at) VALUES
(1, 5, 4, 'DINE_IN', 'PROCESSING', '2026-06-07 20:05:00'),
(2, 4, 2, 'DINE_IN', 'COMPLETED', '2026-06-06 19:10:00'),
(3, 3, NULL, 'TAKE_AWAY', 'COMPLETED', '2026-06-05 12:20:00'),
(4, 3, 5, 'DINE_IN', 'COMPLETED', '2026-05-20 18:45:00'),
(5, 4, NULL, 'TAKE_AWAY', 'CANCELLED', '2026-06-04 13:30:00');

INSERT INTO order_items(id, order_id, dish_id, quantity, actual_price) VALUES
(1, 1, 5, 1, 459000),
(2, 1, 7, 1, 99000),
(3, 1, 10, 2, 39000),
(4, 2, 1, 1, 489000),
(5, 2, 8, 2, 159000),
(6, 2, 11, 2, 45000),
(7, 3, 3, 1, 399000),
(8, 3, 9, 1, 119000),
(9, 4, 2, 1, 529000),
(10, 4, 7, 2, 99000),
(11, 4, 10, 4, 39000),
(12, 5, 4, 1, 459000);

INSERT INTO invoices(id, order_id, total_amount, final_amount, payment_method, payment_status, created_at) VALUES
(1, 2, 897000, 897000, 'CASH', 'PAID', '2026-06-06 21:05:00'),
(2, 3, 518000, 518000, 'BANK_TRANSFER', 'PAID', '2026-06-05 12:55:00'),
(3, 4, 883000, 839000, 'CASH', 'PAID', '2026-05-20 20:30:00'),
(4, 1, 636000, 636000, NULL, 'UNPAID', '2026-06-07 20:20:00');
