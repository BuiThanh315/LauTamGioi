# Lau Tam Gioi

Java 21 Gradle web application for Tomcat 10, JSP/JSTL, Servlet, JDBC and MySQL 8.

## Run locally in IntelliJ

1. Open this folder as a Gradle project.
2. Create MySQL database by running `database/schema.sql`. The database name is `lautamgioi_db`.
   Optional test data can be loaded with `database/seed_test_data.sql`.
3. Configure a Tomcat 10 run configuration and deploy the Gradle artifact `lau-tam-gioi:war`.
4. Set environment variables if needed:

```text
DB_URL=jdbc:mysql://localhost:3306/lautamgioi_db?useUnicode=true&characterEncoding=utf8&connectionCollation=utf8mb4_unicode_ci&serverTimezone=Asia/Ho_Chi_Minh
DB_USER=root
DB_PASSWORD=123456
```

Default seeded accounts:

```text
admin / admin123
staff01 / staff123
khach01 / 123456
khach02 / 123456
khach03 / 123456
```

## Docker

```bash
docker compose up --build
```

Open `http://localhost:8080/lau-tam-gioi/`.

If you previously started Docker with an older schema, reset the MySQL volume before starting again:

```bash
docker compose down -v
docker compose up --build
```
