A simple todolist API
=====================

A simple API to create/read/update/delete, mark as done/undone and search todo tasks.

You can easily configure it by editing `/src/main/webapp/WEB-INF/todolist.properties`

Once running it on your servlet container (tested with Tomcat 8) you can test the API 
by executing `/src/main/webapp/WEB-INF/TodolistTest`. 
Setup the endpoint and the key for the test by editing `test.properties`

## API Doc

### Create todo
 
POST /tasks

#####Request Headers
* Content-Type : application/json
* X-Todolist-Key : yourkey

#####Body
```javascript
{"title" : "task title", "body" : "task body"}
```

#####Returns
* A string id for the newly created todo
* type : string
* Status : 201
    
#####Example output
    09e3abbd-6a8a-4d40-8ed9-effb4f28ae86
    
    
### Get todo
 
GET /tasks/{taskId}

#####Request Headers
* X-Todolist-Key : yourkey

#####Body
empty

#####Returns
* The todo with id "taskId" 
* type : json
* Status : 200 if the item exists; 404 otherwise
    
#####Example output:

```javascript
{
    "id" : "09e3abbd-6a8a-4d40-8ed9-effb4f28ae86", 
    "title" : "task title", 
    "body" : "task body",
    "user" : "taskowner",
    "done" : false
}
```
    

### Search todos
 
GET /tasks/?query={query}&from={from}&limit={limit}

#####Request Headers
* X-Todolist-Key : yourkey

#####Body
empty

#####Returns
* A list of todos matching the input query. Matches on field "title" are returned first. 
* type : json
* Status : 200
    
#####Example output:

```javascript
{
   "content" : [
       {
            "id" : "09e3abbd-6a8a-4d40-8ed9-effb4f28ae86", 
            "title" : "task title", 
            "body" : "task body",
            "user" : "taskowner",
            "done" : false
        },
        {
            "id" : "fe371116-a9aa-4e6f-a193-975109ba8c75", 
            "title" : "another task title", 
            "body" : "another task body",
            "user" : "taskowner",
            "done" : false
        }
        ...
    ],
    "links" : [
        {
            "prev" : "https://host:port/v1/tasks/?query=query&from=0&limit=10"
        },
        {
            "next" : "https://host:port/v1/tasks/?query=query&from=20&limit=10"
        }
    ]
}
```   
    

### Update todo
 
PUT /tasks/{taskId}

#####Request Headers
* Content-Type : application/json
* X-Todolist-Key : yourkey

#####Body
```javascript
{
    "id" : "taskId" , 
    "title" : "new task title", 
    "body" : "new task body",
    "user" : "taskowner",
    "done" : false
}
```

#####Returns
* The modified task
* type : json
* Status : 200 if the item exists; 404 otherwise
    
#####Example output:

```javascript
{
    "id" : "09e3abbd-6a8a-4d40-8ed9-effb4f28ae86" , 
    "title" : "new task title", 
    "body" : "new task body",
    "user" : "taskowner",
    "done" : false
}
```
    
### Set todo as done
 
PUT /tasks/{taskId}/done

#####Request Headers
* X-Todolist-Key : yourkey

#####Body
empty

#####Returns
* The modified task
* type : json
* Status : 200 if the item exists; 404 otherwise
    
#####Example output:

```javascript
{
    "id" : "09e3abbd-6a8a-4d40-8ed9-effb4f28ae86", 
    "title" : "new task title", 
    "body" : "new task body",
    "user" : "taskowner",
    "done" : true
}
```    

### Set todo as undone
 
PUT /tasks/{taskId}/undone

#####Request Headers
* X-Todolist-Key : yourkey

#####Body
empty

#####Returns
* The modified task
* type : json
* Status : 200 if the item exists; 404 otherwise
    
#####Example output:

```javascript
{
    "id" : "09e3abbd-6a8a-4d40-8ed9-effb4f28ae86", 
    "title" : "new task title", 
    "body" : "new task body",
    "user" : "taskowner",
    "done" : false
}
```      
    

### Delete todo
 
DELETE /tasks/{taskId}

#####Request Headers
* X-Todolist-Key : yourkey

#####Body
empty

#####Returns
* Empty body
* type : void
* Status : 204 if the item exists; 404 otherwise
    




