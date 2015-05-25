package app.actions

import xitrum.Action
import xitrum.annotation.{Error404, Error500}

import app.constants.ErrorCds
import ErrorCds._

@Error404
class NotFoundError extends BaseAction {
  def execute() {
    respondClientError(UNKOWN_API, "API not found")
  }
}

@Error500
class ServerError extends BaseAction {
  def execute() {
    respondClientError(SYSTEM_ERROR, "Internal server error")
  }
}
