# Task Management API

## 1. Giới thiệu

Đây là backend REST API cho bài toán quản lý công việc, người dùng và dự án.

Project sử dụng các công nghệ chính:

- Spring Boot 3.3.5
- Spring Web
- Spring Data JPA
- Spring Security + JWT
- SQL Server
- Swagger OpenAPI
- Maven Wrapper

## 2. Chức năng chính

- Đăng ký, đăng nhập và xác thực bằng JWT Cookie
- Quản lý user
- Quản lý project
- Quản lý task
- Phân quyền theo role `USER` và `MANAGER`
- Swagger UI để test API

## 3. Yêu cầu môi trường

- Java 17
- SQL Server đang chạy
- Maven Wrapper của project (`mvnw`, `mvnw.cmd`)

## 4. Chuẩn bị database

Project đang dùng database SQL Server tên `task_management`.

Chạy file SQL sau để tạo database, bảng, constraint, index và dữ liệu mẫu:

```sql
document/taskManagerDB.sql
```

File này sẽ:

- Tạo database `task_management`
- Tạo các bảng `users`, `projects`, `tasks`, `roles`, `user_role`, `project_user`
- Tạo dữ liệu mẫu
- Tạo sẵn role `USER` và `MANAGER`

## 5. Cấu hình hiện tại

Project đang dùng profile Spring:

- `dev`: cấu hình trong `src/main/resources/application-dev.properties`
- `prod`: cấu hình trong `src/main/resources/application-prod.properties`

Profile mặc định hiện tại là:

```properties
spring.profiles.active=dev
```

Ngoài ra project có đọc thêm file `.env` thông qua:

```properties
spring.config.import=optional:file:.env[.properties]
```

Điều này có nghĩa là:

- Chạy bình thường không truyền profile thì app chạy bằng `dev`
- Muốn chạy `prod` thì phải truyền profile `prod`
- Biến trong `.env` có thể được nạp vào application

## 6. Hướng dẫn chạy project

### Trường hợp 1: Run dev

Phù hợp khi chạy local để code và test nhanh.

#### Bước 1: Tạo database

Mở SQL Server và chạy file:

```sql
document/taskManagerDB.sql
```

#### Bước 2: Kiểm tra cấu hình dev

File `src/main/resources/application-dev.properties` đang dùng cấu hình local:

```properties
spring.datasource.url=jdbc:sqlserver://localhost:1433;databaseName=task_management;encrypt=true;trustServerCertificate=true;
spring.datasource.username=sa
spring.datasource.password=Khanh207!
jwt.secret-key=khanhdaykhanhdaykhanhdaykhanhday
```

Nếu máy của bạn không dùng SQL Server tại `localhost:1433` hoặc không dùng user `sa`, hãy sửa lại file này trước khi chạy.

#### Bước 3: Chạy ứng dụng

Windows:

```powershell
.\mvnw.cmd spring-boot:run
```

macOS/Linux:

```bash
./mvnw spring-boot:run
```

Vì profile mặc định là `dev`, bạn không cần truyền thêm tham số profile.

#### Bước 4: Truy cập ứng dụng

- App: `http://localhost:8080`
- Swagger UI: `http://localhost:8080/swagger-ui/index.html`
- OpenAPI JSON: `http://localhost:8080/v3/api-docs`

### Trường hợp 2: Run prod

Phù hợp khi muốn chạy theo cấu hình production.

#### Bước 1: Tạo file `.env`

Bạn có thể tạo từ file mẫu `.env.example`.

Windows PowerShell:

```powershell
Copy-Item .env.example .env
```

macOS/Linux:

```bash
cp .env.example .env
```

Nội dung tối thiểu của `.env`:

```properties
DB_USERNAME=sa
DB_PASSWORD=your_password
JWT_SECRET=your_secret_key
```

#### Bước 2: Kiểm tra cấu hình prod

File `src/main/resources/application-prod.properties` hiện đang đọc:

```properties
spring.datasource.username=${DB_USERNAME}
spring.datasource.password=${DB_PASSWORD}
jwt.secret-key=${JWT_SECRET}
```

Lưu ý:

- `prod` mới chỉ đưa `username`, `password`, `JWT secret` ra biến môi trường
- `spring.datasource.url` vẫn đang cố định là `localhost:1433`
- Nếu môi trường production dùng host, port hoặc database khác, bạn phải sửa lại `src/main/resources/application-prod.properties`

#### Bước 3: Build project

Windows:

```powershell
.\mvnw.cmd clean package -DskipTests
```

macOS/Linux:

```bash
./mvnw clean package -DskipTests
```

Sau khi build xong, file jar sẽ nằm tại:

```text
target/taskManagement-0.0.1-SNAPSHOT.jar
```

#### Bước 4: Chạy ứng dụng với profile prod

```bash
java -jar target/taskManagement-0.0.1-SNAPSHOT.jar --spring.profiles.active=prod
```

Hoặc chạy bằng Maven:

Windows:

```powershell
.\mvnw.cmd spring-boot:run -Dspring-boot.run.profiles=prod
```

macOS/Linux:

```bash
./mvnw spring-boot:run -Dspring-boot.run.profiles=prod
```

#### Bước 5: Truy cập ứng dụng

- App: `http://localhost:8080`
- Swagger UI: `http://localhost:8080/swagger-ui/index.html`
- OpenAPI JSON: `http://localhost:8080/v3/api-docs`

## 7. Tài liệu API

- Swagger UI: `http://localhost:8080/swagger-ui/index.html`
- OpenAPI JSON: `http://localhost:8080/v3/api-docs`
- Postman collection: `document/TaskManegement.postman_collection.json`

## 8. Lưu ý

- File `application-dev.properties` đang chứa thông tin local để chạy dev nhanh, không nên dùng cấu hình này cho production.
- File `.env` đã được ignore bởi Git, nên không commit file này lên repository.
- Nếu database chưa có đủ role `USER` và `MANAGER`, chức năng đăng ký hoặc tạo user có thể lỗi.
- Các API project và task phần lớn yêu cầu quyền `MANAGER`, còn `/api/auth/**` và Swagger được phép truy cập công khai.
