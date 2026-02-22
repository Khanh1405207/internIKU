Status flow:
    TODO -> IN_PROGRESS -> DONE

    Các luồng hợp lệ:
        - TODO -> IN_PROGRESS
        - IN_PROGRESS -> DONE
    Các luồng không hợp lệ:
        - TODO -> DONE
        - DONE -> IN_PROGRESS
        - DONE -> TODO

    rule:
        - Task khi tạo có trạng thái TODO
        - Task ở trạng thái DONE:
            - Không được update status
            - Không được assign lại cho user
Tạo Task:
    Input: 
        - title
        - description
        - deadline
        - project
    Validate:
        - project phải tồn tại
        - deadline > current date

Rule Assign Task:
    - User phải tồn tại
    - Task phải tồn tại
    - User phải cùng project với Task
    - Task phải ở trạng thái TODO

