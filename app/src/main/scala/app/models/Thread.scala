package app.models

import com.mongodb.casbah.Imports._
import app.service.DB


class Thread(doc: MongoDBObject) extends BaseModel {
  
  lazy val id             = doc.getAsOrElse[String]("_id", "")

  
  override def toJson = {
    s"""{"tid":"${id}}"""
  }
  
  override def toMap = Map(
    "tid" -> id
  )
}

object Thread {
  private val col = DB.threadsColl

  private def findById(id: String): Option[Thread] = {
    None
  }
  
  private def findByAddress(address: String): Option[Thread] = {
    None
  }
  
  def searchByKeyWord(keyword: String): List[Thread] = {
    List.empty
  }

  def searchByTag(tags: Seq[String]): List[Thread] = {
    List.empty
  }

}