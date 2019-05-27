package services

import controllers.HomeController
import play.api.routing.Router
import play.api.routing.sird._
import repositories.DBQueriesRepoImpl

trait PhoneComponents extends WithControllerComponents {
  val dbRepo = new DBQueriesRepoImpl
  val cacheImpl: CacheImpl = new CacheImpl
  val phoneServiceImpl: PhoneServiceImpl = new PhoneServiceImpl(dbRepo, cacheImpl)
  val homeController: HomeController = new HomeController(controllerComponents, phoneServiceImpl)

  val phoneRoutes: Router.Routes = {
    case GET(p"/phones") =>
      homeController.phones
    case GET(p"/phones/search+number=$number") =>
      homeController.searchPhoneByNumber(number)
    case GET(p"/phones/search+name=$name") =>
      homeController.searchPhoneByName(name)
    case DELETE(p"/phones/$number") =>
      homeController.deletePhone(number)
    case POST(p"/phones/$number") =>
      homeController.updatePhone(number)
    case POST(p"/phones/add") =>
      homeController.addPhoneNumber
  }
}
