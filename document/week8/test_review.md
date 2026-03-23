# Test Review - TaskServiceTest

## Tổng quan
| Mục | Chi tiết |
|-----|----------|
| Class được test | TaskService |
| Framework | JUnit 5 + Mockito |
| Số test case | 23 |
| Kết quả | 23/23 passed |

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
| `assign_userInactive_throwException` | exception | V |

**Nhận xét:** Coverage tốt nhất trong 3 nhóm. Các verify `never()` phản ánh đúng thứ tự fail-fast của logic service — sát với thực tế.

---

### startTask (5 cases)
| Test case | Loại | Kết quả |
|-----------|------|---------|
| `startTask_success_assigneeUser` | happy path | V |
| `startTask_success_manager` | happy path | V |
| `startTask_taskNotFound_throwException` | exception | V |
| `startTask_noAssignee_throwException` | exception | V |
| `startTask_nonAssigneeUser_throwAccessDenied` | authorization | V |

**Nhận xét:** Đã cover đúng rule mới: USER chỉ start task của chính mình, MANAGER có thể thao tác toàn cục.

---

### completeTask (5 cases)
| Test case | Loại | Kết quả |
|-----------|------|---------|
| `completeTask_success_assigneeUser` | happy path | V |
| `completeTask_success_manager` | happy path | V |
| `completeTask_taskNotFound_throwException` | exception | V |
| `completeTask_taskNotInProgress_throwException` | exception | V |
| `completeTask_nonAssigneeUser_throwAccessDenied` | authorization | V |

**Nhận xét:** Bao phủ cả điều kiện trạng thái (`IN_PROGRESS`) và kiểm soát quyền theo assignee/manager.

---

## Điểm mạnh
- Tên test rõ ràng, đúng pattern method_condition_expectedResult.
- Có kết hợp assert kết quả và verify interaction với mock.
- Có kiểm thử authorization ở service layer bằng SecurityContextHolder.
- Refactor helper methods giúp giảm lặp code mà không làm mất ý nghĩa của từng test.

## Điểm cần cải thiện
- assign_success hiện mới assert response khác null; nên assert thêm assignee trong response.
- createTask_success có thể capture TaskEntity được save để assert title, deadline, project.
- Chưa có test cho getAllTasks, getTasksById, getTasksByProject, getTasksByAssignee, getMyTask.
