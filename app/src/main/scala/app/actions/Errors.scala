package app.actions

import io.netty.handler.codec.http.HttpResponseStatus.NOT_FOUND

import xitrum.Action
import xitrum.annotation.{Error404, Error500}

import app.constants.ErrorCds
import ErrorCds._

@Error404
class NotFoundError extends BaseAction {
  def execute() {
    response.setStatus(NOT_FOUND)
    respondClientError(UNKOWN_API, "API not found")
  }
}

@Error500
class ServerError extends BaseAction {
  def execute() {
    respondServerError(SYSTEM_ERROR, "Internal server error")
  }
}
