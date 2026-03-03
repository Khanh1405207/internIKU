User:
    - name: notblank, size 3-50
    - email: notblank, email format, unique
    - password: notblank, size >6
    - status: notnull (ACTIVE/INACTIVE)
Project:
    - name: notblank, size 3-100
    - createdBy: notnull (userId must exist)
Task:
    - title: notblank, size 3-100
    - description: optional, max 500
    - task_status: notnull (TODO/IN_PROGRESS/DONE)
    - deadline: notnull, must be > curent date
    - projectId: notnull, project must exist
    - assigneeId: optional, but must belong to project if assign