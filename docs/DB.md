# DB

  * cwwork

# Collections

  * User
  * Threads
  * Posts

## User

  * _id: ObjectId
  * email: String (pattern=email)
  * name: String (min=1 max=10)
  * password: String (min=1 max=100)
  * token: String
  * tokenExpiredAt: Int (unixTime)
  * lastLogin: Int (unixTime)
  * latestPost : Post
  * updatedAt: Int (unixTime)
  * createdAt: Int (unixTime)
  * deletedAt: Int (unixTime)


## Threads

  * _id: ObjectId
  * title: String (min=1 max=40)
  * createdBy: User
  * createdAt: Int (unixTime)
  * tags: String[]
  * recentPosts: Post[]
  * postsCount: Int


## Posts

  * _id: ObjectId
  * tid: ObjectId
  * body: String (min=1, max=140)
  * createdBy: User
  * createdAt: Int (unixTime)


