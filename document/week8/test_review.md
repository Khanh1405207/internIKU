# Test Review - TaskServiceTest

## Tổng quan
| Mục | Chi tiết |
|-----|----------|
| Class được test | TaskService |
| Framework | JUnit 5 + Mockito |
| Số test case | 13 |
| Kết quả | 13/13 passed |

---

## Mocked dependencies
| Mock | Mục đích |
|------|----------|
| `TaskRepository` | tìm kiếm, lưu task |
| `ProjectRepository` | tìm project, kiểm tra membership |
| `UserRepository` | tìm user |

---

## Chi tiết từng nhóm

### createTask (3 cases)
| Test case | Loại | Kết quả |
|-----------|------|---------|
| `createTask_success` | happy path | V |
| `createTask_projectNotFound_throwException` | exception | V |
| `createTask_deadlineNotFuture_throwException` | exception | V |

**Nhận xét:** Đủ coverage. Các exception case đều verify `taskRepository.save` không được gọi — đúng.

---

### updateTask (3 cases)
| Test case | Loại | Kết quả |
|-----------|------|---------|
| `updateTask_success` | happy path | V |
| `updateTask_taskNotFound_throwException` | exception | V |
| `updateTask_deadlineNotFuture_throwException` | exception | V |

**Nhận xét:** Happy path có assert `response.getTitle()` kiểm tra dữ liệu thực sự được cập nhật — tốt.

---

### assign (7 cases)
| Test case | Loại | Kết quả |
|-----------|------|---------|
| `assign_success` | happy path | V |
| `assign_taskNotFound_throwException` | exception | V |
| `assign_userNotFound_throwException` | exception | V |
| `assign_taskNotTodo_throwException` | exception | V |
| `assign_taskIsOverdue_throwException` | exception | V |
| `assign_userNotInProject_throwException` | exception | V |

**Nhận xét:** Coverage tốt nhất trong 3 nhóm. Các verify `never()` phản ánh đúng thứ tự fail-fast của logic service — sát với thực tế.

---

## Điểm mạnh
- Tên test rõ ràng, đúng pattern method_condition_expectedResult.
- Có kết hợp assert kết quả và verify interaction với mock.
- Refactor helper methods giúp giảm lặp code mà không làm mất ý nghĩa của từng test.

## Điểm cần cải thiện
- assign_success hiện mới assert response khác null; nên assert thêm assignee hoặc trạng thái object sau khi assign.
- createTask_success có thể capture TaskEntity được save để assert title, deadline, project.
- Chưa có test cho getAllTasks, getTasksById, getTasksByProject, getTasksByAssignee, getMyTask, startTask, completeTask.
