# Checklist Demo - Task Management System

## Phần Chuẩn Bị

- [ ] Khởi động application (Spring Boot dev profile)
- [ ] Mở Swagger UI: http://localhost:8080/swagger-ui.html
- [ ] Mở Postman hoặc sử dụng Swagger để test API
- [ ] Kiểm tra database đã khởi tạo role USER và MANAGER
- [ ] Chuẩn bị 2 trình duyệt hoặc 2 cửa sổ Postman (1 cho MANAGER, 1 cho USER)

## Phần 1: Registration & Authentication

### 1.1 Register Tài Khoản USER
- [ ] Gọi `POST /api/auth/register`
- [ ] Request body:
  ```json
  {
    "name": "John User",
    "email": "user@example.com",
    "password": "password123"
  }
  ```
- [ ] Verify: Response 201, user được tạo với role USER
- [ ] Save email `user@example.com` cho bước 3.5

### 1.2 Register Tài Khoản MANAGER
- [ ] Đăng ký tài khoản MANAGER (có quyền tạo project)
- [ ] Gọi `POST /api/auth/register`
- [ ] Request body:
  ```json
  {
    "name": "Admin Manager",
    "email": "manager@example.com",
    "password": "password123"
  }
  ```
- [ ] Verify: Response 201
- [ ] Manual update role trong DB: `UPDATE user_roles SET role_id = 2 WHERE user_id = (SELECT id FROM users WHERE email = 'manager@example.com')`

### 1.3 Login MANAGER & Nhận JWT
- [ ] Gọi `POST /api/auth/login` từ browser/Postman thứ nhất (MANAGER)
- [ ] Request body:
  ```json
  {
    "email": "manager@example.com",
    "password": "password123"
  }
  ```
- [ ] Verify: Response 200, cookie `accessToken` được set (HttpOnly)
- [ ] Note: Lưu email `manager@example.com` cho các bước tiếp theo

## Phần 2: MANAGER Tạo Project & Task

### 2.1 Tạo Project
- [ ] Gọi `POST /api/projects` với cookie từ 1.3
- [ ] Request body:
  ```json
  {
    "name": "Demo Project",
    "description": "Project for demo",
    "createdBy": 2
  }
  ```
- [ ] Verify: Response 201, project được tạo
- [ ] Save `projectId` từ response

### 2.2 Thêm USER Vào Project (addMember)
- [ ] Gọi `POST /api/projects/{projectId}/members/{userId}` với cookie MANAGER
- [ ] Tìm user ID của `user@example.com` từ `GET /api/users` hoặc note từ register response
- [ ] Verify: Response 200, USER được thêm vào project

### 2.3 Tạo Task Trong Project
- [ ] Gọi `POST /api/tasks` với cookie MANAGER
- [ ] Request body:
  ```json
  {
    "title": "Sample Task",
    "description": "This is a demo task",
    "projectId": {projectId},
    "deadline": "2026-03-30T10:00:00Z"
  }
  ```
- [ ] Verify: Response 201, task được tạo ở trạng thái TODO
- [ ] Save `taskId` từ response

### 2.4 Assign Task Cho USER
- [ ] Gọi `PATCH /api/tasks/{taskId}/assign/{userId}` với cookie MANAGER
- [ ] Verify: Response 200, task được assign cho USER
- [ ] Task status vẫn là TODO

### 2.5 Start Task (MANAGER)
- [ ] Gọi `POST /api/tasks/{taskId}/start` với cookie MANAGER
- [ ] Verify: Response 200, task status chuyển thành IN_PROGRESS
- [ ] Nhấn mạnh: MANAGER có quyền start/complete bất kỳ task

## Phần 3: USER Thực Hiện Task

### 3.1 Login USER
- [ ] Gọi `POST /api/auth/login` từ browser/Postman thứ hai (USER)
- [ ] Request body:
  ```json
  {
    "email": "user@example.com",
    "password": "password123"
  }
  ```
- [ ] Verify: Response 200, cookie `accessToken` được set cho USER

### 3.2 Xem Task Của Chính Mình
- [ ] Gọi `GET /api/tasks/me` với cookie USER
- [ ] Verify: Response 200, danh sách chứa task vừa được assign
- [ ] Nhấn mạnh: USER chỉ thấy task của chính mình

### 3.3 USER Try Start Task (Expected to Fail)
- [ ] Gọi `POST /api/tasks/{taskId}/start` với cookie USER
- [ ] Verify: Response 403 (Forbidden) - USER không được start task mà không assigned
- [ ] Nhấn mạnh rule authorization: USER chỉ được start/complete task được assign cho chính mình

### 3.4 Complete Task (USER)
- [ ] Gọi `POST /api/tasks/{taskId}/complete` với cookie USER
- [ ] Verify: Response 200, task status chuyển thành DONE
- [ ] Nhấn mạnh: USER đã hoàn thành task

## Phần 4: Validation & Error Handling

### 4.1 Test Validation Error (Bad Deadline)
- [ ] Gọi `POST /api/tasks` với cookie MANAGER, deadline ở quá khứ
- [ ] Verify: Response 400 `Deadline must be in the future`

### 4.2 Test Authorization Error
- [ ] Gọi `POST /api/projects` với cookie USER (không phải MANAGER)
- [ ] Verify: Response 403 (Access denied)

### 4.3 Test Not Found Error
- [ ] Gọi `GET /api/tasks/99999` 
- [ ] Verify: Response 404 `Task not found`

## Phần 5: Swagger Documentation

- [ ] Mở http://localhost:8080/swagger-ui.html
- [ ] Rà qua danh sách API: Auth, Users, Projects, Tasks
- [ ] Nhấn mạnh: Tất cả endpoint có mô tả rõ ràng, response example, HTTP status codes

## Phần 6: Summary & Q&A

- [ ] Tổng kết các tính năng đã demo:
  - [ ] Authentication via JWT cookie (HttpOnly)
  - [ ] Authorization: MANAGER vs USER roles
  - [ ] Task lifecycle: TODO → IN_PROGRESS → DONE
  - [ ] Project membership rule
  - [ ] Validation & error handling
  - [ ] Stateless session
- [ ] Mở Q&A: giải thích JPA relationship, JWT flow, business rules
- [ ] Nhận feedback từ mentor

## Notes Kỹ Thuật

- Token JWT được set qua cookie `accessToken` (HttpOnly, 6 phút timeout)
- MANAGER ID = 2, USER ID >= 3 (sau khi register)
- Task status flow: TODO → IN_PROGRESS → DONE (không thể quay lại)
- User INACTIVE không thể được assign task hoặc thêm vào project
- Swagger tự động generate từ @Operation annotations
