package services

import scala.collection.mutable._

import java.util.Date
import javax.inject._

import models._

class ValidationData @Inject() (op: Operation) {

  var message: String = "";

  var valid: Boolean = false

  def validateName(name: String): Boolean = {
    if (this.valid) {
      if (this.checkEmpty(name)) {
        this.message = "Name is empty."
        return false
      } else {
        if (!this.checkMinMaxString(name, 3, 255)) {
          this.message = "Amount of character invalid for name. Min=3 and Max=255."
          return false
        }
      }
      return true
    }
    return true
  }

  def validateEmail(email: String, pemail: String, statusEdit: Boolean): Boolean = {
    if (this.valid) {
      if (statusEdit) {
        if (email == pemail) {
          return true
        }
      }
      if (this.checkEmpty(email)) {
        this.message = "E-mail is empty."
        return false
      } else {
        if (!this.checkMinMaxString(email, 7, 255)) {
          this.message = "Amount of character invalid for e-mail. Min=7 and Max=255."
          return false
        } else {
          if (!this.checkPattern(email, """^([a-zA-Z0-9_\-\.]+)@([a-zA-Z0-9_\-\.]+)\.([a-zA-Z]{2,5})$""")) {
            this.message = "Invalid E-mail."
            return false
          } else {
            if (this.checkExistsEmail(email)) {
              this.message = "E-mail is already registered."
              return false
            }
          }
        }
      }
      return true
    }
    return true
  }

  def validateSalary(salary: String): Boolean = {
    if (this.valid) {
      if (this.checkEmpty(salary)) {
        this.message = "Salary is empty."
        return false
      } else {
        if (!this.checkIfIsBigDecimal(salary)) {
          this.message = "Invalid salary (format!)."
          return false
        } else {
          if (!this.checkRangeNumber(BigDecimal(salary), 0, 999999999999.99)) {
            this.message = "Invalid salary."
            return false
          }
        }
      }
      return true
    }
    return true
  }

  def validateBirthday(birthday: String): Boolean = {
    if (this.valid) {
      if (this.checkEmpty(birthday)) {
        this.message = "Birthday is empty."
        return false
      } else {
        if (this.checkPattern(birthday, """^(|(0[1-9])|(1[0-2]))\/((0[1-9])|(1\d)|(2\d)|(3[0-1]))\/((\d{4}))$""")) {
          import java.time.LocalDate
          if (!this.checkDateRange(new Date(birthday), new Date(s"${LocalDate.now.getMonthValue}/${LocalDate.now.getDayOfMonth}/${LocalDate.now.getYear - 18}"))) {
            this.message = "Only 18 people can be registered."
            return false
          }
        } else {
          this.message = "Invalid Birthday."
          return false
        }
      }
      return true
    }
    return true
  }

  def validateGender(gender: String): Boolean = {
    if (this.valid) {
      if (this.checkEmpty(gender)) {
        this.message = "Gender is empty."
        return false
      } else {
        if (!this.checkMinMaxString(gender, 1, 1)) {
          this.message = "Amount of character invalid for gender."
          return false
        } else {
          if (!this.checkPattern(gender, """^(M|F|M/F)$""")) {
            this.message = "Invalid gender."
            return false
          }
        }
      }
      return true
    }
    return true
  }

  /*
  def validatePicture(path: String): Boolean = {
    if (this.valid) {
      if (this.checkEmpty(path)) {
        this.message = "Picture is empty."
        return false
      } else {
        if (!this.checkImageExtension(path)) {
          this.message = "Invalid picture."
          return false
        } else {
          if (!this.checkImageSize(path, 200, 1000)) {
            this.message = "Invalid picture size. Minimum of 200 KB and maximum of 1 MB."
            return false
          }
        }
      }
      return true
    }
    return true
  }
  */

  def validatePicture(path: String, statusEdit: Boolean): Boolean = {
    if (this.valid) {
      if (!statusEdit) {
        if (this.checkEmpty(path)) {
          this.message = "Picture is empty."
          return false
        }
      }
    }
    return true
  }

  def validateDataLogin(data: String, field: String): Boolean = {
    if (this.checkEmpty(data)) {
      message = s"${field} is empty."
      return false
    } else {
      if (!this.checkMinMaxString(data, 8, 45)) {
        message = s"Amount of character invalid for ${field}."
        return false
      }
    }
    return true
  }

  private def checkExistsEmail(email: String): Boolean = {
    return (op.findByEmail(email) != null)
  }

  private def checkEmpty(data: String): Boolean = if (data.length == 0) true else false

  private def checkRangeNumber(value: BigDecimal, min: BigDecimal, max: BigDecimal): Boolean = if (value >= min) if (value <= max) true else false else false

  private def checkMinMaxString(value: String, min: Int, max: Int): Boolean = if (value.length >= min) if (value.length <= max) true else false else false

  private def checkPattern(data: String, pattern: String): Boolean = if (data.matches(pattern)) true else false

  import java.lang._

  private def checkIfIsBigDecimal(value: String): Boolean = {
    try {
      val obj = BigDecimal(value)
    } catch {
      case e: NumberFormatException => return false
    }
    return true
  }

  private def checkIfIsDate(date: String): Boolean = {
    try {
      val obj = new Date(date)
    } catch {
      case e: IllegalArgumentException => return false
    }
    return true
  }

  private def checkDateRange(date: Date, max: Date): Boolean = if (date.after(max)) false else true

  /*
  private def checkImageExtension(path: String): Boolean = {
    import util.control.Breaks._
    val extensions: List[String] = List(".png", ".bmp", ".jpg", ".jpge")
    for (e <- extensions) {
      if (path.endsWith(e)) {
        return true
        break
      }
    }
    return false
  }
  */
  // private def checkImageSize(path: String, minsize: Int, maxsize: Int): Boolean = true
}