package app.actions

import xitrum.annotation.{POST, PUT, DELETE, Swagger}

import app.constants.ErrorCds
import app.models.User
import app.validators.AppValidator

@Swagger(
  Swagger.Resource("auth", "API for authentication"),
  Swagger.Produces("application/json"),
  Swagger.Response(200, ""),
  Swagger.Response(400, "Invalid password")
)
@POST("/api/:version/auth/login")
class Login extends BaseAction with Versioning {
  def execute(){

    val reqJSON = requestContentJson[Map[String, String]].getOrElse(Map.empty)
    val emailo    = reqJSON.get("email")
    val passwordo = reqJSON.get("password")

    val user = for (email <- emailo; password <- passwordo) yield {
      User.authByPassword(email, password).getOrElse(None)
    }

    user match {
      case None =>
        respondClientError(ErrorCds.MISSING_PARAM, "email or password is missing")
      case Some(None) =>
        respondClientError(ErrorCds.INVALID_PARAM, "email or password is wrong")
      case Some(u: User)    =>
        u.refreshToken() match {
          case None =>
            respondServerError(ErrorCds.SYSTEM_ERROR, "Failed to update token")
          case Some(u2) =>
            respondSuccess(u2.toMapForMe)
        }
      case ignore =>
    }
  }
}

@POST("/api/:version/auth/signup")
class SignUp extends BaseAction with Versioning {
  def execute(){

    val reqJSON = requestContentJson[Map[String, String]].getOrElse(Map.empty)
    val emailo    = reqJSON.get("email")
    val passwordo = reqJSON.get("password")
    val nameo     = reqJSON.get("name")

    if (!emailo.isDefined) {
      respondClientError(ErrorCds.MISSING_PARAM, "email")
      return
    } else {
      val isValidEmail = AppValidator.pattern(AppValidator.EMAIL_REGEX)
      if (!isValidEmail(emailo.get)) {
        respondClientError(ErrorCds.INVALID_PARAM, "email")
        return
      }
    }
    
    if (!passwordo.isDefined) {
      respondClientError(ErrorCds.MISSING_PARAM, "email")
      return
    } else {
      val isValidMinLengthPassword = AppValidator.minLength(AppValidator.PASSWORD_MINLENGTH)
      val isValidMaxLengthPassword = AppValidator.maxLength(AppValidator.PASSWORD_MAXLENGTH)
      if (!isValidMinLengthPassword(passwordo.get) || !isValidMaxLengthPassword(passwordo.get)) {
        respondClientError(ErrorCds.INVALID_PARAM, "password")
        return
      }
    }

    if (!nameo.isDefined) {
      respondClientError(ErrorCds.MISSING_PARAM, "name")
      return
    } else {
      // @TODO
      val IS_VALID_NAME_PATTERN = true
      if (!IS_VALID_NAME_PATTERN) {
        respondClientError(ErrorCds.INVALID_PARAM, "name")
        return
      }
    }

    emailo.map{email => 
      User.findByEmail(email) match {
        case Some(u) =>
          respondClientError(ErrorCds.DATA_IS_ALREADY_EXIST, "email is already used")
        case None =>
          val result = for (password <- passwordo; name <- nameo) yield {
            User.create(email, password, name)
          }
          result match {
            case None =>
              respondServerError(ErrorCds.SYSTEM_ERROR, "Failed to create user")
            case Some(false) =>
              respondServerError(ErrorCds.SYSTEM_ERROR, "Failed to create user")
            case Some(true) =>
              respondSuccess(Map.empty)
          }
       }
    }
  }
}

@PUT("/api/:version/auth/token")
class RefreshToken extends BaseAction with APIAction {
  def execute(){
    respondText("Not implemented")
  }
}

@DELETE("/api/:version/auth/token")
class DeleteToken extends BaseAction with APIAction {
  def execute(){
    respondText("Not implemented")
  }
}