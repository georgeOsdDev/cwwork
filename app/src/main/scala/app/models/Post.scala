package app.models

import com.mongodb.casbah.Imports._
import app.service.DB


class Post(doc: MongoDBObject) extends BaseModel {
  
  override def toJSON = {
    Map(
      "pid" -> doc.getOrElse("_id", "")
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