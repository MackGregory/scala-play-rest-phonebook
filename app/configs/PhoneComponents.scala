package configs

import controllers.HomeController
import configs.ExecutionContextComponents
import play.api.routing.Router
import play.api.routing.sird._
import configs.WithControllerComponents
import repositories.DBQueriesRepoImpl
import services.PhoneServiceImpl

import scala.concurrent.ExecutionContext

trait PhoneComponents extends WithControllerComponents with ExecutionContextComponents {

  lazy val ec: ExecutionContext = executionContext
  lazy val phoneServiceImpl: PhoneServiceImpl = new PhoneServiceImpl(new DBQueriesRepoImpl)
  lazy val homeController: HomeController = new HomeController(controllerComponents, phoneServiceImpl)

  val phoneRoutes: Router.Routes = {
    case GET(p"/phones") =>
      homeController.phones()
    case POST(p"/phones/addPhone") =>
      homeController.addPhoneNumber
  }
}
