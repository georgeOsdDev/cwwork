package app.actions

import xitrum.annotation.{POST, PUT, DELETE, Swagger}

@Swagger(
  Swagger.Resource("auth", "API for authentication"),
  Swagger.Produces("application/json"),
  Swagger.Response(200, ""),
  Swagger.Response(400, "Invalid password")
)
@POST("/api/:version/auth/login")
class Login extends BaseAction with Versioning {
  def execute(){
    
  }
}

@POST("/api/:version/auth/signup")
class SignUp extends BaseAction with Versioning {
  def execute(){
    
  }
}

@PUT("/api/:version/auth/token")
class RefreshToken extends BaseAction with APIAction {
  def execute(){
    
  }
}

@DELETE("/api/:version/auth/token")
class DeleteToken extends BaseAction with APIAction {
  
}