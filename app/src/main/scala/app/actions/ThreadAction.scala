package app.actions

import xitrum.annotation.{GET, POST, PUT, DELETE, Swagger}

@GET("/api/:version/threads")
class ListThreads extends BaseAction with APIAction {
  def execute(){
    
    val tags    = paramo[String]("tags").getOrElse("").split(',')
    val keyword = paramo[String]("q")

    val limit = paramo[Int]("limit").getOrElse(100)
    val skip  = paramo[Int]("offset").getOrElse(0)
    val sort  = paramo[String]("skip").getOrElse("_id")


    respondSuccess(Map("users" -> Thread.listAll(tags, keyword, limit, skip, sort).map(_.toMap)))
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