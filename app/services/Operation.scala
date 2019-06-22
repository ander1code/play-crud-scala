package services

import models._

import java.util.Date
import java.sql.{ Connection, ResultSet }
import javax.inject._

import scala.collection.mutable._

import forms._

class Operation @Inject() (fc: Factory, hash: Hash) {

  private var id: Int = 0

  private var people = ListBuffer[Person]()

  private var conn: Connection = null

  def findByName(name: String = ""): ListBuffer[Person] = {
    try {
      val rs: ResultSet = fc.connect.createStatement.executeQuery(s"SELECT * FROM PERSON WHERE PERSON.NAME LIKE '${name}%';")
      val list: ListBuffer[Person] = this.resultsetTolistBuffer(rs)
      fc.close
      return list
    } catch {
      case _ => {
        fc.close
        return null
      }
    }
  }

  def findById(id: Int): Person = {
    try {
      val rs: ResultSet = fc.connect.createStatement.executeQuery(s"SELECT * FROM PERSON WHERE PERSON.ID = ${id};")
      val person: Person = this.resultsetTolistBuffer(rs)(0)
      fc.close
      return person
    } catch {
      case _ => {
        fc.close
        return null
      }
    }
  }

  def findByEmail(email: String): Person = {
    try {
      val rs: ResultSet = fc.connect.createStatement.executeQuery(s"SELECT * FROM PERSON WHERE PERSON.EMAIL = '${email}';")
      val person: Person = this.resultsetTolistBuffer(rs)(0)
      fc.close
      return person
    } catch {
      case _ => {
        fc.close
        return null
      }
    }
  }

  def savePerson(form: PersonForm): Boolean = {
    try {
      this.conn = fc.connect
      val p: Person = this.createPerson(form)
      if (p.id > 0) {
        this.conn.createStatement.executeUpdate(s"INSERT INTO PERSON VALUES(${p.id},'${p.name}','${p.email}',${p.salary},'${p.birthday}','${p.gender}');")
        this.conn.commit
        fc.close
        return true
      }
      fc.close
      return false
    } catch {
      case _ => {
        this.conn.rollback
        fc.close
      }
    }
    return false
  }

  def updatePerson(id: Int, form: PersonForm): Boolean = {
    if (this.findById(id) != null) {
      try {
        this.id = id
        val p: Person = this.createPerson(form)
        this.conn = fc.connect
        this.conn.createStatement.executeUpdate(s"UPDATE PERSON SET NAME = '${p.name}', EMAIL = '${p.email}', SALARY = ${p.salary}, BIRTHDAY = '${p.birthday}', GENDER = '${p.gender}' WHERE ID = ${p.id};")
        this.conn.commit
        fc.close
        return true
      } catch {
        case _ => {
          this.conn.rollback
          fc.close
        }
      }
      return false
    }
    return false
  }

  def deletePerson(id: Int): Boolean = {
    if (this.findById(id) != null) {
      try {
        this.conn = fc.connect
        this.conn.createStatement.executeUpdate(s"DELETE FROM PERSON WHERE ID = ${id};")
        this.conn.commit
        fc.close
        return true
      } catch {
        case _ => {
          this.conn.rollback
          fc.close
        }
      }
      return false
    }
    return false
  }

  def login(form: LoginForm): Boolean = {
    try {
      val u: User = this.createUser(form)
      val user = """"user""""
      var rs: ResultSet = fc.connect.createStatement.executeQuery(s"SELECT * FROM ${user} WHERE USERNAME = '${u.username}' AND password = '${u.password}';")
      var result: Boolean = rs.next()
      fc.close
      return result
    } catch {
      case e: Exception => {
        fc.close
        return false
      }
    }
  }

  private def resultsetTolistBuffer(rs: ResultSet): ListBuffer[Person] = {
    var list: ListBuffer[Person] = ListBuffer[Person]()
    while (rs.next()) {
      list += this.createPerson(rs)
    }
    return list
  }

  private def createPerson[T](t: T): Person = {

    if (t.getClass.getSimpleName.toString == "HikariProxyResultSet") {
      val rs = t.asInstanceOf[ResultSet]
      return new Person(rs.getInt("id"), rs.getString("name"), rs.getString("email"), rs.getBigDecimal("salary"), rs.getDate("birthday"), rs.getString("gender").charAt(0))
    }

    if (t.getClass.getSimpleName.toString == "PersonForm") {
      val pf = t.asInstanceOf[PersonForm]
      if (this.id > 0) {
        return new Person(this.id, pf.name.toString, pf.email.toString, BigDecimal(pf.salary.toString), new Date(pf.birthday.toString), pf.gender.charAt(0))
      }
      return new Person(this.generateID, pf.name.toString, pf.email.toString, BigDecimal(pf.salary.toString), new Date(pf.birthday.toString), pf.gender.charAt(0))
    }
    return null
  }

  private def createUser(form: LoginForm): User = {
    return new User(0, form.username.toString, hash.stringToSHA512(form.password.toString))
  }

  private def generateID(): Int = {
    try {
      val rs: ResultSet = this.conn.createStatement.executeQuery("SELECT MAX(ID) + 1 FROM PERSON;")
      rs.next()
      val id: Int = rs.getInt(1)
      return id
    } catch {
      case _ => {
        return -1
      }
    }
  }
}