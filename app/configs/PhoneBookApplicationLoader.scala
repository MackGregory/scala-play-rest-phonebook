package configs

import play.api._
import play.api.mvc._
import play.api.ApplicationLoader.Context
import play.filters.cors.{CORSConfig, CORSFilter}
import play.api.routing.Router

class PhoneBookApplicationLoader extends ApplicationLoader {
  def load(context: Context): Application = new PhoneBookComponents(context).application
}

class PhoneBookComponents(context: Context) extends BuiltInComponentsFromContext(context) with PhoneComponents {

  val routes: PartialFunction[RequestHeader, Handler] = phoneRoutes

  override lazy val router: Router = Router.from(routes)

  private lazy val corsFilter: CORSFilter = {
    val corsConfig = CORSConfig.fromConfiguration(configuration)
    CORSFilter(corsConfig)
  }

  override def httpFilters: Seq[EssentialFilter] = List(corsFilter)
}