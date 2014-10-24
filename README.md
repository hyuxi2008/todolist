A simple todolist API
=====================

A simple API to create/read/update/delete, mark as done/undone and search todo tasks.

You can easily configure it by editing `/src/main/webapp/WEB-INF/todolist.properties`

Once running on your servlet container (tested with Tomcat 8) you can test the API 
by running `/src/main/webapp/WEB-INF/TodolistTest`. 
Setup the endpoint and the key for the test by editing `test.properties`

| Method | Path | Body | Request Headers | Returns
|------|----------|--------|---------|-------
| POST | `/tasks` | `{"title" : "task title", "body" : "task body"}` | `Content-Type : application/json`, `X-Todolist-Key : yourkey` | string (201)
| GET | `/tasks/{taskId}` |  | `X-Todolist-Key : yourkey` | json
| GET | `/tasks/?query={yourquery}&from={from}&limit={limit}` |  | `X-Todolist-Key : yourkey` | json (200)
| PUT | `/tasks/{taskId}` | `{"id" : "taskId","title" : "task title", "body" : "task body"}` | `Content-Type : application/json`, `X-Todolist-Key : yourkey` | json (200)
| PUT | `/tasks/{taskId}/done` | |`X-Todolist-Key : yourkey` | json (200)
| PUT | `/tasks/{taskId}/undone` | |`X-Todolist-Key : yourkey` | json (200)
| DELETE | `/tasks/{taskId}` | |`X-Todolist-Key : yourkey` | void (204)

