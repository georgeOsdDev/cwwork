package app.service

import java.util.{ArrayList => JArrayList, Date}
import scala.util.control.NonFatal
import org.bson.types.ObjectId
import com.mongodb.casbah.Imports._

import xitrum.Log

import app.Config

object DB extends Log {
  private val db = {
    val serverAddresse = new ServerAddress(Config.db.host, Config.db.port)
    val options = MongoClientOptions(autoConnectRetry=true)
    val mongoClient =
    try {
        if (Config.db.user.isDefined){
        Config.db.user.map {user =>
          Config.db.passwd.map { passwd =>
            val credentials = MongoCredential.createMongoCRCredential(user, Config.db.database, passwd.toCharArray())
            MongoClient(serverAddresse, List(credentials), options)
          }
        }
      } else {
        MongoClient(serverAddresse, options)
      }
    } catch {
      case e:MongoException =>
        log.error("Failed to create connection to MongoDB", e)
        sys.exit()
      case NonFatal(e) =>
        log.error("Failed to create connection to MongoDB", e)
        sys.exit()
    }
    mongoClient.asInstanceOf[MongoClient](Config.db.database)
  }

  val usersColl   = db("users")
  val threadsColl = db("threads")
  val postsColl   = db("posts")
  val allColls = List(
    usersColl,
    threadsColl,
    postsColl
  )

  ensureAllIndexies()

  def ensureAllIndexies() {
    ensureUsersIndexies
    ensureThreadsIndexies
    ensurePostsIndexies
  }

  private def ensureUsersIndexies = {
  }
  private def ensureThreadsIndexies = {
  }
  private def ensurePostsIndexies = {
  }

  

  //----------------------------------------------------------------------------

  def nowSecs() = (System.currentTimeMillis() / 1000).toInt

  def objIdFromSecs(secs: Int) = {
    val date = new Date(secs.toLong * 1000)
    new ObjectId(date)
  }

  /**
   * Numbers added in MongoDB console are double by default. Mistakenly added
   * numbers in MongoDB console (without casting using NumberInt) will cause
   * exception if we simply use mongoDBDoc.get("number").asInstanceOf[Int] to
   * get the number out from the server side.
   *
   * null is converted to 0.
   */
  def toInt(number: Any): Int = {
    try {
      number.asInstanceOf[Int]
    } catch {
      case NonFatal(e) =>
        number.asInstanceOf[Double].toInt
    }
  }

  def toBoolean(o: Object, defaultValue: Boolean): Boolean = {
    if (o == null) defaultValue
    else {
      try {
        o.asInstanceOf[Boolean]
      } catch {
        case NonFatal(e) =>
          defaultValue
      }
    }
  }

  //----------------------------------------------------------------------------

  /**
   * When WriteConcern.SAFE is used, inserting doc with existing index will cause
   * MongoException.DuplicateKey.
   *
   * @return true if there's no MongoException.DuplicateKey
   */
  def insertIfNonexistent(coll: MongoCollection, obj: MongoDBObject): Boolean = {
    try {
      coll.insert(obj, WriteConcern.Safe)
      true
    } catch {
      case e:MongoException =>
        false
      case _: Throwable =>
        false
    }
  }
}
