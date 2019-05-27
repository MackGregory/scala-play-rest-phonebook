package controllers

import javax.inject._
import play.api.mvc._
import models.{Phone, Response}
import services.PhoneServiceImpl
import play.api.libs.json._

import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success}
import scala.util.matching.Regex

@Singleton
class HomeController @Inject()(cc: ControllerComponents, phoneService: PhoneServiceImpl)(implicit ec: ExecutionContext)
  extends AbstractController(cc)
    with play.api.i18n.I18nSupport {

  private def validatePhoneNumber(number: String): Boolean = {
    val reg: Regex = """^([+])(\d)(\d{10})$""".r
    reg.pattern.matcher(number).matches
  }

  implicit def toResult[T](f: Future[T])(implicit writes: Writes[T]): Future[Result] = {
    f.map(data => Response(0, Some("Executed successfully"), Some(data)))
      .recover { case e => Response(1, Some(e.getMessage), None) }
      .map(resp => Ok(resp.toJson))
  }

  def phones = Action.async { _ =>
    phoneService.getAllPhones
  }

  def addPhoneNumber = Action.async(parse.json) { request =>
    val phoneResult = request.body.validate[Phone]
    phoneResult.fold(
      errors => {
        Future.successful(BadRequest(Json.obj("errorCode" -> 1, "message" -> JsError.toJson(errors))))
      },
      phone => {
        phoneService.addNewPhone(phone)
      }
    )
  }

  def deletePhone(number: String) = Action.async { _ =>
      phoneService.deletePhone(number)
  }

  def updatePhone(number: String) = Action.async(parse.json) { request =>
    val phoneResult = request.body.validate[Phone]
    phoneResult.fold(
      errors => {
        Future.successful(BadRequest(Json.obj("errorCode" -> 1, "message" -> JsError.toJson(errors))))
      },
      phone => {
        phoneService.updatePhoneData(number, phone)
      }
    )
  }

  def searchPhoneByNumber(number: String) = Action.async { _ =>
    phoneService.searchPhoneByNumber(number)
  }

  def searchPhoneByName(name: String) = Action.async { _ =>
    phoneService.searchPhoneByName(name)
  }
}
