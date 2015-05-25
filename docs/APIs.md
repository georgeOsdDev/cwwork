
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
* POST /api/v1/auth/logout
* POST /api/v1/auth/expand

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
