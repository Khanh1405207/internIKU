CREATE DATABASE task_management;
GO
USE task_management;
GO
CREATE TABLE users
(
    id BIGINT PRIMARY KEY IDENTITY(1,1),
    name NVARCHAR(100) NOT NULL,
    email NVARCHAR(150) NOT NULL UNIQUE,
    password NVARCHAR(255) NOT NULL,
    status NVARCHAR(20) NOT NULL,
    created_at DATETIME2 DEFAULT SYSDATETIME()
);
GO
CREATE TABLE projects
(
    id BIGINT PRIMARY KEY IDENTITY(1,1),
    name NVARCHAR(100) NOT NULL,
    description NVARCHAR(MAX),
    created_at DATETIME2 DEFAULT SYSDATETIME(),
    created_by BIGINT NOT NULL,
    FOREIGN KEY (created_by) REFERENCES users(id)
);
GO
CREATE TABLE tasks
(
    id BIGINT PRIMARY KEY IDENTITY(1,1),
    title NVARCHAR(100) NOT NULL,
    description NVARCHAR(MAX),
    task_status NVARCHAR(20) NOT NULL,
    created_at DATETIME2 DEFAULT SYSDATETIME(),
    deadline DATETIME2 NOT NULL,
    project_id BIGINT NOT NULL,
    assignee_id BIGINT,
    FOREIGN KEY (project_id) REFERENCES projects(id),
    FOREIGN KEY (assignee_id) REFERENCES users(id)
);
GO
CREATE TABLE project_user
(
    project_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    PRIMARY KEY (project_id,user_id),
    FOREIGN KEY (project_id) REFERENCES projects(id),
    FOREIGN KEY (user_id) REFERENCES users(id)
);
GO
CREATE TABLE roles
(
    id BIGINT PRIMARY KEY IDENTITY(1,1),
    role_name VARCHAR(50)

);
GO
CREATE TABLE user_role
(
    user_id BIGINT NOT NULL,
    role_id BIGINT NOT NULL,
    PRIMARY KEY (user_id,role_id),
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (role_id) REFERENCES roles(id)
)
-- thêm constraint CHECK cho tasks và users 
GO
ALTER TABLE tasks
ADD CONSTRAINT chk_task_status
CHECK (task_status IN ('TODO','IN_PROGRESS','DONE'));
GO
ALTER TABLE users
ADD CONSTRAINT chk_user_status
CHECK (status IN ('ACTIVE','INACTIVE'));
GO
-- tạo index cho các querry tìm kiếm
CREATE INDEX idx_task_assignee ON tasks(assignee_id);
CREATE INDEX idx_task_project ON tasks(project_id);
CREATE INDEX idx_task_status ON tasks(task_status);
CREATE INDEX idx_project_created_by ON projects(created_by);
GO
-- insert users
INSERT INTO users (name, email, password, status)
VALUES
(N'Khanh', 'khanh@gmail.com', '$2a$10$4g.68/DHXFMaljdJ5bpatOs2PIW11H.6/PAiIG1hz/s7Sh1026HZa', 'ACTIVE'),
(N'An', 'an@gmail.com', '$2a$10$4g.68/DHXFMaljdJ5bpatOs2PIW11H.6/PAiIG1hz/s7Sh1026HZa', 'ACTIVE'),
(N'Binh', 'binh@gmail.com', '$2a$10$4g.68/DHXFMaljdJ5bpatOs2PIW11H.6/PAiIG1hz/s7Sh1026HZa', 'ACTIVE'),
(N'Cuong', 'cuong@gmail.com', '$2a$10$4g.68/DHXFMaljdJ5bpatOs2PIW11H.6/PAiIG1hz/s7Sh1026HZa', 'ACTIVE'),
(N'Dung', 'dung@gmail.com', '$2a$10$4g.68/DHXFMaljdJ5bpatOs2PIW11H.6/PAiIG1hz/s7Sh1026HZa', 'ACTIVE'),
(N'Hoa', 'hoa@gmail.com', '$2a$10$4g.68/DHXFMaljdJ5bpatOs2PIW11H.6/PAiIG1hz/s7Sh1026HZa', 'INACTIVE');
GO
--insert projects
INSERT INTO projects (name, description, created_by)
VALUES
(N'Website bán hàng', N'Dự án ecommerce', 1),
(N'App quản lý công việc', N'Task management system', 1),
(N'Hệ thống HR', N'Quản lý nhân sự', 2),
(N'Chat App', N'Ứng dụng nhắn tin', 2);
GO
-- insert tasks
INSERT INTO tasks (title, description, task_status, deadline, project_id, assignee_id)
VALUES
-- Project 1
(N'Thiết kế DB', N'Tạo bảng dữ liệu', 'DONE', DATEADD(day, 7, SYSDATETIME()), 1, 2),
(N'API sản phẩm', N'Viết API CRUD', 'IN_PROGRESS', DATEADD(day, 10, SYSDATETIME()), 1, 3),
(N'UI trang chủ', N'Thiết kế giao diện', 'TODO', DATEADD(day, 15, SYSDATETIME()), 1, NULL),
(N'Thanh toán', N'Tích hợp payment', 'TODO', DATEADD(day, 20, SYSDATETIME()), 1, 4),

-- Project 2
(N'Tạo entity', N'Viết entity JPA', 'DONE', DATEADD(day, 5, SYSDATETIME()), 2, 2),
(N'Login API', N'Xử lý đăng nhập', 'IN_PROGRESS', DATEADD(day, 6, SYSDATETIME()), 2, 3),
(N'JWT Filter', N'Bảo mật API', 'TODO', DATEADD(day, 8, SYSDATETIME()), 2, 4),
(N'Test Postman', N'Viết test API', 'TODO', DATEADD(day, 9, SYSDATETIME()), 2, NULL),

-- Project 3
(N'Tạo bảng nhân viên', N'Database HR', 'DONE', DATEADD(day, 12, SYSDATETIME()), 3, 5),
(N'Tính lương', N'Logic tính lương', 'IN_PROGRESS', DATEADD(day, 14, SYSDATETIME()), 3, 3),
(N'Import Excel', N'Upload file', 'TODO', DATEADD(day, 16, SYSDATETIME()), 3, NULL),

-- Project 4
(N'Tạo socket server', N'Realtime chat', 'IN_PROGRESS', DATEADD(day, 7, SYSDATETIME()), 4, 2),
(N'Gửi tin nhắn', N'Chat feature', 'TODO', DATEADD(day, 10, SYSDATETIME()), 4, 3),
(N'Lưu lịch sử chat', N'Database message', 'DONE', DATEADD(day, 12, SYSDATETIME()), 4, 4),
(N'Thông báo online', N'Status userEntity', 'TODO', DATEADD(day, 13, SYSDATETIME()), 4, NULL),

-- thêm cho đủ số lượng
(N'Fix bug UI', N'Sửa giao diện', 'IN_PROGRESS', DATEADD(day, 11, SYSDATETIME()), 1, 2),
(N'Tối ưu query', N'Index DB', 'DONE', DATEADD(day, 18, SYSDATETIME()), 2, 3),
(N'Viết tài liệu', N'Documentation', 'TODO', DATEADD(day, 21, SYSDATETIME()), 3, 5),
(N'Test bảo mật', N'Security test', 'IN_PROGRESS', DATEADD(day, 17, SYSDATETIME()), 4, 2),
(N'Deploy server', N'Đưa lên production', 'TODO', DATEADD(day, 25, SYSDATETIME()), 1, NULL);
GO
-- insert roles
INSERT INTO roles(role_name)
VALUES
('USER'),
('MANAGER');
GO
-- insert user_roles
INSERT INTO user_role
VALUES
(1,2),
(2,1),
(3,1),
(4,1),
(5,1),
(6,1);
GO
-- querry kiểm tra lỗi
INSERT INTO tasks (title, description, task_status, deadline, project_id)
VALUES (N'Test sai status', N'Lỗi', 'WAITING', DATEADD(day, 5, SYSDATETIME()), 1);
GO
INSERT INTO users (name, email, password, status)
VALUES (N'Test', 'test@gmail.com', '123', 'BANNED');
GO
INSERT INTO tasks (title, description, task_status, deadline, project_id, assignee_id)
VALUES (N'Test FK', N'Lỗi FK', 'TODO', DATEADD(day, 5, SYSDATETIME()), 999, 1);
GO
-- select tasks theo từng mục
SELECT id,title,[description],task_status,deadline FROM tasks WHERE assignee_id = 5;
SELECT id,title,[description],task_status,deadline FROM tasks WHERE project_id = 2;
SELECT id,title,[description],task_status,deadline FROM tasks WHERE task_status = 'TODO';

SELECT * FROM users
SELECT * FROM projects
SELECT * FROM tasks
SELECT * FROM roles
SELECT * FROM project_user
SELECT * FROM user_role

UPDATE users SET [password]='$2a$10$4g.68/DHXFMaljdJ5bpatOs2PIW11H.6/PAiIG1hz/s7Sh1026HZa' WHERE id=9