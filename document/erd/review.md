1.user-project(createdBy)
    quan hệ: 1-N
    giải thích:
        - Mỗi user có thể tạo nhiều project
        - Mỗi project được tạo bởi đúng 1 user
    triển khai: Khóa ngoại projects.created_by tham chiếu đến users.id
2.project-task(projectId)
    quan hệ: 1-N
    giải thích:
        - Mỗi task thuộc đúng 1 project
        - Mỗi project có thể chứa nhiều task
    triển khai: Khóa ngoại tasks.project_id tham chiếu đến projects.id
3.user-task(assigneeId)
    quan hệ: 1-N
    giải thích:
        - Mỗi user có thể được phân công nhiều task
        - Mỗi task có thể được phân công cho tối đa 1 user
    triển khai: Khóa ngoại tasks.assignee_id tham chiếu đến users.id(cho phép Null)
