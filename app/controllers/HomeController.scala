package controllers

import javax.inject._
import play.api.mvc._
import models.Phone
import services.PhoneServiceImpl
import play.api.libs.json._

import scala.concurrent.ExecutionContext
import scala.util.{Failure, Success}
import scala.util.matching.Regex

@Singleton
class HomeController @Inject()(cc: ControllerComponents, phoneService: PhoneServiceImpl)(implicit ec: ExecutionContext) extends AbstractController(cc) with play.api.i18n.I18nSupport {
  implicit val phoneWrites: Writes[Phone] = Json.writes[Phone]
  implicit val phoneReads: Reads[Phone] = Json.reads[Phone]

  private def validatePhoneNumber(number: String): Boolean = {
    val reg: Regex = """^([+])(\d)(\d{10})$""".r
    reg.pattern.matcher(number).matches
  }

  def phones() = Action.async { request =>
    phoneService.getAllPhones
      .map(list => Ok(Json.toJson(list)))
  }

  def addPhoneNumber = Action(parse.json) { request =>
    val phoneResult = request.body.validate[Phone]
    phoneResult.fold(
      errors => {
        BadRequest(Json.obj("status" -> "KO", "message" -> JsError.toJson(errors)))
      },
      phone => {
        phoneService.addNewPhone(phone)
        Ok(Json.obj("status" -> "OK", "message" -> s"Phone ${phone.number} saved."))
      }
    )
  }

  def deletePhone(id: Int) = Action { request =>
    phoneService.deletePhone(id)
    Ok(Json.obj("status" -> "OK", "message" -> s"Phone with id $id deleted."))
  }

  def updatePhone(id: Int) = Action(parse.json) { request =>
    val phoneResult = request.body.validate[Phone]
    phoneResult.fold(
      errors => {
        BadRequest(Json.obj("status" -> "KO", "message" -> JsError.toJson(errors)))
      },
      phone => {
        phoneService.updatePhoneData(id, phone)
        Ok(Json.obj("status" -> "OK", "message" -> s"Phone #$id updated."))
      }
    )
  }
}
