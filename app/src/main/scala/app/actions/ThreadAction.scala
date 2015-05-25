package app.actions

import xitrum.annotation.{GET, POST, PUT, DELETE, Swagger}

@GET("/api/:version/threads")
class ListThreads extends BaseAction with APIAction {
  def execute(){
    
    // searchByTag
    
    // searchByKeyword
    
  }
}

@POST("/api/:version/threads")
class CreateThread extends BaseAction with APIAction {
  def execute(){
    
  }
}

@GET("/api/:version/threads/:tid")
class ShowThread extends BaseAction with APIAction {
  def execute(){
    
  }
}

@PUT("/api/:version/threads/:tid")
class UpdateThread extends BaseAction with APIAction {
  def execute(){
    
  }
}

@DELETE("/api/:version/threads/:tid")
class DeleteThread extends BaseAction with APIAction {
    def execute(){
    
  }

}