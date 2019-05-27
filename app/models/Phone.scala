package models

import play.api.libs.json.{Json, Reads, Writes}

case class Phone(number: String, name: String)

object Phone {
  implicit val phoneWrites: Writes[Phone] = Json.writes[Phone]
  implicit val phoneReads: Reads[Phone] = Json.reads[Phone]
}