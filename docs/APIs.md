
# Common


```
http://localhost:8000/api/v1/
```


## requests


JSON format


## response


* Success
```
{
  status: 0,
  result: {

  }
}
```

* Fail
```
{
  status: 0以外,
  message: "API not found"
}


```


## User API

* GET  /api/v1/users

```
{
  status: 0,
  resutl: {
    users: []
  }
}


```


* POST /api/v1/users

* GET /api/v1/users/:uid
* PUT /api/v1/users/:uid
* DELETT /api/v1/users/:uid


## Auth API

* POST /api/v1/auth/login

```
oshidatakeharu [~] curl -i -X POST http://localhost:8000/api/v1/auth/login -H "Content-Type: application/json" -d '{"email":"admin@admin.com", "password":"password"}'                        18:35:52
HTTP/1.1 200 OK
Connection: keep-alive
Content-Type: application/json; charset=UTF-8
Cache-Control: no-store, no-cache, must-revalidate, max-age=0
Content-Length: 113

{"status":0,"result":{"name":"admin","email":"admin@admin.com","token":"tueYEzbXBI","tokenExpiredAt":1432633178}}%
```

* POST /api/v1/auth/singup

```
oshidatakeharu [~] curl -i -X POST http://localhost:8000/api/v1/auth/signup -H "Content-Type: application/json" -d '{"email":"admin2@admin.com", "password":"password2", "name":"admin2" }'   18:39:38
HTTP/1.1 200 OK
Connection: keep-alive
Content-Type: application/json; charset=UTF-8
Cache-Control: no-store, no-cache, must-revalidate, max-age=0
Content-Length: 24

{"status":0,"result":{}}
```



## Threads API

* GET /api/v1/threads
* POST /api/v1/threads

* GET /api/v1/threads/:tid
* PUT /api/v1/threads/:tid
* DELETE /api/v1/threads/:tid

## Thread Search API

* GET /api/v1/threads?keywords=
* GET /api/v1/threads?tags=

## Posts API

* GET /api/v1/threads/:tid/posts
* GET /api/v1/threads/:tid/posts?keywords=

* POST /api/v1/threads/:tid/posts

* GET    /api/v1/threads/:tid/posts/:cid
* PUT    /api/v1/threads/:tid/posts/:cid
* DELETE /api/v1/threads/:tid/posts/:cid




```
[INFO] Normal routes:
POST    /api/:version/auth/login               app.actions.Login
POST    /api/:version/auth/signup              app.actions.SignUp
PUT     /api/:version/auth/token               app.actions.RefreshToken
DELETE  /api/:version/auth/token               app.actions.DeleteToken
GET     /api/:version/threads                  app.actions.ListThreads
POST    /api/:version/threads                  app.actions.CreateThread
GET     /api/:version/threads/:tid             app.actions.ShowThread
PUT     /api/:version/threads/:tid             app.actions.UpdateThread
DELETE  /api/:version/threads/:tid             app.actions.DeleteThread
GET     /api/:version/threads/:tid/posts       app.actions.ListPosts
POST    /api/:version/threads/:tid/posts       app.actions.CreatePost
GET     /api/:version/threads/:tid/posts/:pid  app.actions.ShowPost
PUT     /api/:version/threads/:tid/posts/:pid  app.actions.UpdatePost
DELETE  /api/:version/threads/:tid/posts/:pid  app.actions.DeletePost
GET     /api/:version/users                    app.actions.ListUsers
POST    /api/:version/users                    app.actions.CreateUser
GET     /api/:version/users/:uid               app.actions.ShowUser
PUT     /api/:version/users/:uid               app.actions.UpdateUser
DELETE  /api/:version/users/:uid               app.actions.DeleteUser
[INFO] Error routes:
404  app.actions.NotFoundError
500  app.actions.ServerError
````