package app.actions

import xitrum.annotation.{GET, POST, PUT, DELETE, Swagger}

@GET("/api/:version/threads/:tid/posts")
class ListPosts extends BaseAction with APIAction {
  def execute(){
    
    // Page
    // Offset
    // Max
    
  }
}

@POST("/api/:version/threads/:tid/posts")
class CreatePost extends BaseAction with APIAction {
  def execute(){
    
  }
}

@GET("/api/:version/threads/:tid/posts/:pid")
class ShowPost extends BaseAction with APIAction {
  def execute(){
    
  }
}

@PUT("/api/:version/threads/:tid/posts/:pid")
class UpdatePost extends BaseAction with APIAction {
  def execute(){
    
  }
}

@DELETE("/api/:version/threads/:tid/posts/:pid")
class DeletePost extends BaseAction with APIAction {
  
}