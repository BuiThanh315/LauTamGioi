-- LAU TAM GIOI DATABASE FINAL V1
DROP DATABASE IF EXISTS lautamgioi_db;
CREATE DATABASE lautamgioi_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE lautamgioi_db;

CREATE TABLE accounts (
 id INT AUTO_INCREMENT PRIMARY KEY,
 username VARCHAR(50) NOT NULL UNIQUE,
 password VARCHAR(255) NOT NULL,
 full_name VARCHAR(100) NOT NULL,
 email VARCHAR(100),
 phone VARCHAR(20),
 role ENUM('CUSTOMER','STAFF','ADMIN') NOT NULL DEFAULT 'CUSTOMER',
 status ENUM('ACTIVE','LOCKED') NOT NULL DEFAULT 'ACTIVE',
 created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE categories (
 id INT AUTO_INCREMENT PRIMARY KEY,
 name VARCHAR(100) NOT NULL,
 description TEXT
);

CREATE TABLE dishes (
 id INT AUTO_INCREMENT PRIMARY KEY,
 category_id INT NOT NULL,
 name VARCHAR(150) NOT NULL,
 image_url VARCHAR(255),
 price DECIMAL(10,2) NOT NULL,
 description TEXT,
 status ENUM('AVAILABLE','OUT_OF_STOCK') DEFAULT 'AVAILABLE',
 FOREIGN KEY(category_id) REFERENCES categories(id)
);

CREATE TABLE table_types (
 id INT AUTO_INCREMENT PRIMARY KEY,
 capacity INT NOT NULL,
 class ENUM('REGULAR','VIP') DEFAULT 'REGULAR'
);

CREATE TABLE restaurant_tables (
 id INT AUTO_INCREMENT PRIMARY KEY,
 table_number VARCHAR(20) UNIQUE NOT NULL,
 table_type_id INT NOT NULL,
 status ENUM('EMPTY','RESERVED','SERVING','DIRTY') DEFAULT 'EMPTY',
 FOREIGN KEY(table_type_id) REFERENCES table_types(id)
);

CREATE TABLE bookings (
 id INT AUTO_INCREMENT PRIMARY KEY,
 account_id INT,
 customer_name VARCHAR(100) NOT NULL,
 customer_phone VARCHAR(20) NOT NULL,
 booking_date DATE NOT NULL,
 booking_time TIME NOT NULL,
 table_type_id INT NOT NULL,
 special_notes TEXT,
 status ENUM('PENDING','CONFIRMED','SEATED','CANCELLED') DEFAULT 'PENDING',
 created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
 FOREIGN KEY(account_id) REFERENCES accounts(id),
 FOREIGN KEY(table_type_id) REFERENCES table_types(id)
);

CREATE TABLE booking_preorders (
 booking_id INT,
 dish_id INT,
 quantity INT NOT NULL,
 PRIMARY KEY(booking_id,dish_id),
 FOREIGN KEY(booking_id) REFERENCES bookings(id) ON DELETE CASCADE,
 FOREIGN KEY(dish_id) REFERENCES dishes(id)
);

CREATE TABLE orders (
 id INT AUTO_INCREMENT PRIMARY KEY,
 account_id INT,
 table_id INT,
 order_type ENUM('DINE_IN','TAKE_AWAY') NOT NULL,
 status ENUM('PROCESSING','COMPLETED','CANCELLED') DEFAULT 'PROCESSING',
 created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
 FOREIGN KEY(account_id) REFERENCES accounts(id),
 FOREIGN KEY(table_id) REFERENCES restaurant_tables(id)
);

CREATE TABLE order_items (
 id INT AUTO_INCREMENT PRIMARY KEY,
 order_id INT NOT NULL,
 dish_id INT NOT NULL,
 quantity INT NOT NULL,
 actual_price DECIMAL(10,2) NOT NULL,
 FOREIGN KEY(order_id) REFERENCES orders(id) ON DELETE CASCADE,
 FOREIGN KEY(dish_id) REFERENCES dishes(id)
);

CREATE TABLE invoices (
 id INT AUTO_INCREMENT PRIMARY KEY,
 order_id INT UNIQUE NOT NULL,
 total_amount DECIMAL(10,2) NOT NULL,
 final_amount DECIMAL(10,2) NOT NULL,
 payment_method ENUM('CASH','BANK_TRANSFER'),
 payment_status ENUM('UNPAID','PAID') DEFAULT 'UNPAID',
 created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
 FOREIGN KEY(order_id) REFERENCES orders(id)
);

CREATE INDEX idx_account_username ON accounts(username);
CREATE INDEX idx_dish_name ON dishes(name);
CREATE INDEX idx_booking_date ON bookings(booking_date);
CREATE INDEX idx_invoice_created ON invoices(created_at);

INSERT INTO accounts(username,password,full_name,email,role)
VALUES ('admin','admin123','System Administrator','admin@lautamgioi.com','ADMIN');

INSERT INTO accounts(username,password,full_name,email,role)
VALUES ('staff01','staff123','Restaurant Staff','staff@lautamgioi.com','STAFF');

INSERT INTO categories(name, description) VALUES
('Thiên Giới', 'Các set lẩu thanh nhã, nước dùng trong và nguyên liệu cao cấp.'),
('Nhân Giới', 'Món lẩu cân bằng, dễ dùng cho gia đình và nhóm bạn.'),
('Ma Giới', 'Hương vị cay nồng, đậm vị, dành cho thực khách thích trải nghiệm mạnh.'),
('Món ăn kèm', 'Đồ nhúng, khai vị và món phụ.');

INSERT INTO dishes(category_id, name, image_url, price, description, status) VALUES
(1, 'Lẩu Thiên Sơn Tuyết Liên', 'https://images.unsplash.com/photo-1560963689-b5682b6440f8?auto=format&fit=crop&w=900&q=80', 489000, 'Nước dùng nấm tuyết, gà ác, kỷ tử và rau thanh vị.', 'AVAILABLE'),
(2, 'Lẩu Nhân Gian Sum Họp', 'https://images.unsplash.com/photo-1547592166-23ac45744acd?auto=format&fit=crop&w=900&q=80', 399000, 'Set lẩu bò, hải sản và rau theo mùa cho nhóm 3-4 người.', 'AVAILABLE'),
(3, 'Lẩu Ma Vực Hỏa Diệm', 'https://images.unsplash.com/photo-1582007100398-e695aef75f99?auto=format&fit=crop&w=900&q=80', 459000, 'Nước lẩu cay tê, sa tế, bò Mỹ và viên thả tổng hợp.', 'AVAILABLE'),
(4, 'Đĩa Nấm Tam Sinh', 'https://images.unsplash.com/photo-1518977676601-b53f82aba655?auto=format&fit=crop&w=900&q=80', 99000, 'Nấm kim châm, nấm đùi gà, nấm hương và nấm linh chi trắng.', 'AVAILABLE'),
(4, 'Bò Cuộn Linh Khí', 'https://images.unsplash.com/photo-1603048297172-c92544798d5a?auto=format&fit=crop&w=900&q=80', 159000, 'Ba chỉ bò Mỹ thái lát mỏng dùng kèm nước chấm đặc chế.', 'AVAILABLE');

INSERT INTO table_types(capacity, class) VALUES
(2, 'REGULAR'),
(4, 'REGULAR'),
(6, 'VIP'),
(8, 'VIP');

INSERT INTO restaurant_tables(table_number, table_type_id, status) VALUES
('T01', 1, 'EMPTY'),
('T02', 1, 'EMPTY'),
('N01', 2, 'EMPTY'),
('N02', 2, 'EMPTY'),
('V01', 3, 'EMPTY'),
('V02', 4, 'EMPTY');
