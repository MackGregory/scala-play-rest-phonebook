package models

import play.api.libs.json._

import scala.concurrent.Future

case class Response[T](errorCode: Int, message: Option[String], data: Option[T]){
  implicit def toJson(implicit writer: Writes[T]): JsValue = {
    Json.obj("errorCode" -> errorCode, "message" -> message, "data" -> data.map(Json.toJson(_)))
  }
}