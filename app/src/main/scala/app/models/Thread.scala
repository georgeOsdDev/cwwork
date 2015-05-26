package app.models


import scala.collection.mutable.ListBuffer

import com.mongodb.casbah.Imports._
import app.service.DB
import app.service.DB.usersColl.{T => UserType}
import app.service.DB.threadsColl.{T => ThreadType}
import app.service.DB.postsColl.{T => PostType}


class Thread(doc: ThreadType) extends BaseModel {
  
  lazy val id             = doc.getAs[ObjectId]("_id").map(_.toString)
  lazy val title          = doc.getAsOrElse[String]("title", "")
  lazy val tags           = doc.getAsOrElse[List[String]]("tags", List.empty)
  lazy val postsCount     = doc.getAsOrElse[Int]("postsCount", 0)
  lazy val recentPosts    = doc.getAsOrElse[List[PostType]]("recentPosts", List.empty)

  lazy val createdBy = doc.getAs[UserType]("createdBy").map(new User(_))
  lazy val createdAt = doc.getAsOrElse[Int]("createdAt", 0)
  lazy val updatedAt = doc.getAsOrElse[Int]("updatedAt", 0)
  lazy val deletedAt = doc.getAsOrElse[Int]("deletedAt", 0)

  override def toMap = Map(
    "_id" -> id,
    "title" -> title,
    "tags" -> tags,
    "postsCount" -> postsCount,
    "recentPosts" -> recentPosts.map(new Post(_).toMap),
    "createdBy" -> createdBy.map(_.toMap).getOrElse(Map.empty)
  )
  
  def addRecentPost(post: Post) = {
    val nextPosts = post +: recentPosts.map(new Post(_))
    Thread.addRecentPost(id.get, nextPosts.take(5), postsCount+1)
  }
}

object Thread {

  final val MAX_POSTS = 10000
  
  private def findOne(key: String, value: AnyRef): Option[Thread] = {
    val q = MongoDBObject(key -> value)
    DB.threadsColl.findOne(q) match {
      case Some(x) => Option(new Thread(x))
      case None    => None
    }
  }

  def findById(id: String): Option[Thread] = {
    findOne("_id", new ObjectId(id))
  }

  
  private def findByUser(email: String): Option[Thread] = {
    None
  }

  def listAll(tags: Seq[String], q: Option[String], limit: Option[Int], skip: Option[Int], sort: Option[String]): List[Thread] = {
    val builder = MongoDBObject.newBuilder
    if (!tags.isEmpty) builder += "tags" -> MongoDBObject("$all" -> tags) // or use ("$in" -> tags)
    if (q.isDefined)   builder += "title" -> MongoDBObject("$regex" -> ("^"+q.get +".*").r)

    val cursor = DB.threadsColl.find(builder.result)
                             .sort(MongoDBObject(sortableKeyOrID(sort ) -> 1))
                             .skip(skip.getOrElse(0))
                             .limit(limit.getOrElse(100))
    val ret    = new ListBuffer[Thread]
    while (cursor.hasNext) {
      ret += new Thread(cursor.next())
    }
    ret.toList
  }

  def searchByKeyWord(keyword: String, limit: Option[Int], skip: Option[Int], sort: Option[String]): List[Thread] = {
    listAll(Seq.empty, Option(keyword), limit, skip, sort)
  }

  def searchByTag(tags: Seq[String], limit: Option[Int], skip: Option[Int], sort: Option[String]): List[Thread] = {
    listAll(tags, None, limit, skip, sort)
  }

  def create(title:String, tags:Seq[String], createdBy: User) = {
    val o = MongoDBObject(
      "title"     -> title,
      "tags"      -> tags,
      "createdBy" -> createdBy.toMap,
      "createdAt" -> DB.nowSecs(),
      "postsCount" -> 0
    )
    DB.insertIfNonexistent(DB.threadsColl, o)
  }

  def addRecentPost(id:String, posts: List[Post], nextCount: Int) = {
    val query = MongoDBObject(
       "_id" -> new ObjectId(id) 
    )
    val upd = $set(
      "recentPosts" -> posts.map(_.toMap),
      "postsCount" -> nextCount,
      "updatedAt" -> DB.nowSecs()
    )
    val result = DB.threadsColl.update(query, upd)
    result.getN == 1
  }

  private def sortableKeyOrID(keyo: Option[String]) = {
    keyo match {
      case Some(key) =>
        if (key == "title" || key == "createdBy")
          key
        else
          "_id"
      case _ =>
        "_id"
    }
  }

}