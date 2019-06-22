package services

import javax.inject._
import java.sql.Connection
import java.sql.Connection.TRANSACTION_READ_COMMITTED

import play.api.db._

class Factory @Inject() (db: Database) {

  private var conn: Connection = null

  def connect(): Connection = {
    try {
      this.conn = db.getConnection()
      this.conn.setTransactionIsolation(TRANSACTION_READ_COMMITTED)
      this.conn.setAutoCommit(false)
      return this.conn
    } catch {
      case _ => return null
    }
  }

  def close(): Unit = {
    this.conn.close()
  }
}