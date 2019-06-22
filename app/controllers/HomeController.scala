package controllers

import javax.inject._

import play.api._
import play.api.mvc._
import play.api.i18n.I18nSupport
import play.api.i18n.MessagesApi
import play.api.db._

import services._

@Singleton
class HomeController @Inject() (cc: ControllerComponents) extends AbstractController(cc) with play.api.i18n.I18nSupport {

  def index() = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.index())
  }

  // 404 Error Handler.
  def error(code: Int = 404) = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.error(404, "Not found."))
  }
}
