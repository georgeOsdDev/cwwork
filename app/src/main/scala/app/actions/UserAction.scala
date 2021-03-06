package app.actions

import xitrum.annotation.{GET, POST, PUT, DELETE, Swagger}

import app.constants.ErrorCds
import app.models.User

@GET("/api/:version/users")
class ListUsers extends BaseAction with APIAction {
  def execute(){
    
    val limit = paramo[Int]("limit")
    val skip  = paramo[Int]("skip")
    val sort  = paramo[String]("sort")
    
    respondSuccess(Map("users" -> User.listAll(limit, skip, sort).map(_.toMap)))
  }
}

@POST("/api/:version/users")
class CreateUser extends BaseAction with APIAction {
  def execute(){
    // @FIXME use signupAction instead
    redirectTo[SignUp]()
  }
}

@GET("/api/:version/users/:uid")
class ShowUser extends BaseAction with APIAction {
  def execute(){
    val uid = param[String]("uid")
    val currentUser = at("currentUser").asInstanceOf[User] // See NeedsToken#BeforeFilter

    val usero = 
      if (uid == "me")
        Option(currentUser)
      else
        User.findById(uid)

    usero match {
      case Some(u) =>
        if (currentUser.email == u.email)
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
    respondText("Not implemented")
  }
}

@DELETE("/api/:version/users/:uid")
class DeleteUser extends BaseAction with APIAction {
  def execute(){
    respondText("Not implemented")
  }
}