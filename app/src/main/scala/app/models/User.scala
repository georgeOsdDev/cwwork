package app.models

import scala.collection.mutable.ListBuffer

import org.bson.types.ObjectId
import com.mongodb.casbah.Imports._

import app.AdminUserConfig
import app.service.DB
import app.service.DB.usersColl.{T => UserType}


class User(doc: UserType) extends BaseModel {
  
  lazy val id             = doc.getAs[ObjectId]("_id").map(_.toString)
  lazy val email          = doc.getAsOrElse[String]("email", "")
  lazy val name           = doc.getAsOrElse[String]("name", "Anonymous")
  lazy val password       = doc.getAsOrElse[String]("password", "")
  lazy val token          = doc.getAsOrElse[String]("token", "")
  lazy val tokenExpiredAt = doc.getAsOrElse[Int]("tokenExpiredAt", 0)
//  lazy val lastLogin      = doc.getAsOrElse[Int]("lastLogin", 0)
  lazy val latestPost     = doc.getAs[Post]("latestPost")

  lazy val updatedAt = doc.getAsOrElse[Int]("updatedAt", 0)
  lazy val createdAt = doc.getAsOrElse[Int]("createdAt", 0)
  lazy val deletedAt = doc.getAsOrElse[Int]("deletedAt", 0)
  
  override def toMap: Map[String, Any] = {
    Map(
      "_id"    -> id,
      "email" -> email,
      "name" -> name,
      "latestPost" -> latestPost.map(_.toMap)
    )
  }

  def toMapForPost: Map[String, Any] = {
    Map(
      "_id"    -> id,
      "email" -> email,
      "name" -> name
    )
  }

  def toMapForMe = {
    Map(
      "_id"    -> id,
      "email" -> email,
      "name" -> name,
      "token" -> token,
      "tokenExpiredAt" -> tokenExpiredAt,
      "latestPost" -> latestPost.map(_.toMap)
    )
  }

  def isTokenExpired = tokenExpiredAt < DB.nowSecs
  
  def isDeleted = deletedAt > 0
  
  def refreshToken() = {
    if(id.isDefined) User.refreshToken(id.get)
  }
  
  def setLatestPost(post: Post) = {
    if(id.isDefined) User.setLatestPost(id.get, post)
  }
}

object User {

  private def findOne(key: String, value: AnyRef): Option[User] = {
    val q = MongoDBObject(key -> value)
    DB.usersColl.findOne(q) match {
      case Some(x) => Option(new User(x))
      case None    => None
    }
  }
  
  def findById(id: String): Option[User] = {
    findOne("_id", new ObjectId(id))
  }
  
  def findByEmail(email: String): Option[User] = {
    findOne("email", email)
  }

  def listAll(limit: Option[Int], skip: Option[Int], sort: Option[String]): List[User] = {
    val cursor = DB.usersColl.find()
                             .sort(MongoDBObject(sortableKeyOrID(sort ) -> 1))
                             .skip(skip.getOrElse(0))
                             .limit(limit.getOrElse(100))
    val ret    = new ListBuffer[User]
    while (cursor.hasNext) {
      ret += new User(cursor.next())
    }
    ret.toList
  }
  
  def searchByName(name: String, limit: Option[Int], skip: Option[Int], sort: Option[String]): List[User] = {
    val query  = MongoDBObject("name" -> "name.*".r)
    val cursor = DB.usersColl.find()
                             .sort(MongoDBObject(sortableKeyOrID(sort ) -> "1"))
                             .skip(skip.getOrElse(0))
                             .limit(limit.getOrElse(100))

    val ret    = new ListBuffer[User]
    while (cursor.hasNext) {
      ret += new User(cursor.next())
    }
    ret.toList
  }

  def authByPassword(email: String, password: String): Option[User] = {
    findByEmail(email) match {
      case Some(u) =>
        if (u.password == encrypt(password))
          Some(u)
        else
          None
      case None =>
        None
    }
  }

  def authByToken(email: String, token: String): Option[User] = {
    findByEmail(email) match {
      case Some(u) =>
        if (u.token == token)
          Some(u)
        else
          None
      case None =>
        None
    }
  }

  def create(email: String, password: String, name: String ) = {
    val o = MongoDBObject(
      "email"     -> email,
      "password"  -> encrypt(password),
      "name"      -> name,
      "createdAt" -> DB.nowSecs()
    )
    DB.insertIfNonexistent(DB.usersColl, o)
  }
  
  def refreshToken(id: String): Option[User] = {
    val query = MongoDBObject(
       "_id" -> new ObjectId(id) 
    )
    val upd = $set(
      "token" -> DB.createRandomString,
      "tokenExpiredAt" -> (DB.nowSecs() + 24 * 60 * 60)
    )
    val result = DB.usersColl.update(query, upd)
    if (result.getN == 1)
      findById(id)
    else
      None
  }

  def setLatestPost(id: String, post: Post) = {
    val query = MongoDBObject(
       "_id" -> new ObjectId(id) 
    )
    val upd = $set(
      "latestPost" -> post.toMap,
      "updatedAt" -> DB.nowSecs()
    )
    val result = DB.usersColl.update(query, upd)
    result.getN == 1
  }

  def initializeAdmin(email: String, password: String, name: String):Unit = {
    findByEmail(email) match {
      case Some(u) =>
      case None =>
        create(email, password, name)
    }
  }

  private def encrypt(key:String):String =  {
    DB.encrypt(key)
  }
  
  private def sortableKeyOrID(keyo: Option[String]) = {
    keyo match {
      case Some(key) =>
        if (key == "email" || key == "name")
          key
        else
          "_id"
      case _ =>
        "_id"
    }
  }
}