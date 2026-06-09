-- MySQL dump 10.13  Distrib 8.0.46, for Win64 (x86_64)
--
-- Host: localhost    Database: lautamgioi_db
-- ------------------------------------------------------
-- Server version	8.0.46

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `accounts`
--

DROP TABLE IF EXISTS `accounts`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `accounts` (
  `id` int NOT NULL AUTO_INCREMENT,
  `username` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `password` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `full_name` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `email` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `phone` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `role` enum('CUSTOMER','STAFF','ADMIN') COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'CUSTOMER',
  `status` enum('ACTIVE','LOCKED') COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'ACTIVE',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `username` (`username`),
  KEY `idx_account_username` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `accounts`
--

LOCK TABLES `accounts` WRITE;
/*!40000 ALTER TABLE `accounts` DISABLE KEYS */;
INSERT INTO `accounts` VALUES (1,'admin','admin123','System Administrator','admin@lautamgioi.com','0901000001','ADMIN','ACTIVE','2026-05-01 02:00:00'),(2,'staff01','staff123','Restaurant Staff','staff@lautamgioi.com','0901000002','STAFF','ACTIVE','2026-05-01 02:05:00'),(3,'khach01','123456','Nguyễn Minh An','an.nguyen@example.com','0901234567','CUSTOMER','ACTIVE','2026-05-02 03:00:00'),(4,'khach02','123456','Trần Bảo Ngọc','ngoc.tran@example.com','0912345678','CUSTOMER','ACTIVE','2026-05-03 04:00:00'),(5,'khach03','123456','Lê Hoàng Nam','nam.le@example.com','0923456789','CUSTOMER','ACTIVE','2026-05-04 05:00:00'),(6,'locked01','123456','Tài Khoản Khóa','locked@example.com','0934567890','CUSTOMER','LOCKED','2026-05-05 06:00:00'),(7,'customer03','123456','Bùi Hoàng Thanh','robluccibn040@gmail.com','0973004219','CUSTOMER','ACTIVE','2026-06-08 01:09:52'),(8,'khach99','123456','Bùi Hoàng Thanh','robluccibn040@gmail.com','0973004219','CUSTOMER','ACTIVE','2026-06-08 10:23:53');
/*!40000 ALTER TABLE `accounts` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `booking_preorders`
--

DROP TABLE IF EXISTS `booking_preorders`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `booking_preorders` (
  `booking_id` int NOT NULL,
  `dish_id` int NOT NULL,
  `quantity` int NOT NULL,
  PRIMARY KEY (`booking_id`,`dish_id`),
  KEY `dish_id` (`dish_id`),
  CONSTRAINT `booking_preorders_ibfk_1` FOREIGN KEY (`booking_id`) REFERENCES `bookings` (`id`) ON DELETE CASCADE,
  CONSTRAINT `booking_preorders_ibfk_2` FOREIGN KEY (`dish_id`) REFERENCES `dishes` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `booking_preorders`
--

LOCK TABLES `booking_preorders` WRITE;
/*!40000 ALTER TABLE `booking_preorders` DISABLE KEYS */;
INSERT INTO `booking_preorders` VALUES (1,3,1),(1,7,2),(2,1,1),(2,8,2),(3,5,1),(3,9,1),(4,10,2);
/*!40000 ALTER TABLE `booking_preorders` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `bookings`
--

DROP TABLE IF EXISTS `bookings`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `bookings` (
  `id` int NOT NULL AUTO_INCREMENT,
  `account_id` int DEFAULT NULL,
  `customer_name` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `customer_phone` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL,
  `booking_date` date NOT NULL,
  `booking_time` time NOT NULL,
  `table_type_id` int NOT NULL,
  `table_id` int DEFAULT NULL,
  `special_notes` text COLLATE utf8mb4_unicode_ci,
  `status` enum('PENDING','CONFIRMED','SEATED','PAID','CANCELLED') COLLATE utf8mb4_unicode_ci DEFAULT 'PENDING',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `account_id` (`account_id`),
  KEY `table_type_id` (`table_type_id`),
  KEY `idx_booking_date` (`booking_date`),
  KEY `fk_bookings_table` (`table_id`),
  CONSTRAINT `bookings_ibfk_1` FOREIGN KEY (`account_id`) REFERENCES `accounts` (`id`),
  CONSTRAINT `bookings_ibfk_2` FOREIGN KEY (`table_type_id`) REFERENCES `table_types` (`id`),
  CONSTRAINT `fk_bookings_table` FOREIGN KEY (`table_id`) REFERENCES `restaurant_tables` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `bookings`
--

LOCK TABLES `bookings` WRITE;
/*!40000 ALTER TABLE `bookings` DISABLE KEYS */;
INSERT INTO `bookings` VALUES (1,3,'Nguyễn Minh An','0901234567','2026-06-08','18:30:00',2,NULL,'Ưu tiên bàn gần cửa sổ.','PENDING','2026-06-07 02:00:00'),(2,4,'Trần Bảo Ngọc','0912345678','2026-06-09','19:00:00',3,NULL,'Sinh nhật, cần không gian yên tĩnh.','CONFIRMED','2026-06-07 03:30:00'),(3,5,'Lê Hoàng Nam','0923456789','2026-06-07','20:00:00',2,NULL,'Đã đến nhà hàng.','PAID','2026-06-06 08:00:00'),(4,3,'Nguyễn Minh An','0901234567','2026-06-12','12:00:00',1,1,'Ăn trưa 2 khách.','SEATED','2026-06-07 04:10:00'),(5,4,'Trần Bảo Ngọc','0912345678','2026-06-01','18:00:00',4,NULL,'Khách hủy trước giờ đến.','CONFIRMED','2026-05-30 07:00:00'),(6,8,'Bùi Hoàng Thanh','0973004219','2026-06-17','10:25:00',1,NULL,'no cmt','PENDING','2026-06-08 10:25:05'),(7,3,'Nguyễn Minh An','0901234567','2026-06-26','14:00:00',2,NULL,NULL,'PENDING','2026-06-09 02:17:35');
/*!40000 ALTER TABLE `bookings` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `categories`
--

DROP TABLE IF EXISTS `categories`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `categories` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `description` text COLLATE utf8mb4_unicode_ci,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `categories`
--

LOCK TABLES `categories` WRITE;
/*!40000 ALTER TABLE `categories` DISABLE KEYS */;
INSERT INTO `categories` VALUES (1,'Thiên Giới','Các set lẩu thanh nhã, nước dùng trong và nguyên liệu cao cấp.'),(2,'Nhân Giới','Món lẩu cân bằng, dễ dùng cho gia đình và nhóm bạn.'),(3,'Ma Giới','Hương vị cay nồng, đậm vị, dành cho thực khách thích trải nghiệm mạnh.'),(4,'Món ăn kèm','Đồ nhúng, khai vị và món phụ.'),(5,'Đồ uống','Trà, nước ép và thức uống dùng kèm lẩu.');
/*!40000 ALTER TABLE `categories` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `dishes`
--

DROP TABLE IF EXISTS `dishes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `dishes` (
  `id` int NOT NULL AUTO_INCREMENT,
  `category_id` int NOT NULL,
  `name` varchar(150) COLLATE utf8mb4_unicode_ci NOT NULL,
  `image_url` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `price` decimal(10,2) NOT NULL,
  `description` text COLLATE utf8mb4_unicode_ci,
  `status` enum('AVAILABLE','OUT_OF_STOCK') COLLATE utf8mb4_unicode_ci DEFAULT 'AVAILABLE',
  PRIMARY KEY (`id`),
  KEY `category_id` (`category_id`),
  KEY `idx_dish_name` (`name`),
  CONSTRAINT `dishes_ibfk_1` FOREIGN KEY (`category_id`) REFERENCES `categories` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `dishes`
--

LOCK TABLES `dishes` WRITE;
/*!40000 ALTER TABLE `dishes` DISABLE KEYS */;
INSERT INTO `dishes` VALUES (1,1,'Lẩu Thiên Sơn Tuyết Liên','https://images.unsplash.com/photo-1560963689-b5682b6440f8?auto=format&fit=crop&w=900&q=80',489000.00,'Nước dùng nấm tuyết, gà ác, kỷ tử và rau thanh vị.','AVAILABLE'),(2,1,'Lẩu Linh Chi Dược Thiện','https://images.unsplash.com/photo-1617093727343-374698b1b08d?auto=format&fit=crop&w=900&q=80',529000.00,'Nước lẩu thảo mộc, nấm linh chi, táo đỏ và thịt gà ta.','AVAILABLE'),(3,2,'Lẩu Nhân Gian Sum Họp','https://images.unsplash.com/photo-1547592166-23ac45744acd?auto=format&fit=crop&w=900&q=80',399000.00,'Set lẩu bò, hải sản và rau theo mùa cho nhóm 3-4 người.','AVAILABLE'),(4,2,'Lẩu Đồng Dao Hải Vị','https://images.unsplash.com/photo-1604908176997-125f25cc6f3d?auto=format&fit=crop&w=900&q=80',459000.00,'Tôm, mực, nghêu, cá viên và nước dùng chua ngọt.','AVAILABLE'),(5,3,'Lẩu Ma Vực Hỏa Diệm','https://images.unsplash.com/photo-1582007100398-e695aef75f99?auto=format&fit=crop&w=900&q=80',459000.00,'Nước lẩu cay tê, sa tế, bò Mỹ và viên thả tổng hợp.','AVAILABLE'),(6,3,'Lẩu Huyết Nguyệt Cay Tê','https://images.unsplash.com/photo-1625944525533-473f1a3d54e7?auto=format&fit=crop&w=900&q=80',499000.00,'Vị cay nồng, tiêu Tứ Xuyên và topping bò cuộn.','OUT_OF_STOCK'),(7,4,'Đĩa Nấm Tam Sinh','https://images.unsplash.com/photo-1518977676601-b53f82aba655?auto=format&fit=crop&w=900&q=80',99000.00,'Nấm kim châm, nấm đùi gà, nấm hương và nấm linh chi trắng.','AVAILABLE'),(8,4,'Bò Cuộn Linh Khí','https://images.unsplash.com/photo-1603048297172-c92544798d5a?auto=format&fit=crop&w=900&q=80',159000.00,'Ba chỉ bò Mỹ thái lát mỏng dùng kèm nước chấm đặc chế.','AVAILABLE'),(9,4,'Viên Thả Cửu Châu','https://images.unsplash.com/photo-1604909052743-94e838986d24?auto=format&fit=crop&w=900&q=80',119000.00,'Tổng hợp viên tôm, bò, cá và phô mai.','AVAILABLE'),(10,5,'Trà Hoa Mộc','https://images.unsplash.com/photo-1544787219-7f47ccb76574?auto=format&fit=crop&w=900&q=80',39000.00,'Trà hoa nhẹ vị, dùng lạnh hoặc nóng.','AVAILABLE'),(11,5,'Nước Mơ Tiên Sơn','https://images.unsplash.com/photo-1622597467836-f3285f2131b8?auto=format&fit=crop&w=900&q=80',45000.00,'Nước mơ chua ngọt, cân vị cay.','AVAILABLE');
/*!40000 ALTER TABLE `dishes` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `invoices`
--

DROP TABLE IF EXISTS `invoices`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `invoices` (
  `id` int NOT NULL AUTO_INCREMENT,
  `order_id` int NOT NULL,
  `total_amount` decimal(10,2) NOT NULL,
  `final_amount` decimal(10,2) NOT NULL,
  `payment_method` enum('CASH','BANK_TRANSFER') COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `payment_status` enum('UNPAID','PAID') COLLATE utf8mb4_unicode_ci DEFAULT 'UNPAID',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `order_id` (`order_id`),
  KEY `idx_invoice_created` (`created_at`),
  CONSTRAINT `invoices_ibfk_1` FOREIGN KEY (`order_id`) REFERENCES `orders` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `invoices`
--

LOCK TABLES `invoices` WRITE;
/*!40000 ALTER TABLE `invoices` DISABLE KEYS */;
INSERT INTO `invoices` VALUES (1,2,897000.00,897000.00,'CASH','PAID','2026-06-06 14:05:00'),(2,3,518000.00,518000.00,'BANK_TRANSFER','PAID','2026-06-05 05:55:00'),(3,4,883000.00,839000.00,'CASH','PAID','2026-05-20 13:30:00'),(4,1,636000.00,636000.00,NULL,'UNPAID','2026-06-07 13:20:00');
/*!40000 ALTER TABLE `invoices` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `order_items`
--

DROP TABLE IF EXISTS `order_items`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `order_items` (
  `id` int NOT NULL AUTO_INCREMENT,
  `order_id` int NOT NULL,
  `dish_id` int NOT NULL,
  `quantity` int NOT NULL,
  `actual_price` decimal(10,2) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `order_id` (`order_id`),
  KEY `dish_id` (`dish_id`),
  CONSTRAINT `order_items_ibfk_1` FOREIGN KEY (`order_id`) REFERENCES `orders` (`id`) ON DELETE CASCADE,
  CONSTRAINT `order_items_ibfk_2` FOREIGN KEY (`dish_id`) REFERENCES `dishes` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `order_items`
--

LOCK TABLES `order_items` WRITE;
/*!40000 ALTER TABLE `order_items` DISABLE KEYS */;
INSERT INTO `order_items` VALUES (1,1,5,1,459000.00),(2,1,7,1,99000.00),(3,1,10,2,39000.00),(4,2,1,1,489000.00),(5,2,8,2,159000.00),(6,2,11,2,45000.00),(7,3,3,1,399000.00),(8,3,9,1,119000.00),(9,4,2,1,529000.00),(10,4,7,2,99000.00),(11,4,10,4,39000.00),(12,5,4,1,459000.00);
/*!40000 ALTER TABLE `order_items` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `orders`
--

DROP TABLE IF EXISTS `orders`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `orders` (
  `id` int NOT NULL AUTO_INCREMENT,
  `account_id` int DEFAULT NULL,
  `table_id` int DEFAULT NULL,
  `order_type` enum('DINE_IN','TAKE_AWAY') COLLATE utf8mb4_unicode_ci NOT NULL,
  `status` enum('PROCESSING','COMPLETED','CANCELLED') COLLATE utf8mb4_unicode_ci DEFAULT 'PROCESSING',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `account_id` (`account_id`),
  KEY `table_id` (`table_id`),
  CONSTRAINT `orders_ibfk_1` FOREIGN KEY (`account_id`) REFERENCES `accounts` (`id`),
  CONSTRAINT `orders_ibfk_2` FOREIGN KEY (`table_id`) REFERENCES `restaurant_tables` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `orders`
--

LOCK TABLES `orders` WRITE;
/*!40000 ALTER TABLE `orders` DISABLE KEYS */;
INSERT INTO `orders` VALUES (1,5,4,'DINE_IN','PROCESSING','2026-06-07 13:05:00'),(2,4,2,'DINE_IN','COMPLETED','2026-06-06 12:10:00'),(3,3,NULL,'TAKE_AWAY','COMPLETED','2026-06-05 05:20:00'),(4,3,5,'DINE_IN','COMPLETED','2026-05-20 11:45:00'),(5,4,NULL,'TAKE_AWAY','CANCELLED','2026-06-04 06:30:00');
/*!40000 ALTER TABLE `orders` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `restaurant_tables`
--

DROP TABLE IF EXISTS `restaurant_tables`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `restaurant_tables` (
  `id` int NOT NULL AUTO_INCREMENT,
  `table_number` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL,
  `table_type_id` int NOT NULL,
  `status` enum('EMPTY','RESERVED','SERVING','DIRTY') COLLATE utf8mb4_unicode_ci DEFAULT 'EMPTY',
  PRIMARY KEY (`id`),
  UNIQUE KEY `table_number` (`table_number`),
  KEY `table_type_id` (`table_type_id`),
  CONSTRAINT `restaurant_tables_ibfk_1` FOREIGN KEY (`table_type_id`) REFERENCES `table_types` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `restaurant_tables`
--

LOCK TABLES `restaurant_tables` WRITE;
/*!40000 ALTER TABLE `restaurant_tables` DISABLE KEYS */;
INSERT INTO `restaurant_tables` VALUES (1,'T01',1,'SERVING'),(2,'T02',1,'RESERVED'),(3,'N01',2,'RESERVED'),(4,'N02',2,'SERVING'),(5,'V01',3,'EMPTY'),(6,'V02',4,'DIRTY'),(7,'V03',4,'EMPTY');
/*!40000 ALTER TABLE `restaurant_tables` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `table_types`
--

DROP TABLE IF EXISTS `table_types`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `table_types` (
  `id` int NOT NULL AUTO_INCREMENT,
  `capacity` int NOT NULL,
  `class` enum('REGULAR','VIP') COLLATE utf8mb4_unicode_ci DEFAULT 'REGULAR',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `table_types`
--

LOCK TABLES `table_types` WRITE;
/*!40000 ALTER TABLE `table_types` DISABLE KEYS */;
INSERT INTO `table_types` VALUES (1,2,'REGULAR'),(2,4,'REGULAR'),(3,6,'VIP'),(4,8,'VIP');
/*!40000 ALTER TABLE `table_types` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-06-09  9:39:01
