package app.actions

import xitrum.annotation.{GET, POST, PUT, DELETE, Swagger}

@GET("/api/:version/users")
class ListUsers extends BaseAction with APIAction {
  def execute(){
    
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
    
  }
}

@PUT("/api/:version/users/:uid")
class UpdateUser extends BaseAction with APIAction {
  def execute(){
    
  }
}

@DELETE("/api/:version/users/:uid")
class DeleteUser extends BaseAction with APIAction {
  
}