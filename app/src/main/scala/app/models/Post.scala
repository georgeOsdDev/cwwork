package app.models

import com.mongodb.casbah.Imports._
import app.service.DB


class Post(doc: MongoDBObject) extends BaseModel {

  lazy val pid       = doc.getAs[String]("_id", "")
  lazy val tid       = doc.getAsOrElse[String]("tid", "")
  lazy val body      = doc.getAsOrElse[String]("body", "")
  lazy val createdBy = doc.getAsOrElse[String]("createdBy", "")
  lazy val createdAt = doc.getAsOrElse[Int]("createdAt", 0)
  lazy val updatedAt = doc.getAsOrElse[Int]("updatedAt", 0)
  lazy val deletedAt = doc.getAsOrElse[Int]("deletedAt", 0)

  
  override def toJson = {
    s"""{"pid":${pid}}"""
  }
  
  override def toMap:Map[String, AnyRef] = {
    Map(
      "pid" -> pid,
      "tid" -> tid,
      "body" -> body,
      "createdBy" -> createdBy,
      "createdAt" -> createdAt.toString
    )
  }
}

object Post {
  private val col = DB.postsColl

  private def findById(id: String): Option[Post] = {
    None
  }
    
  def searchByThradId(tid: String, 
                      pageo: Option[Int] = Option(0),
                      offset: Option[Int] = Option(0),
                      max: Option[Int] = Option(100)): List[Post] = {
    List.empty
  }
  
  def searchByKeyWord(keyword: String): List[Post] = {
    List.empty
  }

}