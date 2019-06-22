package models

import java.util.Date

import scala.math.BigDecimal

case class Person(id: Int, name: String, email: String, salary: BigDecimal, birthday: Date, gender: Char)
