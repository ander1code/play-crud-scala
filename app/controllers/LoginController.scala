package controllers

import javax.inject._

import play.api._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._
import play.filters.csrf._
import play.filters.csrf.CSRF.Token

import models._
import services._
import forms._

@Singleton
class LoginController @Inject()(cc: ControllerComponents, vd: ValidationData, hs: Hash, op: Operation, addToken: CSRFAddToken, checkToken: CSRFCheck) extends AbstractController(cc) with play.api.i18n.I18nSupport {

  private val loginForm = Form(
		mapping(
			"username" -> text.verifying(vd.message, username => vd.validateDataLogin(username, "Username")),
			"password" -> text.verifying(vd.message, password => vd.validateDataLogin(password, "Password")),
		)(LoginForm.apply)(LoginForm.unapply)
	)

  def index() = addToken(Action { 
    implicit request: Request[AnyContent] =>
      request.session.get("logged").map { logged =>
        Redirect("/").flashing(("title", "Information - "), ("class", "alert-info alert alert-dismissible"), ("message", "User already logged in."))
   	 	}.getOrElse {
        val Token(name, value) = CSRF.getToken.get
        val lData = Map(
          "username" -> "crudadmin",
          "password" -> "crudadmin"
        )
        Ok(views.html.login.form(loginForm.bind(lData)))
    	}
  })

  def login() = checkToken(Action { implicit request: Request[AnyContent] =>
    request.session.get("logged").map { logged =>
  		Redirect("/").flashing(("title", "Information - "), ("class", "alert-info alert alert-dismissible"), ("message", "User already logged in."))
   	}.getOrElse {
      loginForm.bindFromRequest.fold(
        formWithErrors => {
    	    BadRequest(views.html.login.form(formWithErrors))
        },
        userData => {
    	   val r: Boolean = op.login(userData)
    	   r match {
    		    case true => {
              Redirect("/").withSession(("logged" -> "true"), ("username" -> userData.username.toString)).flashing(("title", "Success - "), ("class", "alert-success alert alert-dismissible"), ("message", "Successfully logged."))
    		    }
    		    case false => {
    			    Redirect("/login").flashing(("title", "Error - "), ("class", "alert-danger alert alert-dismissible"), ("message", "Invalid username and password."))
    		    }
    	   }
      })
    }
  })
  	
	def logoff() = Action { implicit request: Request[AnyContent] =>
    request.session.get("logged").map { logged =>
  		Redirect("/").withNewSession.flashing(("title", "Success - "), ("class", "alert-success alert alert-dismissible"), ("message", "Successfully logged out."))
	 	  }.getOrElse {
  		Redirect("/").flashing(("title", "Information - "), ("class", "alert-info alert alert-dismissible"), ("message", "User is offline."))
	  }
	}
}