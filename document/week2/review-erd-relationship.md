1.userEntity-projectEntity(createdBy)
    quan hệ: 1-N
    giải thích:
        - Mỗi userEntity có thể tạo nhiều projectEntity
        - Mỗi projectEntity được tạo bởi đúng 1 userEntity
    triển khai: Khóa ngoại projects.created_by tham chiếu đến users.id
2.projectEntity-taskEntity(projectId)
    quan hệ: 1-N
    giải thích:
        - Mỗi taskEntity thuộc đúng 1 projectEntity
        - Mỗi projectEntity có thể chứa nhiều taskEntity
    triển khai: Khóa ngoại tasks.project_id tham chiếu đến projects.id
3.userEntity-taskEntity(assigneeId)
    quan hệ: 1-N
    giải thích:
        - Mỗi userEntity có thể được phân công nhiều taskEntity
        - Mỗi taskEntity có thể được phân công cho tối đa 1 userEntity
    triển khai: Khóa ngoại tasks.assignee_id tham chiếu đến users.id(cho phép Null)
