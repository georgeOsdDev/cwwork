package app.actions

import xitrum.annotation.{GET, POST, PUT, DELETE, First, Swagger}

import app.constants.ErrorCds
import app.models.Thread
import app.validators.AppValidator


@Swagger(
  Swagger.Resource("threads", "APIs to list threads"),
  Swagger.StringBody("body", "email"),
  Swagger.StringBody("token", "token"),
  Swagger.OptIntQuery("limit", "limit count"),
  Swagger.OptIntQuery("skip", "Skip count"),
  Swagger.OptStringQuery("sort", "Sort key"),
  Swagger.OptStringQuery("tags", "tags to search as csv, eg: tag1,tag2"),
  Swagger.OptStringQuery("keyword", "keyword to search"),
  Swagger.Produces("application/json"),
  Swagger.Response(200, "JSON"),
  Swagger.Response(400, "Wrong token etc")
)
@GET("/api/:version/threads")
class ListThreads extends BaseAction with APIAction {
  def execute(){
    
    val tags    = paramo[String]("tags").map(_.split(',')).getOrElse(Array.empty)
    val keyword = paramo[String]("keyword")

    val limit = paramo[Int]("limit")
    val skip  = paramo[Int]("offset")
    val sort  = paramo[String]("skip")

    respondSuccess(Map("threads" -> Thread.listAll(tags, keyword, limit, skip, sort).map(_.toMap)))
  }
}

@POST("/api/:version/threads")
class CreateThread extends BaseAction with APIAction {
  def execute(){

    val reqJSON = requestContentJson[Map[String, String]].getOrElse(Map.empty)
    val titleo = reqJSON.get("title")
    val tagso  = reqJSON.get("tags")

    if (!titleo.isDefined) {
      respondClientError(ErrorCds.MISSING_PARAM, "title")
      return
    } else {
      val isValidTitleMinLength = AppValidator.minLength(AppValidator.TITLE_MINLENGTH)
      val isValidTitleMaxLength = AppValidator.maxLength(AppValidator.TITLE_MAXLENGTH)
      if (!isValidTitleMinLength(titleo.get) || !isValidTitleMaxLength(titleo.get)) {
        respondClientError(ErrorCds.INVALID_PARAM, "title")
        return
      }
    }
    val tags = tagso.getOrElse("").split(',')
    titleo.map{ title => 
      Thread.create(title, tags, at("currentUser")) match {
        case true =>
          respondSuccess(Map.empty)
        case false =>
          respondServerError(ErrorCds.SYSTEM_ERROR, "Failed to create thread")
       }
    }
  }
}

@GET("/api/:version/threads/:tid")
class ShowThread extends BaseAction with APIAction {
  def execute(){
    val tid    = param[String]("tid")
    Thread.findById(tid) match {
      case None =>
        respondClientError(ErrorCds.DATA_NOT_FOUND, "Thread not found")
      case Some(t) =>
        respondSuccess(t.toMap)
    }
  }
}

@PUT("/api/:version/threads/:tid")
class UpdateThread extends BaseAction with APIAction {
  def execute(){
    respondText("Not implemented")
  }
}

@DELETE("/api/:version/threads/:tid")
class DeleteThread extends BaseAction with APIAction {
    def execute(){
    respondText("Not implemented")
  }
}