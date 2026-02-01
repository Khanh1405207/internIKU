Test Entity Mapping

Step 1: Tạo User
Step 2: Tạo Project với createdBy = userId
Step 3: Tạo Task với projectId
Step 4: Assign Task cho User

Kết quả: Foreign keys project_id, assignee_id, created_by được lưu đúng trong Database → JPA relationship mapping hoạt động.