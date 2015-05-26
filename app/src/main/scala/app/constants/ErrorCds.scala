package app.constants

object ErrorCds {
  val SUCCESS       = 0

  // Client errors
  val UNKOWN_API     = 101
  val INVALID_PARAM  = 102
  val MISSING_PARAM  = 103
  val DATA_NOT_FOUND = 104
  val DATA_IS_ALREADY_EXIST = 105
  val UNSUPPORTED_API_VERSION = 106
  val NOT_AUTHENTICATED = 107
  val TOKEN_EXPIRED = 108
  
  // Server errors
  val IN_MAINTENANCE = 201
  val SYSTEM_ERROR   = 202
  val POSTS_COUNT_REACHED_MAX = 203
  
  val UNEXPECTED_ERROR = 999
}