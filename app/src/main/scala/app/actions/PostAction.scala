package app.actions

import io.netty.handler.codec.http.HttpResponseStatus.BAD_REQUEST

import xitrum.Action
import xitrum.annotation.{GET, POST, PUT, DELETE, Swagger}

import app.constants.{ErrorCds, Keys} 
import app.models.{User, Thread, Post}
import app.validators.AppValidator

trait DependsOnThread {
  this: Action =>

  beforeFilter {
    val tid   = param[String]("tid")
    Thread.findById(tid) match {
      case None =>
        response.setStatus(BAD_REQUEST)
        respondJson(Map(
          Keys.STATUS  -> ErrorCds.DATA_NOT_FOUND,
          Keys.MESSAGE -> "Thread not found"
        ))
        false
      case Some(t) =>
        at("thread") = t
        true
    }
  }
}

@GET("/api/:version/threads/:tid/posts")
class ListPosts extends BaseAction with APIAction with DependsOnThread {
  def execute(){

    val tid   = param[String]("tid")
    val limit = paramo[Int]("limit")
    val skip  = paramo[Int]("skip")

    val thread = at("thread").asInstanceOf[Thread]
    
    val posts = Post.searchByThreadId(tid, limit, skip).map(_.toMap)
    respondSuccess(Map("posts" -> posts, "totalCount" -> thread.postsCount))
  }
}

@POST("/api/:version/threads/:tid/posts")
class CreatePost extends BaseAction with APIAction with DependsOnThread {
  def execute(){

    val tid         = param[String]("tid")
    val thread      = at("thread").asInstanceOf[Thread]
    val currentUser = at("currentUser").asInstanceOf[User]
    
    val reqJSON = requestContentJson[Map[String, String]].getOrElse(Map.empty)
    val bodyo = reqJSON.get("body")

    if (!bodyo.isDefined) {
      respondClientError(ErrorCds.MISSING_PARAM, "body")
      return
    } else {
      val isValidBodyMinLength = AppValidator.minLength(AppValidator.POST_MINLENGTH)
      val isValidBodyMaxLength = AppValidator.maxLength(AppValidator.POST_MAXLENGTH)
      if (!isValidBodyMinLength(bodyo.get) || !isValidBodyMaxLength(bodyo.get)) {
        respondClientError(ErrorCds.INVALID_PARAM, "body")
        return
      }
    }

    if (thread.postsCount >= Thread.MAX_POSTS) {
      respondServerError(ErrorCds.POSTS_COUNT_REACHED_MAX, s"Posts count reached to ${Thread.MAX_POSTS}")
      return
    }

    bodyo.map{ body => 
      val created = Post.create(body, tid, currentUser)
      created match {
        case None =>
          respondServerError(ErrorCds.SYSTEM_ERROR, "Failed to create post")
        case Some(id) =>
          // cache into users/threads collection
          // @FIXME error case
          val post = Post.findById(id.toString).map{p =>
            currentUser.setLatestPost(p)
            thread.addRecentPost(p)
          }
          respondSuccess(Map.empty)
      }
    }
  }
}

@GET("/api/:version/threads/:tid/posts/:pid")
class ShowPost extends BaseAction with APIAction with DependsOnThread{
  def execute(){
    val pid  = param[String]("pid")
    Post.findById(pid) match {
      case None =>
        respondClientError(ErrorCds.DATA_NOT_FOUND, "Post not found")
      case Some(post) =>
        respondSuccess(post.toMap)
    }
  }
}

@PUT("/api/:version/threads/:tid/posts/:pid")
class UpdatePost extends BaseAction with APIAction with DependsOnThread{
  def execute(){
    respondText("Not implemented")
  }
}

@DELETE("/api/:version/threads/:tid/posts/:pid")
class DeletePost extends BaseAction with APIAction with DependsOnThread{
  def execute(){
    respondText("Not implemented")
  }
}