package app

import com.typesafe.config.{Config => TConfig, ConfigFactory}

class DefaultDBConfig {
  val host     = "127.0.0.1"
  val port     = 27017
  val database = "chatwork"
  val user:Option[String]   = None
  val passwd:Option[String] = None
}

class DBConfig(config: TConfig) extends DefaultDBConfig {
  override val host     = config.getString("host")
  override val port     = config.getInt("port")
  override val database = config.getString("database")
  override val user   = if (config.hasPath("user"))     Some(config.getString("user")) else None
  override val passwd = if (config.hasPath("password")) Some(config.getString("password")) else None
}

object Config {
  val db =
    if (xitrum.Config.application.hasPath("db"))
      new DBConfig(xitrum.Config.application.getConfig("db"))
    else
      new DefaultDBConfig()

}