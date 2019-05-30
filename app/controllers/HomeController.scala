package controllers

import models.{Phone, Response}
import play.api.libs.json._
import play.api.mvc._
import services.PhoneServiceImpl
import utils.InvalidPhoneNumber

import scala.concurrent.{ExecutionContext, Future}
import scala.util.matching.Regex

class HomeController (cc: ControllerComponents, phoneService: PhoneServiceImpl)(implicit ec: ExecutionContext)
  extends AbstractController(cc)
    with play.api.i18n.I18nSupport {

  private def validatePhoneNumber(number: String): Boolean = {
    val reg: Regex = """([+])(\d)(\d{9})""".r
    reg.pattern.matcher(number).matches()
  }

  implicit def toResult[T](f: Future[T])(implicit writes: Writes[T]): Future[Result] = {
    f.map(data => Response(0, Some("Executed successfully"), Some(data)))
      .recover { case e => Response(1, Some(e.getMessage), None) }
      .map(resp => Ok(resp.toJson))
  }

  def phones: Action[AnyContent] = Action.async { _ =>
    phoneService.getAllPhones
  }

  def addPhone: Action[JsValue] = Action.async(parse.json) { request =>
    request.body.validate[Phone].fold(
      errors => {
        Future.successful(BadRequest(Json.obj("errorCode" -> 1, "message" -> JsError.toJson(errors))))
      },
      phone => {
        if(validatePhoneNumber(phone.number)) {
          toResult(phoneService.addNewPhone(phone).map(_ => phone))
        }
        else {
          toResult(Future.failed[Phone](new InvalidPhoneNumber))
        }
      }
    )
  }

  def deletePhone(number: String): Action[AnyContent] = Action.async { _ =>
    if(validatePhoneNumber(number))
      toResult(phoneService.deletePhone(number))
    else
      toResult(Future.failed[Phone](new InvalidPhoneNumber))
  }

  def updatePhone(number: String): Action[JsValue] = Action.async(parse.json) { request =>
    request.body.validate[Phone].fold(
      errors => {
        Future.successful(BadRequest(Json.obj("errorCode" -> 1, "message" -> JsError.toJson(errors))))
      },
      phone => {
        if(validatePhoneNumber(phone.number))
          toResult(phoneService.updatePhoneData(number, phone).map(_ => phone))
        else
          toResult(Future.failed[Phone](new InvalidPhoneNumber))
      }
    )
  }

  def searchPhoneByNumber(number: String): Action[AnyContent] = Action.async { _ =>
    phoneService.searchPhoneByNumber(number)
  }

  def searchPhoneByName(name: String): Action[AnyContent] = Action.async { _ =>
    phoneService.searchPhoneByName(name)
  }
}
