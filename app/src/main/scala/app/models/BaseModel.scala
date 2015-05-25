package app.models

import app.service.DB

abstract class BaseModel {

  def toMap: Map[String, AnyRef]
}

object BaseModel {
}