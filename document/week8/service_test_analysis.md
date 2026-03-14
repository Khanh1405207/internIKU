## AuthService
### 1.register()

**Mô tả**
Tạo 1 user mới

**dependencies**
- UserRepository
- RoleRepository
- BCryptPasswordEncoder

**Logic**
1. kiểm tra email đã tồn tại, throw nếu đã có
2. mã hóa password với BCryptPasswordEncoder
3. tạo UserEntity với name, email và password
4. tìm role USER, throw nếu không tồn tại
5. gán role cho user
6. lưu user vào database
7. trả về RegisterResponse

**Test cases**
- register_success
- register_emailExists_throwException
- register_roleNotFound_throwException

### 2.login()

**Mô tả**
Xác thực thông tin đăng nhập và trả về JWT access token

**dependencies**
- AuthenticationManager
- JwtUtil

**Logic**
1. xác thực email và password qua AuthenticationManager
2. lấy UserDetails từ principal của Authentication
3. tạo JWT access token từ UserDetails
4. trả về LoginResponse chứa access token

**Test cases**
- login_success
- login_badCredentials_throwException

---

## ProjectService
### 1.getAllProjects()

**Mô tả**
Lấy danh sách tất cả project

**dependencies**
- ProjectRepository

**Logic**
1. lấy tất cả project từ database
2. map sang danh sách ProjectResponse
3. trả về danh sách

**Test cases**
- getAllProjects_success

### 2.getProjectById()

**Mô tả**
Lấy thông tin một project theo id

**dependencies**
- ProjectRepository

**Logic**
1. tìm project theo id, throw nếu không tồn tại
2. trả về ProjectResponse

**Test cases**
- getProjectById_success
- getProjectById_projectNotFound_throwException

### 3.createProject()

**Mô tả**
Tạo một project mới

**dependencies**
- ProjectRepository
- UserRepository

**Logic**
1. tìm user theo createdBy, throw nếu không tồn tại
2. tạo ProjectEntity với name, description và user
3. lưu project vào database
4. trả về ProjectResponse

**Test cases**
- createProject_success
- createProject_userNotFound_throwException

### 4.updateProject()

**Mô tả**
Cập nhật thông tin project

**dependencies**
- ProjectRepository

**Logic**
1. tìm project theo id, throw nếu không tồn tại
2. cập nhật name và description
3. trả về ProjectResponse

**Test cases**
- updateProject_success
- updateProject_notFound_throwException

### 5.addMember()

**Mô tả**
Thêm một user vào project

**dependencies**
- ProjectRepository
- UserRepository

**Logic**
1. tìm project theo id, throw nếu không tồn tại
2. tìm user theo id, throw nếu không tồn tại
3. kiểm tra user đã có trong project chưa, throw nếu đã tồn tại
4. thêm user vào project
5. trả về ProjectResponse

**Test cases**
- addMember_success
- addMember_projectNotFound_throwException
- addMember_userNotFound_throwException
- addMember_userAlreadyInProject_throwException

---

## TaskService
### 1.getAllTasks()

**Mô tả**
Lấy danh sách tất cả task

**dependencies**
- TaskRepository

**Logic**
1. lấy tất cả task (kèm project và assignee) từ database
2. map sang danh sách TaskResponse
3. trả về danh sách

**Test cases**
- getAllTasks_success

### 2.getTasksById()

**Mô tả**
Lấy thông tin một task theo id

**dependencies**
- TaskRepository

**Logic**
1. tìm task theo id, throw nếu không tồn tại
2. trả về TaskResponse

**Test cases**
- getTasksById_success
- getTasksById_taskNotFound_throwException

### 3.getTasksByProject()

**Mô tả**
Lấy danh sách task thuộc một project

**dependencies**
- TaskRepository

**Logic**
1. tìm tất cả task theo projectId
2. map sang danh sách TaskResponse
3. trả về danh sách

**Test cases**
- getTasksByProject_success

### 4.getTasksByAssignee()

**Mô tả**
Lấy danh sách task được giao cho một user

**dependencies**
- TaskRepository

**Logic**
1. tìm tất cả task theo assigneeId
2. map sang danh sách TaskResponse
3. trả về danh sách

**Test cases**
- getTasksByAssignee_success

### 5.getMyTask()

**Mô tả**
Lấy danh sách task của user đang đăng nhập theo email

**dependencies**
- TaskRepository

**Logic**
1. tìm tất cả task theo email của assignee
2. map sang danh sách TaskResponse
3. trả về danh sách

**Test cases**
- getMyTask_success

### 6.createTask()

**Mô tả**
Tạo một task mới trong project

**dependencies**
- TaskRepository
- ProjectRepository

**Logic**
1. tìm project theo projectId, throw nếu không tồn tại
2. kiểm tra deadline phải ở tương lai, throw nếu không hợp lệ
3. tạo TaskEntity với title, description, deadline và project
4. lưu task vào database
5. trả về TaskResponse

**Test cases**
- createTask_success
- createTask_projectNotFound_throwException
- createTask_deadlineNotFuture_throwException

### 7.updateTask()

**Mô tả**
Cập nhật thông tin task

**dependencies**
- TaskRepository

**Logic**
1. tìm task theo id, throw nếu không tồn tại
2. kiểm tra deadline không được ở quá khứ, throw nếu không hợp lệ
3. cập nhật title, description và deadline
4. trả về TaskResponse

**Test cases**
- updateTask_success
- updateTask_taskNotFound_throwException
- updateTask_deadlineNotFuture_throwException

### 8.assign()

**Mô tả**
Giao task cho một user

**dependencies**
- TaskRepository
- UserRepository
- ProjectRepository

**Logic**
1. tìm task theo taskId, throw nếu không tồn tại
2. tìm user theo assigneeId, throw nếu không tồn tại
3. kiểm tra task status phải là TODO, throw nếu không hợp lệ
4. kiểm tra task không bị quá hạn, throw nếu đã hết hạn
5. kiểm tra user có thuộc project của task không, throw nếu không thuộc
6. gán user cho task
7. trả về TaskResponse

**Test cases**
- assign_success
- assign_taskNotFound_throwException
- assign_userNotFound_throwException
- assign_taskNotTodo_throwException
- assign_taskIsOverdue_throwException
- assign_userNotInProject_throwException

### 9.startTask()

**Mô tả**
Bắt đầu thực hiện task

**dependencies**
- TaskRepository

**Logic**
1. tìm task theo taskId, throw nếu không tồn tại
2. kiểm tra task đã có assignee chưa, throw nếu chưa có
3. gọi task.start() để chuyển trạng thái
4. trả về TaskResponse

**Test cases**
- startTask_success
- startTask_notFound_throwException
- startTask_noAssignee_throwException

### 10.completeTask()

**Mô tả**
Hoàn thành task

**dependencies**
- TaskRepository

**Logic**
1. tìm task theo taskId, throw nếu không tồn tại
2. kiểm tra task status phải là IN_PROGRESS, throw nếu không hợp lệ
3. gọi task.complete() để chuyển trạng thái
4. trả về TaskResponse

**Test cases**
- completeTask_success
- completeTask_notFound_throwException
- completeTask_notInProgress_throwException

---

## UserService
### 1.getAllUsers()

**Mô tả**
Lấy danh sách tất cả user đang hoạt động

**dependencies**
- UserRepository

**Logic**
1. lấy tất cả user có status ACTIVE
2. map sang danh sách UserResponse
3. trả về danh sách

**Test cases**
- getAllUsers_success

### 2.getUserById()

**Mô tả**
Lấy thông tin một user theo id

**dependencies**
- UserRepository

**Logic**
1. tìm user theo id, throw nếu không tồn tại
2. trả về UserResponse

**Test cases**
- getUserById_success
- getUserById_userNotFound_throwException

### 3.createUser()

**Mô tả**
Tạo một user mới (admin tạo thủ công)

**dependencies**
- UserRepository
- RoleRepository
- BCryptPasswordEncoder

**Logic**
1. kiểm tra email đã tồn tại, throw nếu đã có
2. mã hóa password với BCryptPasswordEncoder
3. tạo UserEntity với name, email và password
4. tìm role USER, throw nếu không tồn tại
5. gán role cho user
6. lưu user vào database
7. trả về UserResponse

**Test cases**
- createUser_success
- createUser_emailExists_throwException
- createUser_roleNotFound_throwException

### 4.updateUser()

**Mô tả**
Cập nhật thông tin user

**dependencies**
- UserRepository

**Logic**
1. tìm user theo id, throw nếu không tồn tại
2. kiểm tra email không bị trùng với user khác, throw nếu đã tồn tại
3. cập nhật name và email
4. trả về UserResponse

**Test cases**
- updateUser_success
- updateUser_userNotFound_throwException
- updateUser_emailAlreadyExists_throwException

### 5.upgradeToManager() -- tạm thời không sử dụng(chỉ dành cho ADMIN)

**Mô tả**
Nâng cấp quyền của user lên MANAGER

**dependencies**
- UserRepository
- RoleRepository

**Logic**
1. tìm user theo id, throw nếu không tồn tại
2. tìm role MANAGER, throw nếu không tồn tại
3. thêm role MANAGER cho user
4. lưu user vào database
5. trả về UserResponse

**Test cases**
- upgradeToManager_success
- upgradeToManager_userNotFound_throwException
- upgradeToManager_roleNotFound_throwException

### 6.disableUser()

**Mô tả**
Vô hiệu hóa tài khoản user

**dependencies**
- UserRepository

**Logic**
1. tìm user theo id, throw nếu không tồn tại
2. gọi userEntity.deactivate() để chuyển trạng thái
3. lưu user vào database
4. trả về UserResponse

**Test cases**
- disableUser_success
- disableUser_userNotFound_throwException
