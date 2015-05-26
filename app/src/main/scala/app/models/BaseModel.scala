package app.models

import com.mongodb.casbah.Imports.MongoDBObject

import app.service.DB

abstract class BaseModel {
  def toMap: Map[String, Any]
}

object BaseModel {
  
}