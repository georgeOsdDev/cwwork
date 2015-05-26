package app.models

import scala.collection.mutable.ListBuffer

import com.mongodb.casbah.Imports._
import app.service.DB

import app.service.DB.usersColl.{T => UserType}
import app.service.DB.postsColl.{T => PostType}


class Post(doc: MongoDBObject) extends BaseModel {

  lazy val pid       = doc.getAs[ObjectId]("_id").map(_.toString)
  lazy val tid       = doc.getAsOrElse[String]("tid", "")
  lazy val body      = doc.getAsOrElse[String]("body", "")

  lazy val createdBy = doc.getAs[UserType]("createdBy").map(new User(_))
  lazy val createdAt = doc.getAsOrElse[Int]("createdAt", 0)
  lazy val updatedAt = doc.getAsOrElse[Int]("updatedAt", 0)
  lazy val deletedAt = doc.getAsOrElse[Int]("deletedAt", 0)

  override def toMap:Map[String, AnyRef] = {
    Map(
      "_id" -> pid,
      "tid" -> tid,
      "body" -> body,
      "createdBy" -> createdBy.map(_.toMapForPost).getOrElse(Map.empty),
      "createdAt" -> createdAt.toString
    )
  }
}

object Post {

  private def findOne(key: String, value: AnyRef): Option[Post] = {
    val q = MongoDBObject(key -> value)
    DB.postsColl.findOne(q) match {
      case Some(x) => Option(new Post(x))
      case None    => None
    }
  }

  def findById(id: String): Option[Post] = {
    findOne("_id", new ObjectId(id))
  }

  def searchByThreadId(tid: String, limit: Option[Int], skip: Option[Int]): List[Post] = {
    val cursor = DB.postsColl.find(MongoDBObject("tid" -> tid))
                             .skip(skip.getOrElse(0))
                             .limit(limit.getOrElse(100))
    val ret    = new ListBuffer[Post]
    while (cursor.hasNext) {
      ret += new Post(cursor.next())
    }
    ret.toList
  }

  def create(body: String, tid: String, createdBy: User): Option[ObjectId] = {
    val o = MongoDBObject(
      "body"     -> body,
      "tid"      -> tid,
      "createdBy" -> createdBy.toMap,
      "createdAt" -> DB.nowSecs()
    )
    val result = DB.postsColl.insert(o)
    
    //@TODO @FIXME Install MongoDB 3.0 and use result.getUpsertedId
    if (result.getLastError.ok)
      o.getAs[ObjectId]("_id")
    else
      None
  }
}