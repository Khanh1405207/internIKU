Thành phần:
    User : Người dùng
    Project : Dự án(nhóm công việc)
    Task : Công việc cụ thể
    Role : Các vai trò 
    UserRole : Vai trò của User
    1 User có thể tham gia nhiều Project
    1 User có thể có nhiều vai trò
    1 Task thuộc 1 Project
    1 Task có thể được thực hiện bởi 1 User 
    Task có 3 trạng thái: TODO, IN_PROGRESS, DONE
    Role có 2: USER,MANAGER
Entity + field:
    User :
        - id: mã userEntity
        - name: tên userEntity
        - emai: unique
        - password: mật khẩu đăng nhập
        - createdAt: thời gian tạo
        - status: trạng thái userEntity
    Project:
        - id: mã projectEntity
        - name: tên projectEntity
        - description: mô tả projectEntity
        - createdAt: thời gian tạo
        - createBy: mã của MANAGER tạo Project (FK)
    Task:
        - id: mã Task
        - title: tên Task
        - description: mô tả Task
        - taskStatus: trạng thái hiện tại của Task
        - createdAt: thời gian tạo Task
        - deadline: hạn hoàn thành của Task
        - projectId: mã của projectEntity chứa Task (FK)
        - assigneeId: mã của User nhận công việc (FK)
    Role:
        - id: mã Role
        - name: tên Role
    UserRole:
        - userId: mã User (FK)
        - roleId: mã Role của User (FK)
        - (PK) (userId,roleId)
Enum:
    taskStatus: TODO, IN_PROGRESS, DONE
    roleName: USER, MANAGER
Mô tả nghiệp vụ:
    - 1 User có thể tham gia nhiều Project
    - 1 User có thể có nhiều vai trò
    - Chỉ MANAGER mới được tạo projectEntity
    - 1 Project có nhiều Task
    - 1 Task thuộc 1 Project
    - 1 Task có thể được gán cho 1 User
    - USER chỉ xem được Task của mình
    - Không được chuyển từ DONE -> IN_PROGRESS
    - Không được asign Task cho User không thuộc Project đó
    - MANAGER xem được toàn bộ Task trong Project mình quản lý
    - Deadline phải lớn hơn thời điểm tạo Task