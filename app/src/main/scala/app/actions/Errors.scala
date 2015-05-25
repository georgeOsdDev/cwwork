package app.action

import xitrum.Action
import xitrum.annotation.{Error404, Error500}

@Error404
class NotFoundError extends Action {
  def execute() {
    respondJson(Map.empty)
  }
}

@Error500
class ServerError extends Action {
  def execute() {
    respondJson(Map.empty)
  }
}
