package app.constants

object ErrorCds {
  val SUCCESS       = 0

  // Client errors
  val UNKOWN_API     = 101
  val INVALID_PARAM  = 102
  val MISSING_PARAM  = 103
  val DATA_NOT_FOUND = 104
  
  // Server errors
  val IN_MAINTENANCE = 201
  val SYSTEM_ERROR   = 202
  
  val UNEXPECTED_ERROR = 999
}