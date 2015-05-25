package app.models

import com.mongodb.casbah.Imports.MongoDBObject

import app.service.DB

abstract class BaseModel {

//  def asDBObject = MongoDBObject(
//    doc.toMap.asScala.map{case (k,v) => (k.toString, v)}.toList
//  )


  def toJson: String

  def toMap: Map[String, AnyRef]
}

object BaseModel {
  
}