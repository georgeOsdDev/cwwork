package app.validators

import scala.util.matching.Regex

object AppValidator {
  
  val EMAIL_REGEX = """^[a-zA-Z0-9\.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?(?:\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)*$""".r
  val PASSWORD_MINLENGTH = 8
  val PASSWORD_MAXLENGTH = 16
  val NAME_MINLENGTH = 1
  val NAME_MAXLENGTH = 10
  val TITLE_MINLENGTH = 1
  val TITLE_MAXLENGTH = 40
  val POST_MINLENGTH = 1
  val POST_MAXLENGTH = 140

  def maxLength(max: Int): String => Boolean = {
    (v) => v.length <= max
  }

  def minLength(min: Int): String => Boolean = {
    (v) => min <= v.length
  }

  def pattern(pattern: Regex): String => Boolean = {
    (v) => pattern.findFirstIn(v).nonEmpty 
  }

}