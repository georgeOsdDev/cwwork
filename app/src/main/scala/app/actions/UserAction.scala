package app.actions

import xitrum.annotation.{GET, POST, PUT, DELETE, Swagger}

import app.constants.ErrorCds
import app.models.User

@GET("/api/:version/users")
class ListUsers extends BaseAction with APIAction {
  def execute(){
    
    val limit = paramo[Int]("limit").getOrElse(100)
    val skip  = paramo[Int]("offset").getOrElse(0)
    val sort  = paramo[String]("skip").getOrElse("_id")
    
    respondSuccess(Map("users" -> User.listAll(limit, skip, sort).map(_.toMap)))
  }
}

@POST("/api/:version/users")
class CreateUser extends BaseAction with APIAction {
  def execute(){
    
  }
}

@GET("/api/:version/users/:uid")
class ShowUser extends BaseAction with APIAction {
  def execute(){
    val uid = param[String]("uid")

    val usero = 
      if (uid == "me")
        Option(at("currentUser")) // See NeedsToken#BeforeFilter
      else
        User.findById(uid)
    
    usero match {
      case Some(u) =>
        if (reqJSON.get("email").get == u.email)
          respondSuccess(Map("user" -> u.toMapForMe))
        else
          respondSuccess(Map("user" -> u.toMap))
      case None =>
        respondClientError(ErrorCds.DATA_NOT_FOUND, "User not found")
    }
  }
}

@PUT("/api/:version/users/:uid")
class UpdateUser extends BaseAction with APIAction {
  def execute(){
    
  }
}

@DELETE("/api/:version/users/:uid")
class DeleteUser extends BaseAction with APIAction {
  def execute(){
    
  }
}