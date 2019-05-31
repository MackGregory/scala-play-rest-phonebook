package services

import controllers.HomeController
import play.api.routing.sird._
import play.api.routing.{Router, SimpleRouter}
import repositories.DBQueriesRepoImpl

trait PhoneComponents extends WithControllerComponents with SimpleRouter {
  val dbRepo = new DBQueriesRepoImpl
  val cache: Cache = new CacheImpl
  val phoneServiceImpl: PhoneServiceImpl = new PhoneServiceImpl(dbRepo, cache)
  val homeController: HomeController = new HomeController(controllerComponents, phoneServiceImpl)

  val phoneRoutes: Router.Routes = {
    case GET(p"/phones") =>
      homeController.phones
    case GET(p"/phones/searchByNumberSub" ? q_?"number=$number") =>
      homeController.searchPhoneByNumber(number.getOrElse(""))
    case GET(p"/phones/searchByNameSub" ? q_?"name=$name") =>
      homeController.searchPhoneByName(name.getOrElse(""))
    case DELETE(p"/phones/delete/$number") =>
      homeController.deletePhone(number)
    case POST(p"/phones/addPhone") =>
      homeController.addPhone
    case POST(p"/phones/update/$number") =>
      homeController.updatePhone(number)
  }
}