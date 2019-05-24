package controllers

import javax.inject._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._
import models.Phone
import play.api.libs.json._

@Singleton
class HomeController @Inject()(cc: ControllerComponents) extends AbstractController(cc) with play.api.i18n.I18nSupport {
  //======================
  def addPhoneNumber() = Action { implicit request =>
    phoneForm.bindFromRequest.fold(
      formWithErrors => {
        BadRequest(views.html.phones(Phone.all, formWithErrors))
      },
      data => data match {
        case (number, name) =>
          Phone.create(number, name)
          Redirect(routes.HomeController.phones())
      }
    )
  }

  def phones() = Action { request =>
    Ok(views.html.phones(Phone.all, phoneForm))
  }
  //=======================

  implicit val phoneWrites: Writes[Phone] = Json.writes[Phone]
  implicit val phoneReads: Reads[Phone] = Json.reads[Phone]

  def phonesJson() = Action { request =>
    val json = Json.toJson(Phone.all)
    Ok(json)
  }

  def addPhoneNumberJson = Action(parse.json) { request =>
    val phoneResult = request.body.validate[Phone]
    phoneResult.fold(
      errors => {
        BadRequest(Json.obj("status" -> "KO", "message" -> JsError.toJson(errors)))
      },
      phone => {
        Phone.create(phone.number, phone.name)
        Ok(Json.obj("status" -> "OK", "message" -> s"Phone ${phone.number} saved."))
      }
    )
  }

  def deletePhone(id: Int) = Action { request =>
    Phone.delete(id)
    Ok(Json.obj("status" -> "OK", "message" -> s"Phone with id $id deleted."))
  }

  def updatePhone(id: Int) = Action(parse.json) { request =>
    val phoneResult = request.body.validate[Phone]
    phoneResult.fold(
      errors => {
        BadRequest(Json.obj("status" -> "KO", "message" -> JsError.toJson(errors)))
      },
      phone => {
        Phone.update(id, phone.number, phone.name)
        Ok(Json.obj("status" -> "OK", "message" -> s"Phone #$id updated."))
      }
    )
  }

  val phoneForm = Form(
    tuple (
      "number" -> nonEmptyText,
      "name" -> nonEmptyText
    )
  )

}
