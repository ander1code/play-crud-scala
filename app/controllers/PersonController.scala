package controllers

import scala.collection.mutable.ListBuffer

import java.util.Date
import javax.inject._

import play.api._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._
import play.api.data.validation.Constraints._
import play.api.i18n.I18nSupport
import play.api.i18n.MessagesApi
import play.filters.csrf._
import play.filters.csrf.CSRF.Token

import models._
import services._
import forms.PersonForm

@Singleton
class PersonController @Inject() (cc: ControllerComponents, fd: FormatData, vd: ValidationData, op: Operation, addToken: CSRFAddToken, checkToken: CSRFCheck) extends AbstractController(cc) with play.api.i18n.I18nSupport {

  private var id: Int = 0

  private var email: String = ""

  private var picture: Array[Byte] = null

  private var statusEdit: Boolean = false

  /*
  private val personForm = Form(
    mapping(
      "name" -> text.verifying(vd.message, name => vd.validateName(name)),
      "email" -> text.verifying(vd.message, email => vd.validateEmail(email, this.email, op.getAll, this.statusEdit)),
      "salary" -> text.verifying(vd.message, salary => vd.validateSalary(salary)),
      "birthday" -> text.verifying(vd.message, birthday => vd.validateBirthday(birthday)),
      "gender" -> text.verifying(vd.message, gender => vd.validateGender(gender)),
      "picture" -> text.verifying(vd.message, picture => vd.validatePicture(picture, this.statusEdit)))(PersonForm.apply)(PersonForm.unapply))
    */

  private val personForm = Form(
    mapping(
      "name" -> text.verifying(vd.message, name => vd.validateName(name)),
      "email" -> text.verifying(vd.message, email => vd.validateEmail(email, this.email, this.statusEdit)),
      "salary" -> text.verifying(vd.message, salary => vd.validateSalary(salary)),
      "birthday" -> text.verifying(vd.message, birthday => vd.validateBirthday(birthday)),
      "gender" -> text.verifying(vd.message, gender => vd.validateGender(gender)))(PersonForm.apply)(PersonForm.unapply))

  def index() = Action { implicit request: Request[AnyContent] =>
    request.session.get("logged").map { logged =>
      Ok(views.html.person.index(op.findByName())(fd))
    }.getOrElse {
      Redirect("/login").flashing(("title", "Error - "), ("class", "alert-danger alert alert-dismissible"), ("message", "User is offline."))
    }
  }

  def add() = addToken(Action { implicit request: Request[AnyContent] =>
    request.session.get("logged").map { logged =>
      val Token(name, value) = CSRF.getToken.get
      Ok(views.html.person.form(personForm, routes.PersonController.save, false))
    }.getOrElse {
      Redirect("/login").flashing(("title", "Error - "), ("class", "alert-danger alert alert-dismissible"), ("message", "User is offline."))
    }
  })

  def details(id: Int) = Action { implicit request: Request[AnyContent] =>
    request.session.get("logged").map { logged =>
      val p: Person = op.findById(id)
      p match {
        case null => Redirect(routes.HomeController.error(404))
        case _ => Ok(views.html.person.details(p)(fd))
      }
    }.getOrElse {
      Redirect("/login").flashing(("title", "Error - "), ("class", "alert-danger alert alert-dismissible"), ("message", "User is offline."))
    }
  }

  def save() = checkToken(Action(parse.multipartFormData) { implicit request =>
    request.session.get("logged").map { logged =>

      /*
      request.body.file("picture").map { picture =>
      }
      */

      this.statusEdit = false
      vd.valid = true
      personForm.bindFromRequest.fold(
        formWithErrors => {
          BadRequest(views.html.person.form(formWithErrors, routes.PersonController.save, true))
        },
        personData => {
          val r = op.savePerson(personData)
          r match {
            case true => {
              Redirect(routes.PersonController.index()).flashing(("title", "Success - "), ("class", "alert-success alert alert-dismissible"), ("message", "Successfully created."))
            }
            case false => {
              Redirect(routes.PersonController.index()).flashing(("title", "Error - "), ("class", "alert-danger alert alert-dismissible"), ("message", "Error registering person."))
            }
          }
        })
    }.getOrElse {
      Redirect("/login").flashing(("title", "Error - "), ("class", "alert-danger alert alert-dismissible"), ("message", "User is offline."))
    }
  })

  def edit(id: Int) = addToken(Action { implicit request: Request[AnyContent] =>
    request.session.get("logged").map { logged =>
      val p: Person = op.findById(id)
      p match {
        case null => Redirect(routes.HomeController.error(404))
        case _ => {
          val Token(name, value) = CSRF.getToken.get
          this.id = p.id
          this.email = p.email.toString
          // this.picture = p.picture

          val pData = Map(
            "name" -> p.name.toString,
            "email" -> p.email.toString,
            "salary" -> p.salary.toString,
            "birthday" -> fd.formatDateToView(p.birthday).toString,
            "gender" -> p.gender.toString)
          vd.valid = false
          Ok(views.html.person.form(personForm.bind(pData), routes.PersonController.update, false))
        }
      }
    }.getOrElse {
      Redirect("/login").flashing(("title", "Error - "), ("class", "alert-danger alert alert-dismissible"), ("message", "User is offline."))
    }
  })

  def update() = checkToken(Action { implicit request: Request[AnyContent] =>

    request.session.get("logged").map { logged =>
      this.statusEdit = true
      vd.valid = true

      personForm.bindFromRequest.fold(
        formWithErrors => {
          BadRequest(views.html.person.form(formWithErrors, routes.PersonController.update, true))
        },
        personData => {
          val r = op.updatePerson(this.id.toInt, personData)
          r match {
            case true => {
              Redirect(routes.PersonController.index()).flashing(("title", "Success - "), ("class", "alert-success alert alert-dismissible"), ("message", "Successfully edited."))
            }
            case false => {
              Redirect(routes.PersonController.index()).flashing(("title", "Error - "), ("class", "alert-danger alert alert-dismissible"), ("message", "Error editing person."))
            }
          }
        })
    }.getOrElse {
      Redirect("/login").flashing(("title", "Error - "), ("class", "alert-danger alert alert-dismissible"), ("message", "User is offline."))
    }
  })

  def delete() = Action { implicit request: Request[AnyContent] =>
    request.session.get("logged").map { logged =>
      val r = op.deletePerson(this.id)
      r match {
        case true => {
          Redirect("/person").flashing(("title", "Success - "), ("class", "alert-success alert alert-dismissible"), ("message", "Successfully deleted."))
        }
        case false => {
          Redirect(routes.PersonController.index()).flashing(("title", "Error - "), ("class", "alert-danger alert alert-dismissible"), ("message", "Error deleting person."))
        }
      }
    }.getOrElse {
      Redirect("/login").flashing(("title", "Error - "), ("class", "alert-danger alert alert-dismissible"), ("message", "User is offline."))
    }
  }
}
