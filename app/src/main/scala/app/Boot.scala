package app

import xitrum.Server
import app.models.User

object Boot {
  def main(args: Array[String]) {
    User.initializeAdmin(Config.adminUser.email, Config.adminUser.password, Config.adminUser.name)
    Server.start()
  }
}
