package app.models

import com.mongodb.casbah.Imports._
import app.service.DB


class Thread(doc: MongoDBObject) extends BaseModel {
  
  override def toJSON = {
    Map(
      "tid" -> doc.getOrElse("_id", "")
    )
  }
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