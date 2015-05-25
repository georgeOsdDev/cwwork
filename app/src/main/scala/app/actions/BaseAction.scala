package app.actions

import io.netty.handler.codec.http.HttpResponseStatus
import HttpResponseStatus.{BAD_REQUEST, INTERNAL_SERVER_ERROR}

import xitrum.{Action, Log}

import app.constants.{ErrorCds, Keys}
import ErrorCds._
import Keys._
import app.models.User

trait BaseAction extends Action with Log {
  def respondSuccess(result: AnyRef) = {
    respondJson(Map(
      STATUS -> SUCCESS,
      RESULT -> result
    ))
  }

  def respondClientError(errCd: Int, msg: String = "Invalid Request") = {
    response.setStatus(BAD_REQUEST)
    respondJson(Map(
      STATUS  -> errCd,
      MESSAGE -> msg
    ))
  }

  def respondServerError(errCd: Int, msg: String = "Unexpected Error") = {
    response.setStatus(INTERNAL_SERVER_ERROR)
    respondJson(Map(
      STATUS  -> errCd,
      MESSAGE -> msg
    ))
  }


}

trait NeedsToken {
  this: Action =>
  
  val tokeno = paramo("token")
  val emailo  = paramo("email")
    
  // Check accessToken
  beforeFilter {
  }
}

trait Versioning {
  this: Action =>

  // Check current API version
  beforeFilter {
  }
}

trait APIAction extends NeedsToken with Versioning {
  this: Action =>
}