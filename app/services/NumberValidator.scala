package services

import scala.util.matching.Regex

object NumberValidator {
  val reg: Regex = """^([+])(\d)(\d{10})$""".r

  def validate(number: String): Boolean = reg.pattern.matcher(number).matches
}