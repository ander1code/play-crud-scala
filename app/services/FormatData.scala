package services

import java.util.Date
import java.text._

import java.time.LocalDate

class FormatData {
  def formatDateToView(date: Date): String = {
    var format = new SimpleDateFormat("MM/dd/yyyy")
    var dateString = format.format(date)
    dateString.toString()
  }

  def formatGender(gender: String): String = if (gender == "M") "Male" else if (gender == "F") "Female" else "Invalid!"
}