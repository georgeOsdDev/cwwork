# API Spec

## Routes

```
POST    /api/:version/auth/login               app.actions.Login
POST    /api/:version/auth/signup              app.actions.SignUp
PUT     /api/:version/auth/token               app.actions.RefreshToken(未実装)
DELETE  /api/:version/auth/token               app.actions.DeleteToken(未実装)
GET     /api/:version/threads                  app.actions.ListThreads
POST    /api/:version/threads                  app.actions.CreateThread
GET     /api/:version/threads/:tid             app.actions.ShowThread
PUT     /api/:version/threads/:tid             app.actions.UpdateThread(未実装)
DELETE  /api/:version/threads/:tid             app.actions.DeleteThread(未実装)
GET     /api/:version/threads/:tid/posts       app.actions.ListPosts
POST    /api/:version/threads/:tid/posts       app.actions.CreatePost
GET     /api/:version/threads/:tid/posts/:pid  app.actions.ShowPost
PUT     /api/:version/threads/:tid/posts/:pid  app.actions.UpdatePost(未実装)
DELETE  /api/:version/threads/:tid/posts/:pid  app.actions.DeletePost(未実装)
GET     /api/:version/users                    app.actions.ListUsers
POST    /api/:version/users                    app.actions.CreateUser(未実装)
GET     /api/:version/users/:uid               app.actions.ShowUser
PUT     /api/:version/users/:uid               app.actions.UpdateUser(未実装)
DELETE  /api/:version/users/:uid               app.actions.DeleteUser(未実装)
[INFO] Error routes:
404  app.actions.NotFoundError
500  app.actions.ServerError
```


## Common


### Root url

```
http://localhost:8000/api/:version
```

`:version`にはAPIバージョンを指定する。
現在有効なバージョンは`v1`のみ

Name              |Required |Type         |Desc
------------------|---------|-------------|--------------------
version           |YES      |String (path)|`v1`,`v2`など


## Requests format

#### 共通パラメーター

ログインAPI(/api/:version/auth/login)とサインアップAPI(/api/:version/auth/signup)以外は
以下のヘッダーパラメータを必要とする

Name              |Required |Type  |Desc
------------------|---------|------|--------------------
X-APP-TOKEN       |YES      |String|loginAPIで取得できるトークン
X-APP-EMAIL       |YES      |String|ログイン用のEメールアドレス

トークンおよびEメールアドレスはリクエストボディ(JSONフォーマット)に含めてることも可能
その場合のキーは`token`と`email`。
ただしHTTPヘッダとボディの両方に値が存在する場合はヘッダの値を優先する。

ログインAPI(/api/:version/auth/login)とサインアップAPI(/api/:version/auth/signup)は認証なしで利用することができる。

## Response format

* Content-Type:

  `application/json; charset=UTF-8`

* 正常系レスポンス(statusが0の場合)
```
  {
    status: 0,
    result: {
    }
}
```

* 異常系レスポンス(statusが0以外の場合)
```
  {
    status: -1,
    message: "API not found"
  }
```

* エラーコード一覧
``` ErrorCds.scala
object ErrorCds {
  val SUCCESS       = 0

  // Client errors
  val UNKOWN_API     = 101
  val INVALID_PARAM  = 102
  val MISSING_PARAM  = 103
  val DATA_NOT_FOUND = 104
  val DATA_IS_ALREADY_EXIST = 105
  val UNSUPPORTED_API_VERSION = 106
  val NOT_AUTHENTICATED = 107
  val TOKEN_EXPIRED = 108

  // Server errors
  val IN_MAINTENANCE = 201
  val SYSTEM_ERROR   = 202
  val POSTS_COUNT_REACHED_MAX = 203

  val UNEXPECTED_ERROR = 999
}
```


## Auth API

#### POST /api/:version/auth/login
ログインAPI(トークン払い出しAPI)


#### Requestパラメータ

Name              |Required |Type         |Desc
------------------|---------|-------------|------------
email             |YES      |String (body)|`admin@admin.com`
password          |YES      |String (body)|`password`

#### Responseパラメータ

Name              |Type   |Desc
------------------|-------|------------
id                |String | システム上のユニークID
name              |String | 登録名
email             |String | メールアドレス（リクエストと同じ）
token             |String | APIトークン
tokenExpiredAt    |Int    | APIトークン期限終了時間(Unix秒)


```
oshidatakeharu [~] curl -i -X POST http://localhost:8000/api/v1/auth/login -H "Content-Type: application/json" -d '{"email":"admin@admin.com", "password":"password"}'
HTTP/1.1 200 OK
Connection: keep-alive
Content-Type: application/json; charset=UTF-8
Cache-Control: no-store, no-cache, must-revalidate, max-age=0
Content-Length: 146

{"status":0,"result":{"name":"admin","email":"admin@admin.com","_id":"5563fd873004d9bc7c8df451","token":"Ic2zAai1TP","tokenExpiredAt":1432709153}}
```

#### POST /api/:version/auth/singup
サインアップ(ユーザー作成API)

#### Requestパラメータ

Name              |Required |Type         |Desc
------------------|---------|-------------|------------
email             |YES      |String (body)|`admin@admin.com`
password          |YES      |String (body)|`password`

#### Responseパラメータ

無し

```
oshidatakeharu [~] curl -i -X POST http://localhost:8000/api/v1/auth/signup -H "Content-Type: application/json" -d '{"email":"xxx@admin.com", "password":"password", "name": "new comer"}'
HTTP/1.1 200 OK
Connection: keep-alive
Content-Type: application/json; charset=UTF-8
Cache-Control: no-store, no-cache, must-revalidate, max-age=0
Content-Length: 24

{"status":0,"result":{}}
```


## User API

#### GET /api/:version/users
user一覧取得API


#### Requestパラメータ

Name              |Required |Type        |Value        |
------------------|---------|------------|-------------|
skip              |NO       |Int (Query) |Default 0    |
offset            |NO       |Int (Query) |Default 0    |
limit             |NO       |Int (Query) |Default 100  |


#### Responseパラメータ

Name              |Type   |Desc
------------------|-------|------------
users             |List   |Userの配列

##### user詳細

Name              |Type   |Desc
------------------|-------|------------
id                |String | システム上のユニークID
name              |String | 登録名
email             |String | メールアドレス

```
curl -i -X GET http://localhost:8000/api/v1/users -H "Content-Type: application/json" -H "X-APP-TOKEN:8mCwBc73V2" -H "X-APP-EMAIL:admin2@admin.com"                        15:48:06
HTTP/1.1 200 OK
Connection: keep-alive
Content-Type: application/json; charset=UTF-8
Content-Length: 262
ETag: "fAeCD0WTPxhAGR4OQyEgOA"

{"status":0,"result":{"users":[{"_id":"5563fd873004d9bc7c8df451","email":"admin@admin.com","name":"admin"},{"_id":"5563ff133004fc3c9a99882d","email":"admin2@admin.com","name":"aaa"},{"_id":"556417263004b6982a6b3777","email":"xxx@admin.com","name":"new comer"}]}}
```


#### GET /api/:version/users/:uid
user一覧取得API


#### Requestパラメータ

Name              |Required |Type           |Value        |
------------------|---------|---------------|-------------|
uid               |YES       |String (path) |Meの場合はログインユーザ|


#### Responseパラメータ

Name              |Type   |Desc
------------------|-------|------------
user             　|Object   |User

##### user詳細

Name              |Type   |Desc
------------------|-------|------------
id                |String | システム上のユニークID
name              |String | 登録名
email             |String | メールアドレス

```
oshidatakeharu [~] curl -i -X GET http://localhost:8000/api/v1/users/556417263004b6982a6b3777 -H "Content-Type: application/json" -H "X-APP-TOKEN:8mCwBc73V2" -H "X-APP-EMAIL:admin2@admin.com"
HTTP/1.1 200 OK
Connection: keep-alive
Content-Type: application/json; charset=UTF-8
Content-Length: 108
ETag: "eN5XJ76Uq3K_FWcs4jmLNg"

{"status":0,"result":{"user":{"_id":"556417263004b6982a6b3777","email":"xxx@admin.com","name":"new comer"}}}
```


## Thread API

#### GET /api/:version/threads
thread一覧取得API


#### Requestパラメータ

Name              |Required |Type           |Value        |
------------------|---------|---------------|-------------|
keyword           |NO       |String (Query) |検索キーワード threads.titleを前方一致検索|
tag               |NO       |String (Query) |csvで指定 eg. tag1,tag2    |
limit             |NO       |Int (Query)    |Default 0    |
skip              |NO       |Int (Query)    |Default 0    |
sort              |NO       |String (Query) |Default _id  |


#### Responseパラメータ

Name              |Type   |Desc
------------------|-------|------------
threads           |List   |Threadの配列

##### threads詳細

Name              |Type   |Desc
------------------|-------|------------
_id               |String | システム上のユニークID
title             |String | スレッドタイトル
tags              |List[String] | タグの配列
postCount         |Int | 投稿数
createdBy         |Object | ユーザー情報(UserAPIと同様の詳細)
recentPosts       |List[Object]| 直近の投稿の配列最大5件まで(PostAPIと同様の詳細)

```
curl -i -X GET http://localhost:8000/api/v1/threads -H "Content-Type: application/json" -H "X-APP-TOKEN:8mCwBc73V2" -H "X-APP-EMAIL:admin2@admin.com"
HTTP/1.1 200 OK
Connection: keep-alive
Content-Type: application/json; charset=UTF-8
Content-Length: 1028
ETag: "DSrPRnomuaRUGZLUAAJEbw"

{"status":0,"result":{"threads":[{"_id":"556400b5300442e49f47a009","tags":["food","book","sport"],"postsCount":0,"recentPosts":[],"createdBy":{"email":"admin2@admin.com","name":"aaa"},"title":"myThread"},{"_id":"556403bd3004e8f3e9077190","tags":["soccer","sport","baseball"],"postsCount":4,"recentPosts":[{"body":"rgagragagagag","createdAt":"0","createdBy":{"email":"admin2@admin.com","name":"aaa"},"tid":"556403bd3004e8f3e9077190"},{"body":"rgagragagagag","createdAt":"0","createdBy":{"email":"admin2@admin.com","name":"aaa"},"tid":"556403bd3004e8f3e9077190"},{"body":"rgagragagagag","createdAt":"0","createdBy":{"email":"admin2@admin.com","name":"aaa"},"tid":"556403bd3004e8f3e9077190"},{"body":"rgagragagagag","createdAt":"0","createdBy":{"email":"admin2@admin.com","name":"aaa"},"tid":"556403bd3004e8f3e9077190"},{"body":"rgagragagagag","createdAt":"0","createdBy":{"email":"admin2@admin.com","name":"aaa"},"tid":"556403bd3004e8f3e9077190"}],"createdBy":{"email":"admin2@admin.com","name":"aaa"},"title":"footballThread"}]}}
```


#### GET /api/:version/threads/:tid
thread詳細取得API


#### Requestパラメータ

Name              |Required |Type           |Value        |
------------------|---------|---------------|-------------|
limit             |NO       |Int (Query)    |Default 0    |
skip              |NO       |Int (Query)    |Default 0    |
sort              |NO       |String (Query) |Default _id  |


#### Responseパラメータ

Name              |Type   |Desc
------------------|-------|------------
threads           |List   |Threadの配列

##### threads詳細

Name              |Type   |Desc
------------------|-------|------------
_id               |String | システム上のユニークID
title             |String | スレッドタイトル
tags              |List[String] | タグの配列
postCount         |Int | 投稿数
createdBy         |Object | ユーザー情報(UserAPIと同様の詳細)
recentPosts       |List[Object]| 直近の投稿の配列最大5件まで(PostAPIと同様の詳細)

```
curl -i -X GET http://localhost:8000/api/v1/threads -H "Content-Type: application/json" -H "X-APP-TOKEN:8mCwBc73V2" -H "X-APP-EMAIL:admin2@admin.com"
HTTP/1.1 200 OK
Connection: keep-alive
Content-Type: application/json; charset=UTF-8
Content-Length: 1028
ETag: "DSrPRnomuaRUGZLUAAJEbw"

{"status":0,"result":{"threads":[{"_id":"556400b5300442e49f47a009","tags":["food","book","sport"],"postsCount":0,"recentPosts":[],"createdBy":{"email":"admin2@admin.com","name":"aaa"},"title":"myThread"},{"_id":"556403bd3004e8f3e9077190","tags":["soccer","sport","baseball"],"postsCount":4,"recentPosts":[{"body":"rgagragagagag","createdAt":"0","createdBy":{"email":"admin2@admin.com","name":"aaa"},"tid":"556403bd3004e8f3e9077190"},{"body":"rgagragagagag","createdAt":"0","createdBy":{"email":"admin2@admin.com","name":"aaa"},"tid":"556403bd3004e8f3e9077190"},{"body":"rgagragagagag","createdAt":"0","createdBy":{"email":"admin2@admin.com","name":"aaa"},"tid":"556403bd3004e8f3e9077190"},{"body":"rgagragagagag","createdAt":"0","createdBy":{"email":"admin2@admin.com","name":"aaa"},"tid":"556403bd3004e8f3e9077190"},{"body":"rgagragagagag","createdAt":"0","createdBy":{"email":"admin2@admin.com","name":"aaa"},"tid":"556403bd3004e8f3e9077190"}],"createdBy":{"email":"admin2@admin.com","name":"aaa"},"title":"footballThread"}]}}
```



## Posts API

