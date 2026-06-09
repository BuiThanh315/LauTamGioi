# Lau Tam Gioi

Java 21 web application using Gradle, Tomcat 10, JSP/JSTL, Servlet, JDBC and MySQL 8.

## Local run in IntelliJ

1. Open this folder as a Gradle project.
2. Run `database/schema.sql` to create the database `lautamgioi_db`.
3. Optional: run `database/seed_test_data.sql` to load test data.
4. Configure a Tomcat 10 run configuration and deploy the `lau-tam-gioi.war` artifact.
5. Set database environment variables when needed:

```text
DB_URL=jdbc:mysql://localhost:3306/lautamgioi_db?useUnicode=true&characterEncoding=utf8&connectionCollation=utf8mb4_unicode_ci&serverTimezone=Asia/Ho_Chi_Minh
DB_USER=root
DB_PASSWORD=123456
```

Test accounts:

```text
admin / admin123
staff01 / staff123
khach01 / 123456
khach02 / 123456
khach03 / 123456
```

## Customer profile

- Customer có thể bấm vào tên của mình trên header để vào trang hồ sơ.
- Trang hồ sơ cho phép cập nhật `email` và `số điện thoại`.
- Sau khi bấm `Cập nhật`, dữ liệu được lưu lại vào bảng `accounts` và session hiện tại cũng được cập nhật lại.

## Local Docker

```bash
docker compose up --build
```

Open `http://localhost:8080/lau-tam-gioi/home`.

If you already started Docker with an older schema:

```bash
docker compose down -v
docker compose up --build
```

## Render deployment

This repo includes a Blueprint file at `render.yaml` for:

1. `lau-tam-gioi-db`: private MySQL service with a persistent disk.
2. `lau-tam-gioi`: public Docker web service running Tomcat on port `10000`.

### Deploy by Blueprint

1. Push this project to GitHub, GitLab, or Bitbucket.
2. Sign in to Render and choose `New > Blueprint`.
3. Connect the repository that contains this project.
4. Render reads `render.yaml` and shows two services:
   - `lau-tam-gioi-db`
   - `lau-tam-gioi`
5. Confirm the plan and create the Blueprint.
6. Wait for the MySQL private service to finish first boot.
7. Wait for the web service to finish building and pass the health check at `/lau-tam-gioi/home`.
8. Open:

```text
https://<your-web-service>.onrender.com/lau-tam-gioi/home
```

### Environment variables used by the web service

The web service reads these variables:

```text
DB_HOST
DB_PORT
DB_NAME
DB_USER
DB_PASSWORD
```

`render.yaml` already wires them from the MySQL private service, so you do not need to type them manually when deploying with Blueprint.

### Database initialization on Render

- `render/mysql/Dockerfile` copies `database/schema.sql` into `/docker-entrypoint-initdb.d/01-schema.sql`.
- That means a fresh MySQL disk initializes the schema automatically on first boot.
- If you redeploy with the same persistent disk, MySQL keeps existing data and will not rerun init scripts.

### Loading demo data

If you want test data on Render after the first deploy:

1. Open the MySQL private service shell or connect from another trusted client inside the same network.
2. Run `database/seed_test_data.sql` manually.

For production-like deployment, keep only `database/schema.sql`.
