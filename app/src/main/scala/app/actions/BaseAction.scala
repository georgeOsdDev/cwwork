package app.actions

import io.netty.channel.ChannelFuture
import io.netty.handler.codec.http.HttpResponseStatus
import HttpResponseStatus.{BAD_REQUEST, INTERNAL_SERVER_ERROR, UNAUTHORIZED}


import xitrum.{Action, Log, SkipCsrfCheck}

import app.Config
import app.constants.{ErrorCds, Keys}
import ErrorCds._
import Keys._
import app.models.User

trait BaseAction extends Action with Log with SkipCsrfCheck {
  def respondSuccess(result: AnyRef): ChannelFuture = {
    respondJson(Map(
      STATUS -> SUCCESS,
      RESULT -> result
    ))
  }

  def respondClientError(errCd: Int, msg: String = "Invalid Request"): ChannelFuture = {
    response.setStatus(BAD_REQUEST)
    respondJson(Map(
      STATUS  -> errCd,
      MESSAGE -> msg
    ))
  }

  def respondServerError(errCd: Int, msg: String = "Unexpected Error"): ChannelFuture = {
    response.setStatus(INTERNAL_SERVER_ERROR)
    respondJson(Map(
      STATUS  -> errCd,
      MESSAGE -> msg
    ))
  }
}

trait NeedsToken {
  this: Action =>
  // Check accessToken
    
  lazy val reqJSON = requestContentJson[Map[String, String]].getOrElse(Map.empty)

  beforeFilter {

    val emailo  = reqJSON.get("email")
    val tokeno  = reqJSON.get("token")

    val user = for (email <- emailo; token <- tokeno) yield {
      User.authByToken(email, token).getOrElse(None)
    }
    user match {
      case None =>
        respondJson(Map(
          STATUS  -> MISSING_PARAM,
          MESSAGE -> "email or token is missing"
        ))
        false
      case Some(None) =>
        respondJson(Map(
          STATUS  -> INVALID_PARAM,
          MESSAGE -> "email or token is invalid"
        ))
        false
      case Some(u: User)    =>
        if (u.isTokenExpired) {
        respondJson(Map(
          STATUS  -> TOKEN_EXPIRED,
          MESSAGE -> "token expired"
        ))
          false
        } else
          at("currentUser") = u
          true

      case ignore =>
    }
  }
}

trait Versioning {
  this: Action =>

  // Check current API version
  beforeFilter {
    val apiVersion = paramo[String]("version")
    apiVersion.map{v =>
      if (v != Config.apiVersion){
          response.setStatus(BAD_REQUEST)
          respondJson(Map(
            STATUS  -> UNSUPPORTED_API_VERSION,
            MESSAGE -> s"Current supported api version is ${Config.apiVersion}"
        ))
      }
    }
  }
}

trait APIAction extends NeedsToken with Versioning {
  this: Action =>
}