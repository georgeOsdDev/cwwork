package app.models

import com.mongodb.casbah.Imports._
import app.service.DB


class User(doc: MongoDBObject) extends BaseModel {
  
  override def toJSON = {
    Map(
      "address" -> doc.getOrElse("address", "")
    )
  }
}

object User {
  private val col = DB.usersColl

  private def findById(id: String): Option[User] = {
    None
  }
  
  private def findByAddress(address: String): Option[User] = {
    None
  }
  
  def searchByName(id: String): List[User] = {
    List.empty
  }

  def authByPassword(address: String, password: String): Option[User] = {
    findByAddress(address) match {
      case Some(u) =>
        
        Some(u)
      case None =>
        None
    }
  }
  
  def authByToken(address: String, token: String): Option[User] = {
    findByAddress(address) match {
      case Some(u) =>
        
        Some(u)
      case None =>
        None
    }
  }
}